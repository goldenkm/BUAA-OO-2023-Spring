package Factor;

public class Cos implements Factor {
    
    private Factor factor;
    
    public Cos(Factor factor) {
        this.factor = factor;
    }
    
    public Sin toSin() {
        return new Sin(factor);
    }
    
    @Override
    public String toString() {
        return "cos(" + factor.toString() + ")";
    }
    
    @Override
    public Factor derive() {
        Term term = new Term();
        /* TODO 3 */
        Sin sin = this.toSin();
        Number negativeOne = new Number("-1");
        term.addFactor(negativeOne);
        term.addFactor(sin);
        term = Term.mergeTerm(term, (Term) factor.derive());
        return term;
    }
    
    @Override
    public Factor clone() {
        return new Cos(factor.clone());
    }
}
