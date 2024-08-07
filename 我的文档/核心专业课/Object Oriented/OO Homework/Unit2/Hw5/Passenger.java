public class Passenger {
    private int id;
    private int fromFloor;
    private int toFloor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromFloor() {
        return fromFloor;
    }

    public void setFromFloor(int fromFloor) {
        this.fromFloor = fromFloor;
    }

    public int getToFloor() {
        return toFloor;
    }

    public void setToFloor(int toFloor) {
        this.toFloor = toFloor;
    }

    public Passenger(int id, int fromFloor, int toFloor) {
        this.id = id;
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;
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
