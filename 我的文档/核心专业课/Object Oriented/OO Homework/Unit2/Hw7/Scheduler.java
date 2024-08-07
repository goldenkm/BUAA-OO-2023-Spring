import com.oocourse.elevator3.TimableOutput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Scheduler {
    private RequestQueue requestQueue;

    private ArrayList<Elevator> elevators = new ArrayList<>();

    public ArrayList<Elevator> getElevators() {
        synchronized (elevators) {
            return elevators;
        }
    }

    public void addElevator(Elevator elevator) {
        synchronized (elevators) {
            elevators.add(elevator);
            FloorGraph.updateGraph(elevators);
            new Thread(elevator, "e" + elevator.getId()).start();
        }
    }

    public void setElevators(ArrayList<Elevator> elevators) {
        synchronized (this.elevators) {
            this.elevators = elevators;
        }
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public void setRequestQueue(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public Scheduler(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public void maintainElevator(int id) {
        synchronized (elevators) {
            // 进得去for循环，但是进不去if
            for (int i = 0; i < elevators.size(); i++) {
                if (elevators.get(i).getId() == id) {
                    elevators.get(i).setMaintainFlag(true);
                }
            }
        }
        synchronized (requestQueue) {
            requestQueue.notifyAll();
        }
    }

    public Schedule getSchedule(Elevator elevator) throws InterruptedException {
        synchronized (requestQueue) {
            HashMap<Integer, ArrayList<Passenger>> waitingTable = requestQueue.getWaitingTable();
            ArrayList<Passenger> passengers = new ArrayList<>();
            if (waitingTable.containsKey(elevator.getNow())) {
                passengers = waitingTable.get(elevator.getNow());
            }
            // 如果电梯被维护则跳过
            if (elevator.isMaintained()) {
                return Schedule.MAINTAIN;
            }
            if (someoneWantsIn(elevator, passengers) || someoneWantsOut(elevator)) {
                return Schedule.OPEN;
            }
            //电梯里有人
            if (!elevator.getPassengers().isEmpty()) {
                if (goStraight(elevator)) {
                    return Schedule.MOVE;
                } else {
                    return Schedule.REVERSE;
                }
            }
            // 电梯里没人
            Set<Integer> floors = new HashSet<>();
            for (Integer floor : waitingTable.keySet()) {
                if (waitingTable.get(floor).size() > 0) {
                    floors.add(floor);
                }
            }
            // 请求队列非空
            if (!requestQueue.isEmpty()) {
                if (someoneAhead(floors, elevator)) {
                    return Schedule.MOVE;
                } else if (someoneIsAccessible(waitingTable, elevator)) {
                    return Schedule.REVERSE;
                } else {
                    requestQueue.wait();
                    return Schedule.WAIT;
                }
            } else {
                if (requestQueue.isEnd() && elevatorsAreEmpty()) {
                    requestQueue.notifyAll();
                    return Schedule.OVER;
                } else {
                    requestQueue.wait();
                    return Schedule.WAIT;
                }
            }
        }
    }

    public boolean elevatorsAreEmpty() {
        synchronized (elevators) {
            for (Elevator elevator : elevators) {
                if (!elevator.getPassengers().isEmpty()) {
                    return false;
                }
            }
            return true;
        }
    }

    public boolean goStraight(Elevator elevator) {
        for (Passenger passenger : elevator.getPassengers()) {
            if (elevator.getDirection() == passenger.getDirection()) {
                return true;
            }
        }
        return false;
    }

    public boolean someoneWantsIn(Elevator elevator, ArrayList<Passenger> passengers) {
        synchronized (requestQueue) {
            //System.out.println(elevator.getId()+": "+passengers.size()+"!");
            if (elevator.isOverloaded()) {
                return false;
            }
            if (requestQueue.isEmpty()) {
                return false;
            }
            if (passengers.size() == 0) {
                return false;
            }
            for (Passenger passenger : passengers) {
                if (passenger.getDirection() == elevator.getDirection()
                        && elevator.isAccessible(passenger.getToFloor())
                        && elevator.isAccessible(passenger.getFromFloor())
                        && elevator.isAccessible((elevator.getNow()))) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean someoneCanEnter(Passenger passenger, Elevator elevator) {
        return passenger.getDirection() == elevator.getDirection()
                && elevator.isAccessible(passenger.getToFloor());
    }

    public void someoneGetsIn(Elevator elevator,
                              ArrayList<Passenger> passengers, ArrayList<Passenger> buffer) {
        synchronized (requestQueue) {
            if (!someoneWantsIn(elevator, passengers)) {
                return;
            }
            if (elevator.getPassengers().size() + buffer.size() >= elevator.getCapacity()) {
                return;
            }
            //System.out.println(requestQueue.getRequestNum()+"?");
            for (Passenger passenger : passengers) {
                if (someoneCanEnter(passenger, elevator)) {
                    requestQueue.removeRequest(passenger.getFromFloor(), passenger);
                    buffer.add(passenger);
                    TimableOutput.println("IN-" + passenger.getId() + "-" +
                            elevator.getNow() + "-" + elevator.getId());
                    if (elevator.getPassengers().size() + buffer.size() >= elevator.getCapacity()) {
                        return;
                    }
                }
            }
            //System.out.println(requestQueue.getRequestNum()+"!");
        }
    }

    public boolean someoneWantsOut(Elevator elevator) {
        for (Passenger passenger : elevator.getPassengers()) {
            if (passenger.getToFloor() == elevator.getNow()
                    && elevator.isAccessible(elevator.getNow())) {
                return true;
            }
        }
        return false;
    }

    public void someoneGetsOut(Elevator elevator) {
        if (!someoneWantsOut(elevator)) {
            return;
        }
        // 小心线程并发
        ArrayList<Passenger> passengers = elevator.getPassengers();
        for (int i = 0; i < passengers.size(); i++) {
            Passenger passenger = passengers.get(i);
            if (passenger.getToFloor() == elevator.getNow()) {
                TimableOutput.println("OUT-" + passenger.getId() + "-" +
                        elevator.getNow() + "-" + elevator.getId());
                passengers.remove(passenger);
                passenger.setPos(passenger.getPos() + 1);
                // 如果还没到就再加入队列
                if (passenger.getDestination() != elevator.getNow()) {
                    passenger.updatePath();
                    requestQueue.addRequest(passenger.getFromFloor(), passenger);
                }
                i--;
            }
        }
    }

    //判断人相对于电梯的位置
    public boolean someoneAhead(Set<Integer> floors, Elevator elevator) {
        for (Integer floorOfPassenger : floors) {
            if ((elevator.getDirection() == 1 && floorOfPassenger > elevator.getNow())
                    || (elevator.getDirection() == -1 && floorOfPassenger < elevator.getNow())) {
                if (elevator.isAccessible(floorOfPassenger)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean someoneIsAccessible(HashMap<Integer,
            ArrayList<Passenger>> waitingTable, Elevator elevator) {
        for (int i = 1; i <= 11; i++) {
            if (waitingTable.get(i) != null) {
                for (Passenger passenger : waitingTable.get(i)) {
                    if (elevator.isAccessible(passenger.getFromFloor())
                            && elevator.isAccessible(passenger.getToFloor())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
