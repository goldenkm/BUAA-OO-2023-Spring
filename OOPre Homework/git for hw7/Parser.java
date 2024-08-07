import java.util.ArrayList;

public class Parser {
    public Json parseObject(Lexer lexer) {
        Json json = new Json();
        ArrayList<Attribute> attributes = new ArrayList<>();
        if (lexer.getLex().equals("{")) {
            while (lexer.read()) {
                //System.out.println("!+" + lexer.getLex());
                if (lexer.getLex().equals("}")) {
                    break;
                } else {
                    Attribute newAttribute = parseAttribute(lexer);
                    attributes.add(newAttribute);
                    //System.out.println(newAttribute.getKey() + "+" + newAttribute.getValue());
                }
            }
        }
        json.setAttributes(attributes);
        return json;
    }

    private Attribute parseAttribute(Lexer lexer) {
        Attribute attribute = new Attribute();
        attribute.setKey(lexer.getLex());
        //System.out.println("@+" + lexer.getLex());
        if (lexer.getLex().equals("{")) {
            attribute.setValue(parseObject(lexer));
        } else if (lexer.getLex().equals("[")) {
            attribute.setValue(parseArray(lexer));
        } else {
            if (lexer.getLex().indexOf(":") > 0 && !lexer.getLex().endsWith(":")) {
                String[] records = lexer.getLex().split(":", 2);
                //System.out.println(records[0]+" + "+records[1]);
                attribute.setKey(records[0]);
                attribute.setValue(records[1]);
            } else {
                attribute.setKey(lexer.getLex());
                lexer.read();
                if (lexer.getLex().equals("{")) {
                    attribute.setValue(parseObject(lexer));
                } else if (lexer.getLex().equals("[")) {
                    attribute.setValue(parseArray(lexer));
                }
            }
        }
        return attribute;
    }

    private Array parseArray(Lexer lexer) {
        Array array = new Array();
        ArrayList<Object> objects = new ArrayList<>();
        while (lexer.read()) {
            //System.out.println("#+" + lexer.getLex());
            if (lexer.getLex().equals("]")) {
                break;
            } else {
                objects.add(lexer.getLex());
            }
        }
        array.setElements(objects);
        return array;
    }
}
