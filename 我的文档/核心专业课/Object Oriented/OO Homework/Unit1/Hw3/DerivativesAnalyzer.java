import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class DerivativesAnalyzer {
    public static Poly derivation(Mono mono, int flag) {
        Poly ans = new Poly();
        ans = ans.addPoly(powFuncDerivation(mono, flag));
        if (mono.getSin().size() > 0) {
            HashMap<Poly, Integer> sin = mono.getSin();
            for (Map.Entry<Poly, Integer> entry : sin.entrySet()) {
                Poly newPoly = sinDerivation(mono, entry, flag);
                ans = ans.addPoly(newPoly);
            }
        }
        if (mono.getCos().size() > 0) {
            HashMap<Poly, Integer> cos = mono.getCos();
            for (Map.Entry<Poly, Integer> entry : cos.entrySet()) {
                Poly newPoly = cosDerivation(mono, entry, flag);
                ans = ans.addPoly(newPoly);
            }
        }
        return ans;
    }

    public static Poly powFuncDerivation(Mono mono, int flag) {
        Mono zero = new Mono();
        zero.setNum(BigInteger.ZERO);
        Mono clonedMono = mono.clone();
        switch (flag) {
            case 1:
                if (clonedMono.getIndexX() == 0) {
                    return zero.toPoly();
                }
                clonedMono.setNum(clonedMono.getNum().
                        multiply(BigInteger.valueOf(clonedMono.getIndexX())));
                clonedMono.setIndexX(clonedMono.getIndexX() - 1);
                break;
            case 2:
                if (clonedMono.getIndexY() == 0) {
                    return zero.toPoly();
                }
                clonedMono.setNum(clonedMono.getNum().
                        multiply(BigInteger.valueOf(clonedMono.getIndexY())));
                clonedMono.setIndexY(clonedMono.getIndexY() - 1);
                break;
            default:
                if (clonedMono.getIndexZ() == 0) {
                    return zero.toPoly();
                }
                clonedMono.setNum(clonedMono.getNum().
                        multiply(BigInteger.valueOf(clonedMono.getIndexZ())));
                clonedMono.setIndexZ(clonedMono.getIndexZ() - 1);
                break;
        }
        return clonedMono.toPoly();
    }

    public static Poly sinDerivation(Mono mono, Map.Entry<Poly, Integer> target, int flag) {
        Mono newMono = new Mono(mono.getNum(),
                mono.getIndexX(),
                mono.getIndexY(),
                mono.getIndexZ());
        HashMap<Poly, Integer> newSin = new HashMap<>();
        HashMap<Poly, Integer> newCos = new HashMap<>();
        newCos.putAll(mono.getCos());
        for (Map.Entry<Poly, Integer> entry : mono.getSin().entrySet()) {
            if (entry.getKey().equals(target.getKey())
                    && entry.getValue() == target.getValue()) {
                continue;
            }
            newSin.put(entry.getKey(), entry.getValue());
        }
        newMono.setSin(newSin);
        newMono.setCos(newCos);
        // 以上构建出了除了需要求导的那项以外的Mono，下面对该项求导
        Mono sinDer = new Mono();
        sinDer.setNum(BigInteger.valueOf(target.getValue()));
        if (target.getValue() > 1) {
            HashMap<Poly, Integer> sin = new HashMap<>();
            sin.put(target.getKey(), target.getValue() - 1);
            sinDer.setSin(sin);
        }
        HashMap<Poly, Integer> cos = new HashMap<>();
        cos.put(target.getKey(), 1);
        sinDer.setCos(cos);
        // 得到了sin的直接导数cos
        Poly factorInPoly = new Poly();
        for (Mono factorInMono : target.getKey().getMonomials()) {  //需要深拷贝
            Mono clonedMono = factorInMono.clone();
            factorInPoly = factorInPoly.addPoly(derivation(clonedMono, flag));
        }
        Poly ans = sinDer.toPoly().mulPoly(factorInPoly);
        // 得到了sin的导数cos 与因子求导的乘积
        ans = ans.mulPoly(newMono.toPoly());
        return ans;
    }

    public static Poly cosDerivation(Mono mono, Map.Entry<Poly, Integer> target, int flag) {
        Mono newMono = new Mono(mono.getNum(),
                mono.getIndexX(),
                mono.getIndexY(),
                mono.getIndexZ());
        HashMap<Poly, Integer> newSin = new HashMap<>();
        newSin.putAll(mono.getSin());
        HashMap<Poly, Integer> newCos = new HashMap<>();
        for (Map.Entry<Poly, Integer> entry : mono.getCos().entrySet()) {
            if (entry.getKey().equals(target.getKey())
                    && entry.getValue() == target.getValue()) {
                continue;
            }
            newCos.put(entry.getKey(), entry.getValue());
        }
        newMono.setSin(newSin);
        newMono.setCos(newCos);
        // 以上构建出了除了需要求导的那项以外的Mono，下面对该项求导
        Mono cosDer = new Mono();
        cosDer.setNum(BigInteger.valueOf(target.getValue()).multiply(BigInteger.valueOf(-1)));
        if (target.getValue() > 1) {
            HashMap<Poly, Integer> cos = new HashMap<>();
            cos.put(target.getKey(), target.getValue() - 1);
            cosDer.setCos(cos);
        }
        HashMap<Poly, Integer> sin = new HashMap<>();
        sin.put(target.getKey(), 1);
        cosDer.setSin(sin);
        // 得到了cos的直接导数sin
        Poly factorInPoly = new Poly();
        for (Mono factorInMono : target.getKey().getMonomials()) {
            Mono clonedMono = factorInMono.clone();
            factorInPoly = factorInPoly.addPoly(derivation(clonedMono, flag));
        }
        Poly ans = cosDer.toPoly().mulPoly(factorInPoly);
        // 得到了sin的导数cos 与因子求导的乘积
        ans = ans.mulPoly(newMono.toPoly());
        return ans;
    }

    public static boolean containsPoly(HashMap<Poly, Integer> map, Poly key) {
        for (Map.Entry<Poly, Integer> entry : map.entrySet()) {
            if (entry.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }
}
//-dz(--z**+2*cos(z)-(++3*z**2*0*0)*2)*4