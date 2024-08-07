import java.util.ArrayList;
import java.util.HashMap;

public class RequestQueue {
    private boolean end;
    private HashMap<Integer, ArrayList<Passenger>> waitingTable = new HashMap<>();

    public HashMap<Integer, ArrayList<Passenger>> getWaitingTable() {
        return waitingTable;
    }

    public synchronized boolean isEnd() {
        return end;
    }

    public synchronized boolean isEmpty() {
        for (int i = 1; i <= 11; i++) {
            if (waitingTable.get(i) != null) {
                if (waitingTable.get(i).size() > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public synchronized void setEnd(boolean flag) {
        end = flag;
        notifyAll();
    }

    public void setWaitingTable(HashMap<Integer, ArrayList<Passenger>> waitingTable) {
        this.waitingTable = waitingTable;
    }

    public RequestQueue() {
        end = false;
    }

    public synchronized void updatePath() {
        for (int i = 1; i <= 11; i++) {
            if (waitingTable.get(i) != null) {
                for (int j = 0; j < waitingTable.get(i).size(); j++) {
                    Passenger passenger = waitingTable.get(i).get(j);
                    ArrayList<Integer> path =
                            FloorGraph.findShortestPath(i, passenger.getDestination());
                    if (path == null) {
                        path = new ArrayList<>();
                        path.add(i);
                        path.add(passenger.getDestination());
                    }
                    passenger.setPath(path);
                }
            }
        }
    }

    public synchronized void addRequest(int fromFloor, Passenger passenger) {
        //TimableOutput.println(passenger.getId()+"!!!");
        if (waitingTable.get(fromFloor) != null) {
            for (Passenger passenger1 : waitingTable.get(fromFloor)) {
                if (passenger.getId() == passenger1.getId()) {
                    return;
                }
            }
        }
        if (!waitingTable.containsKey(fromFloor)) {
            ArrayList passengers = new ArrayList<>();
            passengers.add(passenger);
            waitingTable.put(fromFloor, passengers);
        } else {
            waitingTable.get(fromFloor).add(passenger);
            waitingTable.put(fromFloor, waitingTable.get(fromFloor));
        }
        notifyAll();
    }

    public synchronized void removeRequest(int fromFloor, Passenger target) {
        if (this.isEmpty()) {
            return;
        }
        // 小心线程并发
        ArrayList<Passenger> passengers = new ArrayList<>();
        for (Passenger passenger : waitingTable.get(fromFloor)) {
            if (passenger.getId() == target.getId()) {
                continue;
            }
            passengers.add(passenger);
        }
        waitingTable.put(fromFloor, passengers);
    }
}
