import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class TriFunc implements Factor {
    private int type;   //sin: 1; cos: -1
    private Factor factor;

    private int index = 1;

    public TriFunc() {
    }

    public TriFunc(int type, Factor factor) {
        this.type = type;
        this.factor = factor;
    }

    public void setFactor(Factor factor) {
        this.factor = factor;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public Poly toPoly() {
        ArrayList<Mono> monos = new ArrayList<>();
        HashMap<Poly, Integer> triFuncs = new HashMap<>();
        if (factor.toPoly().toString().equals("0")) {   //化简sin(0) = 0, cos(0) = 1
            if (type == 1) {
                Mono zero = new Mono(BigInteger.ZERO, 0, 0, 0);
                monos.add(zero);
            } else {
                Mono one = new Mono(BigInteger.ONE, 0, 0, 0);
                monos.add(one);
            }
            return new Poly(monos);
        }
        triFuncs.put(factor.toPoly(), index);
        Mono mono = new Mono(BigInteger.ONE, 0, 0, 0);
        if (type == 1) {
            mono.setSin(triFuncs);
        } else {
            mono.setCos(triFuncs);
        }
        monos.add(mono);
        Poly poly = new Poly(monos);
        return poly;
    }

    public String toString() {
        if (type == 1) {
            return "sin(" + factor.toString()+")";
        } else {
            return "cos(" + factor.toString()+")";
        }
    }
}
