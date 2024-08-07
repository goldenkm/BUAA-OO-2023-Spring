import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.MaintainRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

import java.io.IOException;
import java.util.ArrayList;

public class InputHandler implements Runnable {
    private ElevatorInput elevatorInput;
    private RequestQueue requestQueue;

    private Scheduler scheduler;

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public void setRequestQueue(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public InputHandler() {
        elevatorInput = new ElevatorInput(System.in);
    }

    @Override
    public void run() {
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                requestQueue.setEnd(true);
                break;
            } else {
                if (request instanceof PersonRequest) {
                    int fromFloor = ((PersonRequest) request).getFromFloor();
                    int destination = ((PersonRequest) request).getToFloor();
                    ArrayList<Integer> path = FloorGraph.findShortestPath(fromFloor, destination);
                    if (path == null) {
                        path = new ArrayList<>();
                        path.add(fromFloor);
                        path.add(destination);
                    }
                    Passenger passenger = new Passenger(((PersonRequest) request).getPersonId()
                            , destination, path);
                    requestQueue.addRequest(fromFloor, passenger);
                } else if (request instanceof ElevatorRequest) {
                    Elevator newElevator =
                            new Elevator(((ElevatorRequest) request).getElevatorId(),
                                    scheduler,
                                    ((ElevatorRequest) request).getFloor(),
                                    ((ElevatorRequest) request).getCapacity(),
                                    ((ElevatorRequest) request).getSpeed(),
                                    ((ElevatorRequest) request).getAccess());
                    scheduler.addElevator(newElevator);
                } else if (request instanceof MaintainRequest) {
                    scheduler.maintainElevator(((MaintainRequest) request).getElevatorId());
                }
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}