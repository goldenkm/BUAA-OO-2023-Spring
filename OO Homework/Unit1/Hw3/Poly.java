import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Poly {
    private ArrayList<Mono> monomials = new ArrayList<>();

    public ArrayList<Mono> getMonomials() {
        return monomials;
    }

    public Poly() {
    }

    public Poly(ArrayList<Mono> monomials) {
        this.monomials = monomials;
    }

    public Poly addPoly(Poly other) {
        ArrayList<Mono> newMonomials = new ArrayList<>();
        newMonomials.addAll(monomials);
        newMonomials.addAll(other.monomials);
        Poly poly = new Poly(newMonomials);
        return Merge.merge(poly);
    }

    public Poly mulPoly(Poly other) {
        ArrayList<Mono> newMonomials = new ArrayList<>();
        for (Mono myMono : this.monomials) {
            for (Mono otherMono : other.monomials) {
                BigInteger newNum = myMono.getNum().multiply(otherMono.getNum());
                if (newNum.equals(BigInteger.ZERO)) {
                    newMonomials.add(new Mono(BigInteger.ZERO, 0, 0, 0));
                    continue;
                }
                HashMap<Poly, Integer> sin = new HashMap<>();
                sin.putAll(myMono.getSin());
                for (Map.Entry<Poly, Integer> otherEntry : otherMono.getSin().entrySet()) {
                    int flag = 0;
                    for (Map.Entry<Poly, Integer> myEntry : sin.entrySet()) {
                        if (myEntry.getKey().equals((otherEntry.getKey()))) {
                            flag = 1;
                            int index = Merge.getIndex(otherMono.getSin(), otherEntry.getKey());
                            sin.put(myEntry.getKey(), myEntry.getValue() + index);
                        }
                    }
                    if (flag == 0) {
                        sin.put(otherEntry.getKey(), otherEntry.getValue());
                    }
                }
                HashMap<Poly, Integer> cos = new HashMap<>();
                cos.putAll(myMono.getCos());
                for (Map.Entry<Poly, Integer> otherEntry : otherMono.getCos().entrySet()) {
                    int flag = 0;
                    for (Map.Entry<Poly, Integer> myEntry : cos.entrySet()) {
                        if (myEntry.getKey().equals((otherEntry.getKey()))) {
                            flag = 1;
                            int index = Merge.getIndex(otherMono.getCos(), otherEntry.getKey());
                            cos.put(myEntry.getKey(), myEntry.getValue() + index);
                        }
                    }
                    if (flag == 0) {
                        cos.put(otherEntry.getKey(), otherEntry.getValue());
                    }
                }
                Mono newMono = new Mono(newNum,
                        myMono.getIndexX() + otherMono.getIndexX(),
                        myMono.getIndexY() + otherMono.getIndexY(),
                        myMono.getIndexZ() + otherMono.getIndexZ());
                newMono.setSin(sin);
                newMono.setCos(cos);
                newMonomials.add(newMono);
            }
        }
        Poly poly = new Poly(newMonomials);
        return Merge.merge(poly);
    }

    public Poly powPoly(int index) {
        int count = index;
        Poly poly = this;
        if (count == 0) {
            Mono mono = new Mono(BigInteger.ONE, 0, 0, 0);
            this.monomials.clear();
            this.monomials.add(mono);
        }
        while (count > 1) {
            poly = this.mulPoly(poly);
            count--;
        }
        return Merge.merge(poly);
    }

    public Poly toNegative() {
        for (Mono monomial : this.monomials) {
            monomial.setNum(monomial.getNum().multiply(BigInteger.valueOf(-1)));
            //monomial.setSign(-monomial.getSign());
        }
        return this;
    }

    public String toString() {
        if (monomials.size() == 0) {
            return "0";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(monomials.get(0).toString());
        if (monomials.size() > 1) {
            for (int i = 1; i < monomials.size(); i++) {
                sb.append("+");
                sb.append(monomials.get(i).toString());
            }
        }
        return sb.toString();
    }

    public boolean equals(Poly other) {
        if (this.monomials.size() != other.monomials.size()) {
            return false;
        }
        ArrayList<String> strings1 = new ArrayList<>();
        for (int i = 0; i < this.monomials.size(); i++) {
            strings1.add(this.monomials.get(i).toString());
        }
        strings1.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        ArrayList<String> strings2 = new ArrayList<>();
        for (int i = 0; i < other.monomials.size(); i++) {
            strings2.add(other.monomials.get(i).toString());
        }
        strings2.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        for (int i = 0; i < strings1.size(); i++) {
            if (strings1.get(i).compareTo(strings2.get(i)) != 0) {
                return false;
            }
        }
        return true;
    }

    public Poly clone() {
        ArrayList<Mono> clonedMonos = new ArrayList<>();
        for (Mono monomial : this.monomials) {
            clonedMonos.add(monomial.clone());
        }
        return new Poly(clonedMonos);
    }
}
