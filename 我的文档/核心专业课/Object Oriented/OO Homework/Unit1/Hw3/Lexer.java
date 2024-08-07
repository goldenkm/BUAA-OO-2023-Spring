public class Lexer {
    private String input;
    private int pos = 0;
    private String curToken;
    private static String tokens = "xyzfghd()+-\\*/,";
    //private static final String patternUnit = "[0-9]* *[xyz] *(\\*\\*)* *[+-]*[0-9]*";

    public Lexer() {
    }

    public Lexer(String input) {
        this.input = input;
        this.next();
    }

    private String getNum() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            pos += 1;
        }
        return sb.toString();
    }

    public void next() {    //读取下一个符号
        if (pos == input.length()) {
            return;
        }
        char c = input.charAt(pos);
        if (Character.isDigit(c)) {
            curToken = getNum();
        } else if (tokens.indexOf(c) >= 0) {
            if (c == '*' && input.charAt(pos + 1) == '*') {
                curToken = "**";
                pos += 2;
            } else {
                curToken = String.valueOf(c);
                pos += 1;
            }
        } else if (c == 's' && input.charAt(pos + 1) == 'i'
                && input.charAt(pos + 2) == 'n' && input.charAt(pos + 3) == '(') {
            pos += 4;
            curToken = "sin(";
        } else if (c == 'c' && input.charAt(pos + 1) == 'o'
                && input.charAt(pos + 2) == 's' && input.charAt(pos + 3) == '(') {
            pos += 4;
            curToken = "cos(";
        }
        //System.out.println(curToken+"!");
    }

    public String now() {   //返回当前符号
        return this.curToken;
    }
}

