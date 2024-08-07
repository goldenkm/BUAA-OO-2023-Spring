import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Mono {
    private int sign = 1;
    private BigInteger num;

    private int indexX;

    private int indexY;

    private int indexZ;

    private HashMap<Poly, Integer> sin = new HashMap<>();

    private HashMap<Poly, Integer> cos = new HashMap<>();

    public Mono() {
    }

    public int getSign() {
        return sign;
    }

    public BigInteger getNum() {
        return num;
    }

    public int getIndexX() {
        return indexX;
    }

    public int getIndexY() {
        return indexY;
    }

    public int getIndexZ() {
        return indexZ;
    }

    public HashMap<Poly, Integer> getSin() {
        return sin;
    }

    public HashMap<Poly, Integer> getCos() {
        return cos;
    }

    public void reset() {
        this.indexX = 0;
        this.indexY = 0;
        this.indexZ = 0;
        this.sin = new HashMap<>();
        this.cos = new HashMap<>();
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public void setNum(BigInteger num) {
        this.num = num;
    }

    public void setIndexX(int indexX) {
        this.indexX = indexX;
    }

    public void setIndexY(int indexY) {
        this.indexY = indexY;
    }

    public void setIndexZ(int indexZ) {
        this.indexZ = indexZ;
    }

    public void setSin(HashMap<Poly, Integer> sin) {
        this.sin = sin;
    }

    public void setCos(HashMap<Poly, Integer> cos) {
        this.cos = cos;
    }

    public Mono(BigInteger num, int indexX, int indexY, int indexZ) {
        this.num = num;
        this.indexX = indexX;
        this.indexY = indexY;
        this.indexZ = indexZ;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.num.compareTo(BigInteger.ZERO) == 0) {
            return String.valueOf(0);
        }
        if (indexX == 0 && indexY == 0 && indexZ == 0 && sin.isEmpty() && cos.isEmpty()) {    //常数
            return num.toString();
        }
        if (num.compareTo(BigInteger.valueOf(-1)) == 0) {
            sb.append("-");
        }
        if (num.compareTo(BigInteger.ONE) != 0 && num.compareTo(BigInteger.valueOf(-1)) != 0) {
            sb.append(num + "*");
        }
        if (indexX != 0) {
            sb.append("x");
            if (indexX == 2) {
                sb.append("*x");
            } else if (indexX != 1) {
                sb.append("**" + indexX);
            }
        }
        if (indexY != 0) {
            if (indexX != 0) {
                sb.append("*");
            }
            sb.append("y");
            if (indexY == 2) {
                sb.append("*y");
            } else if (indexY != 1) {
                sb.append("**" + indexY);
            }
        }
        if (indexZ != 0) {
            if (indexX != 0 || indexY != 0) {
                sb.append("*");
            }
            sb.append("z");
            if (indexZ == 2) {
                sb.append("*z");
            } else if (indexZ != 1) {
                sb.append("**" + indexZ);
            }
        }
        if (sin.size() > 0) {
            sb = sinToString(sb);
        }
        if (cos.size() > 0) {
            sb = cosToString(sb);
        }
        return sb.toString();
    }

    public StringBuilder sinToString(StringBuilder sb) {
        if (indexX != 0 || indexY != 0 || indexZ != 0) {
            sb.append("*");
        }
        int count = 0;
        for (Map.Entry<Poly, Integer> entry : sin.entrySet()) {
            if (count > 0) {
                sb.append("*");
            }
            count++;
            sb.append("sin(");
            if (entry.getKey().toString().equals("x") ||
                    entry.getKey().toString().equals("y") ||
                    entry.getKey().toString().equals("z") ||
                    isNumber(entry.getKey().toString())) {
                sb.append(entry.getKey().toString());
            } else {
                sb.append("(" + entry.getKey().toString() + ")");
            }
            sb.append(")");
            if (entry.getValue() > 1) {
                sb.append("**" + entry.getValue());
            }
        }
        return sb;
    }

    public StringBuilder cosToString(StringBuilder sb) {
        if (indexX != 0 || indexY != 0 || indexZ != 0 || sin.size() > 0) {
            sb.append("*");
        }
        int count = 0;
        for (Map.Entry<Poly, Integer> entry : cos.entrySet()) {
            if (count > 0) {
                sb.append("*");
            }
            count++;
            sb.append("cos(");
            if (entry.getKey().toString().equals("x") ||
                    entry.getKey().toString().equals("y") ||
                    entry.getKey().toString().equals("z") ||
                    isNumber(entry.getKey().toString())) {
                sb.append(entry.getKey().toString());
            } else {
                sb.append("(" + entry.getKey().toString() + ")");
            }
            sb.append(")");
            if (entry.getValue() > 1) {
                sb.append("**" + entry.getValue());
            }
        }
        return sb;
    }

    public boolean isNumber(String str) {
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (!(Character.isDigit(str.charAt(i)) || str.charAt(i) == '-')) {
                return false;
            }
        }
        return true;
    }
}
