import java.math.BigInteger;
import java.util.ArrayList;

public class Parser {
    private Lexer lexer;

    public Parser() {
    }

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();
        int flag = 1;
        //System.out.println("!" + lexer.now());
        if (lexer.now().equals("-")) {
            flag = -1;
            lexer.next();
        }
        expr.addTerm(parseTerm(flag));
        while (lexer.now().equals("+") || lexer.now().equals("-")) {
            flag = lexer.now().equals("-") ? -1 : 1;
            lexer.next();
            expr.addTerm(parseTerm(flag));
        }
        return expr;
    }

    public Term parseTerm(int flag) {
        Term term = new Term();
        term.setSign(flag);
        term.addFactor(parseFactor());
        while (lexer.now().equals("*")) {
            lexer.next();
            term.addFactor(parseFactor());
        }
        return term;
    }

    public Factor parseFactor() {
        //System.out.println(lexer.now()+"!");
        int sign = 1;
        if (lexer.now().equals("-") || lexer.now().equals("+")) {  //解析因子前面的符号
            if (lexer.now().equals("-")) {
                sign = -1;
            }
            lexer.next();
        }
        if (lexer.now().equals("(")) {     //表达式因子
            lexer.next();
            Factor expr = parseExpr();
            lexer.next();
            if (lexer.now().equals("**")) {
                ((Expr) expr).setIndex(readIndex());
                lexer.next();
            }
            return expr;
        } else if (isVar(lexer.now())) {   //变量因子 or 幂函数因子
            PowFunc powFunc = new PowFunc();
            powFunc.setVar(lexer.now());
            lexer.next();
            if (lexer.now().equals("**")) {
                powFunc.setIndex(readIndex());
                lexer.next();
            }
            return powFunc;
        } else if (isNumber(lexer.now())) {    //常数因子
            BigInteger num = new BigInteger(lexer.now());
            lexer.next();
            if (sign == -1) {
                num = num.multiply(BigInteger.valueOf(-1));
            }
            return new Number(num);
        } else if (lexer.now().equals("sin(")) {
            //System.out.println(lexer.now()+"!");
            lexer.next();
            TriFunc sin = new TriFunc(1, parseFactor());
            lexer.next();
            if (lexer.now().equals("**")) {
                sin.setIndex(readIndex());
                lexer.next();
            }
            return sin;
        } else if (lexer.now().equals("cos(")) {        //cos
            //System.out.println(lexer.now()+"!");
            lexer.next();
            TriFunc cos = new TriFunc(-1, parseFactor());
            lexer.next();   //read )
            if (lexer.now().equals("**")) {
                cos.setIndex(readIndex());
                lexer.next();
            }
            return cos;
        } else {        //custom function
            return parseCustomFunc();
        }
    }

    public CustomFunc parseCustomFunc() {
        final String funcName = lexer.now();
        lexer.next();       // read f/g/h
        lexer.next();       // read (
        ArrayList<Factor> realParas = new ArrayList<>();
        Factor factor = parseFactor();
        realParas.add(factor);
        CustomFunc customFunc = new CustomFunc(funcName);
        if (lexer.now().equals(",")) {
            lexer.next();
            realParas.add(parseFactor());
            if (lexer.now().equals(",")) {
                lexer.next();
                realParas.add(parseFactor());
            }
        }
        customFunc.setRealParas(realParas);
        lexer.next();       //read )
        return customFunc;
    }

    public int readIndex() {
        lexer.next();   //read **
        while (lexer.now().equals("+")) {       //防止指数带+号
            lexer.next();
        }
        int index = Integer.valueOf(lexer.now());
        return index;
    }

    public boolean isVar(String s) {
        return s.equals("x") || s.equals("y") || s.equals("z");
    }

    public boolean isNumber(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
