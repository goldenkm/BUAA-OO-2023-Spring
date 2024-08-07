import java.util.ArrayList;
import java.util.HashMap;

public class RequestQueue {
    private boolean end;
    private int requestNum = 0;
    private HashMap<Integer, ArrayList<Passenger>> waitingTable = new HashMap<>();

    public HashMap<Integer, ArrayList<Passenger>> getWaitingTable() {
        return waitingTable;
    }

    public synchronized boolean isEnd() {
        if (end == true) {
            notifyAll();
        }
        return end;
    }

    public synchronized void setEnd(boolean flag) {
        end = flag;
    }

    public int getRequestNum() {
        return requestNum;
    }

    public synchronized boolean isEmpty() {
        return requestNum == 0;
    }

    public void setWaitingTable(HashMap<Integer, ArrayList<Passenger>> waitingTable) {
        this.waitingTable = waitingTable;
    }

    public RequestQueue() {
        end = false;
    }

    public synchronized void addRequest(int fromFloor, Passenger passenger) {
        requestNum++;
        if (!waitingTable.containsKey(fromFloor)) {
            ArrayList passengers = new ArrayList<>();
            passengers.add(passenger);
            waitingTable.put(fromFloor, passengers);
        } else {
            waitingTable.get(fromFloor).add(passenger);
            waitingTable.put(fromFloor, waitingTable.get(fromFloor));
        }
        notify();
    }

    public synchronized void removeRequest(int fromFloor, Passenger target) {
        if (requestNum == 0) {
            return;
        }
        // 小心线程并发
        ArrayList<Passenger> passengers = new ArrayList<>();
        for (Passenger passenger : waitingTable.get(fromFloor)) {
            if (passenger.equals(target)) {
                continue;
            }
            passengers.add(passenger);
        }
        requestNum--;
        waitingTable.put(fromFloor, passengers);
    }
}
