import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.MaintainRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;

import java.io.IOException;

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
                    int fromFloor =  ((PersonRequest) request).getFromFloor();
                    Passenger passenger = new Passenger(((PersonRequest) request).getPersonId(),
                            ((PersonRequest) request).getFromFloor(),
                            ((PersonRequest) request).getToFloor());
                    requestQueue.addRequest(fromFloor, passenger);
                } else if (request instanceof ElevatorRequest) {
                    Elevator newElevator =
                            new Elevator(((ElevatorRequest) request).getElevatorId(),
                                    scheduler,
                                    ((ElevatorRequest) request).getFloor(),
                                    ((ElevatorRequest) request).getCapacity(),
                                    ((ElevatorRequest) request).getSpeed());
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
        /*
        Passenger passenger1 = new Passenger(1, 2, 7);
        Passenger passenger2 = new Passenger(2, 2, 7);
        Passenger passenger3 = new Passenger(3, 2, 7);
        Passenger passenger4 = new Passenger(4, 2, 7);
        Passenger passenger5 = new Passenger(5, 2, 7);
        Passenger passenger6 = new Passenger(6, 2, 7);
        Passenger passenger7 = new Passenger(7, 2, 7);
        Passenger passenger8 = new Passenger(8, 2, 7);
        requestQueue.addRequest(2, passenger1);
        requestQueue.addRequest(2, passenger2);
        requestQueue.addRequest(2, passenger3);
        requestQueue.addRequest(2, passenger4);
        requestQueue.addRequest(2, passenger5);
        requestQueue.addRequest(2, passenger6);
        requestQueue.addRequest(2, passenger7);
        requestQueue.addRequest(2, passenger8);
        */
    }
}