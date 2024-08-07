public class Node implements Comparable<Node> {
    private int to;
    private int minValue;
    private int minValue2 = Integer.MAX_VALUE;
    private int origin1 = -1;
    private int origin2 = -1;

    public Node(int to) {
        this.to = to;
        this.minValue = Integer.MAX_VALUE;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMinValue2() {
        return minValue2;
    }

    public void setMinValue2(int minValue2) {
        this.minValue2 = minValue2;
    }

    public int getOrigin1() {
        return origin1;
    }

    public void setOrigin1(int origin1) {
        this.origin1 = origin1;
    }

    public int getOrigin2() {
        return origin2;
    }

    public void setOrigin2(int origin2) {
        this.origin2 = origin2;
    }

    @Override
    public int compareTo(Node o) {
        return this.minValue - o.minValue;
    }
}
