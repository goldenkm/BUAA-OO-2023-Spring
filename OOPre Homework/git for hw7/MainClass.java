import java.util.ArrayList;
import java.util.Scanner;

public class MainClass {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        String jsonLine = "";
        Lexer lexer = new Lexer();
        lexer.setStart(0);
        lexer.setString(jsonLine);
        while (!(jsonLine = sc.nextLine()).equals("END_OF_MESSAGE")) {
            lexer.setString(jsonLine);
            lexer.read();
            /*while(lexer.read()){
                System.out.println(lexer.getLex());
            }*/
            Parser parser = new Parser();
            Json json = parser.parseObject(lexer);
            searchJson(json);
        }
    }

    public static void searchJson(Object object) {
        if(object instanceof Json){
            for (Attribute attribute : ((Json) object).getAttributes()) {
                if (attribute.getValue() instanceof Json) {
                    System.out.println("!"+attribute.getKey()+"+"+attribute.getValue());
                    searchJson(attribute.getValue());
                } else if (attribute.getValue() instanceof Array) {
                    searchJson(attribute.getValue());
                } else {
                    System.out.println(attribute.getKey()+"+"+attribute.getValue());
                }
            }
        } else if (object instanceof Array){
            for (Object element : ((Array) object).getElements()) {
                if (element.getValue() instanceof Json) {
                    System.out.println("!"+attribute.getKey()+"+"+attribute.getValue());
                    searchJson(attribute.getValue());
                } else if (attribute.getValue() instanceof Array) {
                    searchJson(attribute.getValue());
                } else {
                    System.out.println(attribute.getKey()+"+"+attribute.getValue());
                }
            }
        }

    }

}
