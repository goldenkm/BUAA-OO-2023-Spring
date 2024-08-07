import java.math.BigInteger;
import java.util.*;

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
        return merge(poly);
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
                Mono newMono = new Mono(newNum,
                        myMono.getIndexX() + otherMono.getIndexX(),
                        myMono.getIndexY() + otherMono.getIndexY(),
                        myMono.getIndexZ() + otherMono.getIndexZ());
                HashMap<Poly, Integer> sin = new HashMap<>();
                sin.putAll(myMono.getSin());
                HashMap<Poly, Integer> cos = new HashMap<>();
                cos.putAll(myMono.getCos());
                if (triFuncHaveSameFactor(myMono, otherMono)) {     //处理三角函数的幂
                    for (Map.Entry<Poly, Integer> entry : sin.entrySet()) {
                        sin.put(entry.getKey(), entry.getValue() + 1);
                    }
                    for (Map.Entry<Poly, Integer> entry : cos.entrySet()) {
                        cos.put(entry.getKey(), entry.getValue() + 1);
                    }
                } else {
                    sin.putAll(otherMono.getSin());
                    cos.putAll(otherMono.getCos());
                }
                newMono.setSin(sin);
                newMono.setCos(cos);
                newMonomials.add(newMono);
            }
        }
        Poly poly = new Poly(newMonomials);
        return merge(poly);
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
        return merge(poly);
    }

    public Poly toNegative() {
        for (Mono monomial : this.monomials) {
            monomial.setNum(monomial.getNum().multiply(BigInteger.valueOf(-1)));
            //System.out.println(monomial.toString()+"!");
        }
        return this;
    }

    public Poly merge(Poly poly) {
        ArrayList<Mono> monos = poly.getMonomials();
        HashSet<Integer> visited = new HashSet<>();
        int len = monos.size();
        for (int i = 0; i < len; i++) {     //合并同类项
            if (visited.contains(i)) {
                continue;
            }
            for (int j = i + 1; j < len; j++) {
                if (visited.contains(j)) {
                    continue;
                }
                Mono mono1 = monos.get(i);
                Mono mono2 = monos.get(j);
                //System.out.println(mono1.toString()+" + "+mono2.toString());
                if (haveSameVar(mono1, mono2)) {
                    //System.out.println(mono1.toString() + " + " + mono2.toString());
                    BigInteger newNum = mono1.getNum().add(mono2.getNum());
                    if (newNum.compareTo(BigInteger.ZERO) != 0) {
                        mono1.setNum(newNum);
                    } else {
                        visited.add(i);
                    }
                    visited.add(j);
                }
                if (isSumOfSquare(mono1, mono2) || isSumOfSquare(mono2, mono1)) {
                    if (mono1.getNum().compareTo(mono2.getNum()) == 0) {
                        mono1.setSin(new HashMap<>());
                        visited.add(j);
                    }
                }
            }
        }
        if (monos.size() == 0) {
            monos.add(new Mono(BigInteger.ZERO, 0, 0, 0));
        }
        ArrayList<Mono> newMonomials = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            if (!visited.contains(i) && !monos.get(i).getNum().equals(BigInteger.ZERO)) {
                newMonomials.add(monos.get(i));
            }
        }
        return new Poly(newMonomials);
    }

    public boolean isSumOfSquare(Mono mono1, Mono mono2) {
        HashMap<Poly, Integer> sin1 = mono1.getSin();
        HashMap<Poly, Integer> cos1 = mono1.getCos();
        HashMap<Poly, Integer> sin2 = mono2.getSin();
        HashMap<Poly, Integer> cos2 = mono2.getCos();
        if (sin1.size() != 1 || cos2.size() != 1) {
            return false;
        }
        if (mono1.getIndexX() != mono2.getIndexX() ||
                mono1.getIndexY() != mono2.getIndexY() ||
                mono1.getIndexZ() != mono2.getIndexZ()) {
            return false;
        }
        if (!sameMap(cos1, sin2) || !sameMap(sin1, cos2)) {
            return false;
        }
        Poly factor1 = (Poly) sin1.keySet().toArray()[0];
        Poly factor2 = (Poly) cos2.keySet().toArray()[0];
        if (getIndex(sin1, factor1) == 2 && getIndex(cos2, factor2) == 2) {
            return true;
        }
        return false;
    }

    public boolean triFuncHaveSameFactor(Mono mono1, Mono mono2) {
        HashMap<Poly, Integer> sin1 = mono1.getSin();
        HashMap<Poly, Integer> sin2 = mono2.getSin();
        HashMap<Poly, Integer> cos1 = mono1.getCos();
        HashMap<Poly, Integer> cos2 = mono2.getCos();
        return sameMap(sin1, sin2) && sameMap(cos1, cos2);
    }

    public boolean sameMap(HashMap<Poly, Integer> map1, HashMap<Poly, Integer> map2) {
        if (map1.size() != map2.size()) {
            return false;
        }
        for (Map.Entry<Poly, Integer> entry : map1.entrySet()) {
            if (!hasKey(map2, entry.getKey())) {
                return false;
            }
            if (entry.getValue() != getIndex(map2, entry.getKey())) {
                return false;
            }
        }
        return true;
    }

    public boolean haveSameVar(Mono mono1, Mono mono2) {
        if (mono1.getIndexX() != mono2.getIndexX() ||
                mono1.getIndexY() != mono2.getIndexY() ||
                mono1.getIndexZ() != mono2.getIndexZ()) {
            return false;
        }
        if (!triFuncHaveSameFactor(mono1, mono2)) {
            return false;
        }
        return true;
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

    public int getIndex(HashMap<Poly, Integer> map, Poly key) {
        for (Map.Entry<Poly, Integer> entry : map.entrySet()) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return -1;
    }

    public boolean hasKey(HashMap<Poly, Integer> map, Poly key) {
        Poly[] polies = map.keySet().toArray(new Poly[0]);
        for (int i = 0; i < polies.length; i++) {
            if (polies[i].equals(key)) {
                return true;
            }
        }
        return false;
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
}
/*
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
 */