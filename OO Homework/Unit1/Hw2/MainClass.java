import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        while (n > 0) {
            String func = scanner.nextLine();
            FuncAnalyzer.addFunc(func);
            n--;
        }
        String inputTmp = scanner.nextLine();
        ExprProcessor processor = new ExprProcessor(inputTmp);  // 建立表达式处理器
        String str = processor.ridSpace();  //去除空格和tab
        str = processor.optimizeSign();      //优化符号
        String input = str;
        Lexer lexer = new Lexer(input);     //读取
        Parser parser = new Parser(lexer);  //解析
        Expr expr = parser.parseExpr();     //建立表达式
        StringBuilder sb2 = new StringBuilder(expr.toPoly().toString());
        if (sb2.charAt(0) == '0' && sb2.length() > 1) {
            sb2.deleteCharAt(0);
            if (sb2.charAt(0) == '+') {
                sb2.deleteCharAt(0);
            }
        }
        processor.setStr(sb2.toString());
        str = processor.optimizeSign();
        System.out.println(str);
    }
}
