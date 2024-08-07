public class ExprProcessor {
    private String str;

    public ExprProcessor(String str) {
        this.str = str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public String ridSpace() {
        StringBuilder sb = new StringBuilder();
        if (str.charAt(0) == '-') {
            sb.append(0);
        }
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c != ' ' && c != '\t') {
                sb.append(c);
            }
        }
        this.str = sb.toString();
        return sb.toString();
    }

    public String optimizeSign() {
        StringBuilder sb = new StringBuilder(str);
        for (int i = 0; i < sb.length(); i++) {
            //System.out.println("!" + sb.charAt(i));
            if (sb.charAt(i) == '+' || sb.charAt(i) == '-') {
                int flag = sb.charAt(i) == '+' ? 1 : -1;
                int j = 0;
                for (j = i + 1; sb.charAt(j) == '+' || sb.charAt(j) == '-'; j++) {
                    //System.out.println("?!"+sb.charAt(j));
                    if (sb.charAt(j) == '-') {
                        flag = (-1) * flag;
                    }
                }
                if (j > i + 1) {    //有连续正负号
                    if (flag == 1) {
                        sb.replace(i, j, "+");
                    } else {
                        sb.replace(i, j, "-");
                    }
                    j--;
                    i = j - 1;
                }
            }
        }
        this.str = sb.toString();
        return sb.toString();
    }
}
