
public class Derivatives implements Factor {
    private String var;
    private Poly polyFactor;

    public Derivatives() {
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public Poly getPolyFactor() {
        return polyFactor;
    }

    public void setPolyFactor(Poly factor) {
        this.polyFactor = factor;
    }

    @Override
    public Poly toPoly() {
        Poly poly = new Poly();
        for (Mono monomial : polyFactor.getMonomials()) {
            int flag = 0;
            if (var.equals("x")) {
                flag = 1;
            } else if (var.equals("y")) {
                flag = 2;
            } else if (var.equals("z")) {
                flag = 3;
            }
            poly = poly.addPoly(DerivativesAnalyzer.derivation(monomial, flag));
        }
        return poly;
    }
}
