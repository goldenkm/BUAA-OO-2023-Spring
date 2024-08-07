public class Lexer {
    private String string;
    private String lex;
    private int start;

    public Lexer() {
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getLex() {
        return lex;
    }

    public void setLex(String lex) {
        this.lex = lex;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public boolean read() {
        String substring = new String();
        int flag = 0;
        int isInString = 0;
        for (int i = start; i < string.length(); i++) {
            switch (string.charAt(i)) {
                case '{':
                    if (isInString == 0) {
                        flag = 1;
                        lex = "{";
                    } else {
                        substring += '{';
                    }
                    break;
                case ',':
                    if (isInString == 0) {
                        flag = 1;
                        lex = substring;
                    } else {
                        substring += ',';
                    }
                    break;
                case '}':
                    if (isInString == 0) {
                        flag = 1;
                        lex = "}";
                    } else {
                        substring += '}';
                    }
                    break;
                case '[':
                    if (isInString == 0) {
                        flag = 1;
                        lex = "[";
                    } else {
                        substring += '[';
                    }
                    break;
                case ']':
                    if (isInString == 0) {
                        flag = 1;
                        lex = "]";
                    } else {
                        substring += ']';
                    }
                    break;
                /*case ':':
                    if (isInString == 0) {
                        flag = 1;
                        lex = substring;
                    } else {
                        substring += ':';
                    }
                    break;*/
                case '\"':
                    if (isInString == 0) {
                        isInString = 1;
                    } else {
                        isInString = 0;
                    }
                    break;
                default:
                    substring += string.charAt(i);
                    break;
            }
            if (i < string.length() - 1) {
                if ((string.charAt(i + 1) == '[' || string.charAt(i + 1) == '{')
                        && (string.charAt(i) != '[' && string.charAt(i) != '{')) {
                    lex = substring;
                    flag = 1;
                }
                if ((string.charAt(i + 1) == ']' || string.charAt(i + 1) == '}')
                        && (string.charAt(i) != ']' && string.charAt(i) != '}')) {
                    lex = substring;
                    flag = 1;
                }
            }
            if (flag == 1) {
                start = i + 1;
                return true;
            }
        }
        return false;
    }
}
