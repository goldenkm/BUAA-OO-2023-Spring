import com.oocourse.elevator1.TimableOutput;

import java.util.ArrayList;
import java.util.HashMap;

public class Elevator implements Runnable {
    private int direction = 1;  // up: 1; down: -1
    private int id;
    private int now = 1;
    private ArrayList<Passenger> passengers = new ArrayList<>();
    private Scheduler scheduler;

    private static final int MAX_PASSENGER_NUM = 6;

    public Elevator() {
    }

    public Elevator(int id, Scheduler scheduler) {
        this.id = id;
        this.scheduler = scheduler;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
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
        return passengers.size() >= MAX_PASSENGER_NUM;
    }

    public boolean reachBoundary() {
        if (now == 11 || now == 0) {
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
                //System.out.println(schedule + " and " + id);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (schedule == Schedule.OVER) {
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
            //wait没写，但我不知道该写啥
        }
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
        Thread.sleep(400);
        TimableOutput.println("ARRIVE-" + now + "-" + id);
    }

    public void open() throws InterruptedException {
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
