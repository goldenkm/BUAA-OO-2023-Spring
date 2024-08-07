import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;

public class InputHandler implements Runnable {
    private ElevatorInput elevatorInput;
    private RequestQueue requestQueue;

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public void setRequestQueue(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public InputHandler() {
        elevatorInput = new ElevatorInput(System.in);
    }

    @Override
    public void run() {
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                requestQueue.setEnd(true);
                break;
            } else {
                int fromFloor = request.getFromFloor();
                Passenger passenger = new Passenger(request.getPersonId(),
                        request.getFromFloor(),
                        request.getToFloor());
                requestQueue.addRequest(fromFloor, passenger);
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