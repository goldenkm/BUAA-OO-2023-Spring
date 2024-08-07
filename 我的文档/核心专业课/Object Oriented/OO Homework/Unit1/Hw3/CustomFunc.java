import java.util.ArrayList;

public class CustomFunc implements Factor {
    private String name;
    private ArrayList<Factor> realParas = new ArrayList<>();

    public CustomFunc() {
    }

    public CustomFunc(String name) {
        this.name = name;
    }

    public void setRealParas(ArrayList<Factor> realParas) {
        this.realParas = realParas;
    }

    @Override
    public Poly toPoly() {
        String func = FuncAnalyzer.callFunc(this.name, this.realParas);
        ExprProcessor processor = new ExprProcessor(func); //修改后添加
        func = processor.optimizeSign();    //修改后添加
        Lexer lexer = new Lexer(func);     //读取
        Parser parser = new Parser(lexer);  //解析
        Expr expr = parser.parseExpr();     //建立表达式
        return expr.toPoly();
    }
}
