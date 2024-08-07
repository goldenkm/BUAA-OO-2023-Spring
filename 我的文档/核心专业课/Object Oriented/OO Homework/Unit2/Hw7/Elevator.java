import com.oocourse.elevator3.TimableOutput;

import java.util.ArrayList;
import java.util.HashMap;

public class Elevator implements Runnable {
    private int direction = 1;  // up: 1; down: -1
    private boolean maintainFlag = false;
    private int id;
    private int now = 1;
    private int capacity;
    private double speed;
    private ArrayList<Integer> access = new ArrayList<>();
    private ArrayList<Passenger> passengers = new ArrayList<>();
    private Scheduler scheduler;

    public Elevator() {
    }

    public Elevator(int id, Scheduler scheduler, int now,
                    int maxPassengerNum, double speed, int access) {
        this.id = id;
        this.scheduler = scheduler;
        this.now = now;
        this.capacity = maxPassengerNum;
        this.speed = speed;
        //TimableOutput.println(access+"!");
        String accessFloor = Integer.toBinaryString(access);
        for (int i = accessFloor.length() - 1; i >= 0; i--) {
            if (accessFloor.charAt(i) == '1') {
                this.access.add(accessFloor.length() - i);
            }
        }
        //TimableOutput.println(this.access+"!!");
    }

    public boolean isAccessible(int targetFloor) {
        return access.contains(targetFloor);
    }

    public ArrayList<Integer> getAccess() {
        return access;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isMaintained() {
        return maintainFlag;
    }

    public synchronized void setMaintainFlag(boolean maintainFlag) {
        this.maintainFlag = maintainFlag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNow() {
        return now;
    }

    public void setNow(int now) {
        this.now = now;
    }

    public ArrayList<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(ArrayList<Passenger> passengers) {
        this.passengers = passengers;
    }

    public void addPassenger(Passenger passenger) {
        this.passengers.add(passenger);
    }

    public boolean isOverloaded() {
        return passengers.size() >= capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean reachBoundary() {
        if (now == 11 || now == 1) {
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        while (true) {
            Schedule schedule = null;
            try {
                schedule = scheduler.getSchedule(this);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (schedule == Schedule.OVER) {
                return;
            } else if (schedule == Schedule.MAINTAIN) {
                maintain();
                return;
            } else if (schedule == Schedule.MOVE) {
                try {
                    move();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else if (schedule == Schedule.REVERSE) {
                direction *= -1;
            } else if (schedule == Schedule.OPEN) {
                // 二次咨询
                synchronized (scheduler.getRequestQueue()) {
                    //加锁是必要的，否则多部电梯会同时开门
                    try {
                        schedule = scheduler.getSchedule(this);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (schedule == Schedule.OPEN) {
                        try {
                            open();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    public void maintain() {
        synchronized (scheduler.getElevators()) {
            //有可能删不掉
            scheduler.getElevators().remove(this);
            FloorGraph.updateGraph(scheduler.getElevators());
        }
        synchronized (scheduler.getRequestQueue()) {
            if (!passengers.isEmpty()) {
                TimableOutput.println("OPEN-" + now + "-" + id);
                for (Passenger passenger : passengers) {
                    TimableOutput.println("OUT-" + passenger.getId() + "-" +
                            now + "-" + id);
                    // 维护但是没到地方，就再加入请求队列
                    if (passenger.getDestination() != now) {
                        // 及时更新passenger的出发楼层！！
                        passenger.setFromFloor(now);
                        passenger.setPath(getNewPath(passenger));
                        scheduler.getRequestQueue().addRequest(now, passenger);
                    }
                }
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                TimableOutput.println("CLOSE-" + now + "-" + id);
            }
            TimableOutput.println("MAINTAIN_ABLE-" + id);
            passengers = new ArrayList<>();
            //更新所有还在等待的乘客的路径
            scheduler.getRequestQueue().updatePath();
        }
    }

    public ArrayList<Integer> getNewPath(Passenger passenger) {
        FloorGraph.updateGraph(scheduler.getElevators());
        ArrayList<Integer> path =
                FloorGraph.findShortestPath(now, passenger.getDestination());
        if (path == null) {
            path = new ArrayList<>();
            path.add(now);
            path.add(passenger.getDestination());
        }
        return path;
    }

    public void move() throws InterruptedException {
        if (direction != -1) {
            now += 1;
        } else {
            now -= 1;
        }
        if (this.reachBoundary()) {
            direction *= -1;
        }
        Thread.sleep((long) (1000 * speed));
        TimableOutput.println("ARRIVE-" + now + "-" + id);
    }

    public void open() throws InterruptedException {
        synchronized (scheduler.getRequestQueue()) {
            ArrayList<Passenger> passengersOutside = new ArrayList<>();
            HashMap<Integer, ArrayList<Passenger>> waitingTable
                    = scheduler.getRequestQueue().getWaitingTable();
            if (waitingTable.containsKey(now)) {
                passengersOutside = scheduler.getRequestQueue().getWaitingTable().get(now);
            }
            ArrayList<Passenger> buffer = new ArrayList<>();
            TimableOutput.println("OPEN-" + now + "-" + id);
            scheduler.someoneGetsOut(this);
            scheduler.someoneGetsIn(this, passengersOutside, buffer);
            Thread.sleep(400);
            // 电梯进人
            //更新当前楼层的等待序列
            if (waitingTable.containsKey(now)) {
                passengersOutside = scheduler.getRequestQueue().getWaitingTable().get(now);
            }
            scheduler.someoneGetsIn(this, passengersOutside, buffer);
            this.getPassengers().addAll(buffer);
            TimableOutput.println("CLOSE-" + now + "-" + id);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Elevator) {
            return this.id == ((Elevator) other).id;
        }
        return false;
    }
}
