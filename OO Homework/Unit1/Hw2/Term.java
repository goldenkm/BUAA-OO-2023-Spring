import java.util.ArrayList;

public class Term {

    private int sign = 1;
    private ArrayList<Factor> factors;

    public Term() {
        this.factors = new ArrayList<>();
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Factor factor : factors) {
            sb.append(factor.toString());
            sb.append(" ");
        }
        return sb.toString();
    }

    public Poly toPoly() {
        Poly poly = new Poly();
        if (factors.size() == 1) {
            if (sign == -1) {
                return factors.get(0).toPoly().toNegative();
            }
            return factors.get(0).toPoly();
        }
        poly = factors.get(0).toPoly();
        for (int i = 1; i < factors.size(); i++) {
            Factor factor = factors.get(i);
            //System.out.println("!!"+factor.toPoly().toString());
            Poly tmp = poly.mulPoly(factor.toPoly());
            poly = tmp;
        }
        if (sign == -1) {
            return poly.toNegative();
        }
        return poly;
    }
}
