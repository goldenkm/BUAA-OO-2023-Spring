import java.util.ArrayList;

public class Passenger {
    private int id;
    private int fromFloor;
    private int toFloor;
    private int destination;
    private ArrayList<Integer> path = new ArrayList<>();
    private int pos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromFloor() {
        return path.get(pos);
    }

    public void setFromFloor(int fromFloor) {
        this.fromFloor = fromFloor;
    }

    public int getToFloor() {
        return path.get(pos + 1);
    }

    public void setToFloor(int toFloor) {
        this.toFloor = toFloor;
    }

    public ArrayList<Integer> getPath() {
        return path;
    }

    public void setPath(ArrayList<Integer> path) {
        this.pos = 0;
        this.path = path;
        this.fromFloor = path.get(0);
        this.toFloor = path.get(1);
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void updatePath() {
        this.fromFloor = path.get(pos);
        this.toFloor = path.get(pos + 1);
    }

    public int getDestination() {
        return destination;
    }

    public boolean isArrived() {
        return pos == path.size() - 1;
    }

    public Passenger(int id, int destination, ArrayList<Integer> path) {
        this.id = id;
        this.destination = destination;
        this.path = path;
        //System.out.println(id+": "+path);
        this.pos = 0;
        this.fromFloor = path.get(pos);
        this.toFloor = path.get(pos + 1);
    }

    public int getDirection() {
        return toFloor > fromFloor ? 1 : -1;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Passenger) {
            return this.id == ((Passenger) other).id;
        }
        return false;
    }
}
