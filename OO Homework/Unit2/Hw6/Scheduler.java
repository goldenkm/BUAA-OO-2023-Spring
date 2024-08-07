import com.oocourse.elevator2.TimableOutput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Scheduler {
    private static final int MAX_FLOOR = 11;
    private static final int MIN_FLOOR = 1;
    private RequestQueue requestQueue;

    private ArrayList<Elevator> elevators = new ArrayList<>();

    public ArrayList<Elevator> getElevators() {
        return elevators;
    }

    public void addElevator(Elevator elevator) {
        elevators.add(elevator);
        new Thread(elevator).start();
    }

    public void setElevators(ArrayList<Elevator> elevators) {
        this.elevators = elevators;
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
        synchronized (requestQueue) {
            for (Elevator elevator : elevators) {
                if (elevator.getId() == id) {
                    requestQueue.notifyAll();
                    elevator.setMaintainFlag(true);
                }
            }
        }
    }

    public Schedule getSchedule(Elevator elevator) throws InterruptedException {
        //synchronized (requestQueue) {
            HashMap<Integer, ArrayList<Passenger>> waitingTable = requestQueue.getWaitingTable();
            ArrayList<Passenger> passengers = new ArrayList<>();
            if (waitingTable.containsKey(elevator.getNow())) {
                passengers = waitingTable.get(elevator.getNow());
            }
            // 如果电梯被维护则跳过
            if (elevator.isMaintained()) {
                return Schedule.MAINTAIN;
            }
            //System.out.println(elevator.getId()+": "+passengers.size());
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
                } else {
                    return Schedule.REVERSE;
                }
            } else {
                if (requestQueue.isEnd() && elevatorsAreEmpty()) {
                    return Schedule.OVER;
                } else {
                    synchronized (requestQueue) {
                      requestQueue.wait();
                    }
                    return Schedule.WAIT;
                }
            }
        //}
    }

    public boolean elevatorsAreEmpty() {
        for (Elevator elevator : elevators) {
            if (!elevator.getPassengers().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean goStraight(Elevator elevator) {
        for (Passenger passenger : elevator.getPassengers()) {
            if (passenger.getDirection() == passenger.getDirection()) {
                return true;
            }
        }
        return false;
    }

    public boolean someoneWantsIn(Elevator elevator, ArrayList<Passenger> passengers) {
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
            if (passenger.getDirection() == elevator.getDirection()) {
                return true;
            }
        }
        return false;

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
                if (passenger.getDirection() == elevator.getDirection()) {
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
            if (passenger.getToFloor() == elevator.getNow()) {
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
                i--;
            }
        }
    }

    //判断人相对于电梯的位置
    public boolean someoneAhead(Set<Integer> floors, Elevator elevator) {
        for (Integer floorOfPassenger : floors) {
            if ((elevator.getDirection() == 1 && floorOfPassenger > elevator.getNow())
                    || (elevator.getDirection() == -1 && floorOfPassenger < elevator.getNow())
                    || elevator.getDirection() == 0) {
                return true;
            }
        }
        return false;
    }

    public boolean isNearest(int floor, Elevator target) {
        int min = 100;
        int targetDistance = calDistance(floor, target);
        for (Elevator elevator : elevators) {
            if (elevator.getId() == target.getId()) {
                continue;
            }
            min = Math.min(min, calDistance(floor, elevator));
            //System.out.println(elevator.getId()+": "+ calDistance(floor, elevator));
        }
        //System.out.println(target.getId() + ": " + min + ", " + targetDistance);
        return targetDistance <= min;    // min为其他电梯的最小时间
    }

    public int calDistance(int targetFloor, Elevator elevator) {
        if (targetFloor == elevator.getNow()) {
            return 0;
        }
        if ((targetFloor > elevator.getNow() && elevator.getDirection() == 1)
                || (targetFloor < elevator.getNow() && elevator.getDirection() == -1)) {
            return Math.abs(targetFloor - elevator.getNow());
        } else if (targetFloor > elevator.getNow() && elevator.getDirection() == -1) {
            return elevator.getNow() + targetFloor;
        } else if (targetFloor < elevator.getNow() && elevator.getDirection() == 1) {
            return 22 - (elevator.getNow() + targetFloor);
        }
        return 13;
    }
}
