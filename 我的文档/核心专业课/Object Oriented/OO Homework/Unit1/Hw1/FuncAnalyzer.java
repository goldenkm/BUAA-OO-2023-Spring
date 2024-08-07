import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class FuncAnalyzer {
    private static HashMap<String, String> funcMap = new HashMap<>();
    private static HashMap<String, ArrayList<String>> paraMap = new HashMap<>();

    public static void addFunc(String input) {
        input = input.replace(" ", "");
        String[] records = input.split("=");
        String funcName = new String();
        if (records[0].indexOf("f") >= 0) {
            funcName = "f";
        } else if (records[0].indexOf("g") >= 0) {
            funcName = "g";
        } else if (records[0].indexOf("h") >= 0) {
            funcName = "h";
        }
        ExprProcessor processor = new ExprProcessor(records[1]);
        String define = processor.ridSpace();
        define = processor.optimizeSign();
        funcMap.put(funcName, define);
        //需要写的：补充paraMap
        String tmp = records[0].substring(2, records[0].length() - 1);
        String[] paras = tmp.split(",");
        ArrayList<String> paraList = new ArrayList<>();
        for (int i = 0; i < paras.length; i++) {
            paraList.add(paras[i]);
        }
        paraMap.put(funcName, paraList);
    }

    public static String callFunc(String funcName, ArrayList<Factor> realParas) {
        String define = funcMap.get(funcName);
        define = parseDefine(define);
        ArrayList<String> fakeParas = paraMap.get(funcName);
        int len = fakeParas.size();
        for (int i = 0; i < len; i++) {
            define = define.replace(fakeParas.get(i), "(" + realParas.get(i).toPoly().toString() + ")");
        }
        return define;
    }

    public static String parseDefine(String define) {
        Lexer lexer = new Lexer(define);     //读取
        Parser parser = new Parser(lexer);  //解析
        Expr expr = parser.parseExpr();     //建立表达式
        return expr.toPoly().toString();
    }
}
