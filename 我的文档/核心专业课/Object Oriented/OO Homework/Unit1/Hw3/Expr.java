import java.util.ArrayList;

public class Expr implements Factor {
    private ArrayList<Term> terms;
    private int index = 1;

    public Expr() {
        this.terms = new ArrayList<>();
    }

    public Expr(ArrayList<Term> terms) {
        this.terms = terms;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Term term : terms) {
            sb.append(term.toString());
            sb.append("+");
        }
        return sb.toString();
    }

    @Override
    public Poly toPoly() {
        Poly poly = new Poly();
        poly = terms.get(0).toPoly();
        if (terms.size() == 1) {
            if (this.index != 1) {
                poly = poly.powPoly(this.index);
            }
            return poly;
        }
        for (int i = 1; i < terms.size(); i++) {
            Term term = terms.get(i);
            Poly tmp = poly.addPoly(term.toPoly());
            poly = tmp;
        }
        if (this.index != 1) {
            poly = poly.powPoly(this.index);
        }
        return poly;
    }
}
