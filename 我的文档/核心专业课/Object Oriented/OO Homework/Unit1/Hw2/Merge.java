import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Merge {
    public static Poly merge(Poly poly) {
        ArrayList<Mono> monos = poly.getMonomials();
        HashSet<Integer> visited = new HashSet<>();
        int len = monos.size();
        for (int i = 0; i < len; i++) {     //合并同类项
            if (visited.contains(i)) {
                continue;
            }
            for (int j = 0; j < len; j++) {
                if (visited.contains(j) || i == j) {
                    continue;
                }
                Mono mono1 = monos.get(i);
                Mono mono2 = monos.get(j);
                //System.out.println(mono1.toString()+" and "+mono2.toString());
                if (haveSameVar(mono1, mono2)) {
                    BigInteger newNum = mono1.getNum().add(mono2.getNum());
                    if (newNum.compareTo(BigInteger.ZERO) != 0) {
                        mono1.setNum(newNum);
                    } else {
                        mono1.setNum(newNum);
                        visited.add(i);
                    }
                    visited.add(j);
                }
                if (isSumOfSquare(mono1, mono2) || isSumOfSquare(mono2, mono1)) {
                    //处理平方和
                    if (mono1.getNum().compareTo(mono2.getNum()) == 0) {
                        visited.add(j);
                    } else {
                        if (mono1.getNum().compareTo(mono2.getNum()) > 0) {
                            mono1.setNum(mono1.getNum().subtract(mono2.getNum()));
                            mono2.reset();
                        } else {
                            mono2.setNum(mono2.getNum().subtract(mono1.getNum()));
                            mono1.reset();
                        }
                    }
                    j = -1;
                }
                if (sinHaveInverseFactor(mono1, mono2)) {
                    //System.out.println(mono1.toString()+" and "+mono2.toString());
                    mono1.setNum(mono1.getNum().subtract(mono2.getNum()).abs());
                    mono2.setSin(new HashMap<>());
                    mono2.setCos(new HashMap<>());
                    visited.add(j);
                    j = -1;
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

    public static boolean sinHaveInverseFactor(Mono mono1, Mono mono2) {
        if (mono1.getSin().size() == 0 || mono2.getSin().size() == 0) {
            return false;
        }
        if (!sameMap(mono1.getCos(), mono2.getCos())) {
            return false;
        }
        HashMap<Poly, Integer> sin1 = mono1.getSin();
        HashMap<Poly, Integer> tmp = new HashMap<>();
        for (Map.Entry<Poly, Integer> entry : sin1.entrySet()) {
            ArrayList<Mono> monos = new ArrayList<>();
            Mono mono = new Mono(BigInteger.valueOf(-1), 0, 0, 0);
            monos.add(mono);
            Poly negative = new Poly(monos);
            Poly inversePoly = entry.getKey().mulPoly(negative);
            tmp.put(inversePoly, entry.getValue());
        }
        HashMap<Poly, Integer> sin2 = mono2.getSin();
        return sameMap(tmp, sin2);
    }

    public static boolean triFuncHaveSameFactor(Mono mono1, Mono mono2) {
        HashMap<Poly, Integer> sin1 = mono1.getSin();
        HashMap<Poly, Integer> cos1 = mono1.getCos();
        HashMap<Poly, Integer> sin2 = mono2.getSin();
        HashMap<Poly, Integer> cos2 = mono2.getCos();
        return sameSet(sin1, sin2) && sameSet(cos1, cos2);
    }

    public static boolean sameSet(HashMap<Poly, Integer> map1, HashMap<Poly, Integer> map2) {
        if (map1.size() != map2.size()) {
            return false;
        }
        Poly[] polies1 = map1.keySet().toArray(new Poly[0]);
        for (int i = 0; i < polies1.length; i++) {
            if (!hasKey(map2, polies1[i])) {
                return false;
            }
        }
        Poly[] polies2 = map2.keySet().toArray(new Poly[0]);
        for (int i = 0; i < polies2.length; i++) {
            if (!hasKey(map1, polies1[i])) {
                return false;
            }
        }
        return true;
    }

    //x*sin((-x+y))*cos(x)+x*sin((-y+x))*cos(x)*cos(z)**2+x*sin((-y+x))*cos(x)*sin(z)**2
    public static boolean isSumOfSquare(Mono mono1, Mono mono2) {
        HashMap<Poly, Integer> sin1 = mono1.getSin();
        HashMap<Poly, Integer> cos2 = mono2.getCos();
        if (sin1.size() == 0 || cos2.size() == 0) {
            return false;
        }
        if (mono1.getIndexX() != mono2.getIndexX() ||
                mono1.getIndexY() != mono2.getIndexY() ||
                mono1.getIndexZ() != mono2.getIndexZ()) {
            return false;
        }
        if ((mono1.getNum().compareTo(BigInteger.ZERO) < 0
                && mono2.getNum().compareTo(BigInteger.ZERO) > 0)
                || (mono1.getNum().compareTo(BigInteger.ZERO) > 0
                && mono2.getNum().compareTo(BigInteger.ZERO) < 0)) {
            return false;
        }
        Poly[] polies1 = sin1.keySet().toArray(new Poly[0]);
        Poly[] polies2 = cos2.keySet().toArray(new Poly[0]);
        int len1 = polies1.length;
        int len2 = polies2.length;
        int flag = 0;
        Poly target1 = new Poly();
        Poly target2 = new Poly();
        for (int i = 0; i < len1; i++) {
            for (int j = 0; j < len2; j++) {
                if (polies1[i].equals(polies2[j])) {
                    if (getIndex(sin1, polies1[i]) == 2 &&
                            getIndex(cos2, polies2[j]) == 2) {
                        flag = 1;
                        target1 = polies1[i];
                        target2 = polies2[j];
                    }
                }
            }
        }
        if (flag == 0) {
            return false;
        }
        sin1.remove(target1, 2);
        cos2.remove(target2, 2);
        HashMap<Poly, Integer> cos1 = mono1.getCos();
        HashMap<Poly, Integer> sin2 = mono2.getSin();
        if (sameMap(sin1, sin2) && sameMap(cos1, cos2)) {
            if (mono1.getNum().compareTo(mono2.getNum()) > 0) {
                sin1.put(target1, 2);
            } else if (mono1.getNum().compareTo(mono2.getNum()) < 0) {
                cos2.put(target2, 2);
            }
            return true;
        } else {
            sin1.put(target1, 2);
            cos2.put(target2, 2);
            return false;
        }
    }

    public static boolean haveSameTriFunc(Mono mono1, Mono mono2) {
        HashMap<Poly, Integer> sin1 = mono1.getSin();
        HashMap<Poly, Integer> sin2 = mono2.getSin();
        HashMap<Poly, Integer> cos1 = mono1.getCos();
        HashMap<Poly, Integer> cos2 = mono2.getCos();
        return sameMap(sin1, sin2) && sameMap(cos1, cos2);
    }

    public static boolean sameMap(HashMap<Poly, Integer> map1, HashMap<Poly, Integer> map2) {
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

    public static boolean haveSameVar(Mono mono1, Mono mono2) {
        if (mono1.getIndexX() != mono2.getIndexX() ||
                mono1.getIndexY() != mono2.getIndexY() ||
                mono1.getIndexZ() != mono2.getIndexZ()) {
            return false;
        }
        if (!haveSameTriFunc(mono1, mono2)) {
            return false;
        }
        return true;
    }

    public static int getIndex(HashMap<Poly, Integer> map, Poly key) {
        for (Map.Entry<Poly, Integer> entry : map.entrySet()) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return -1;
    }

    public static boolean hasKey(HashMap<Poly, Integer> map, Poly key) {
        Poly[] polies = map.keySet().toArray(new Poly[0]);
        for (int i = 0; i < polies.length; i++) {
            if (polies[i].equals(key)) {
                return true;
            }
        }
        return false;
    }

}
