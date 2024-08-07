import java.math.BigInteger;
import java.util.ArrayList;

public class PowFunc implements Factor {
    private String var;
    private int index = 1;

    public PowFunc() {
    }

    public void setVar(String var) {
        this.var = var;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String toString() {
        if (this.index == 0) {
            return var;
        }
        return var + "**" + index;
    }

    public Poly toPoly() {
        ArrayList<Mono> monomials = new ArrayList<>();
        if (var.equals("x")) {
            Mono mono = new Mono(BigInteger.ONE, this.index, 0, 0);
            monomials.add(mono);
        } else if (var.equals("y")) {
            Mono mono = new Mono(BigInteger.ONE, 0, this.index, 0);
            monomials.add(mono);
        } else if (var.equals("z")) {
            Mono mono = new Mono(BigInteger.ONE, 0, 0, this.index);
            monomials.add(mono);
        }
        Poly poly = new Poly(monomials);
        return poly;
    }

}
