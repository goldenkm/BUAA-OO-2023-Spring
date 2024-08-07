import java.math.BigInteger;
import java.util.ArrayList;

public class Number implements Factor {
    private BigInteger num;

    public void setNum(BigInteger num) {
        this.num = num;
    }

    public BigInteger getNum() {
        return num;
    }

    public Number() {
    }

    public Number(BigInteger num) {
        this.num = num;
    }

    public String toString() {
        return this.num.toString();
    }

    public Poly toPoly() {
        ArrayList<Mono> monomials = new ArrayList<>();
        Mono mono = new Mono(num, 0, 0, 0);
        monomials.add(mono);
        Poly poly = new Poly(monomials);
        return poly;
    }
}
