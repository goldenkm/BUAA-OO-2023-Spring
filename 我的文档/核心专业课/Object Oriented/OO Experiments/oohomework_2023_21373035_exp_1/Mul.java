public class Mul extends Operator {
    public Mul(Operator left, Operator right) {
        super(left, right);
    }

    public int getResult() {
        // TODO done
        return getLeft().getResult() * getRight().getResult();
    }
}
