public class MyPair implements Comparable<MyPair> {
    private int id;
    private int value;

    public MyPair(int id, int value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(MyPair o) {
        return this.value - o.getValue();
    }
}
