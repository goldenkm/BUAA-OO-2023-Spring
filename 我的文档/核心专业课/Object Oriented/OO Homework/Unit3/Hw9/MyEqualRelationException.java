import com.oocourse.spec1.exceptions.EqualRelationException;
import java.util.HashMap;

public class MyEqualRelationException extends EqualRelationException {
    private int id1;
    private int id2;
    private static int count = 0;
    private static HashMap<Integer, Integer> excMap = new HashMap<>();

    public MyEqualRelationException(int id1, int id2) {
        if (id1 != id2) {
            this.id1 = Math.min(id1, id2);
            this.id2 = Math.max(id1, id2);
            this.count++;
            addException(this.id1);
            addException(this.id2);
        } else {
            this.id1 = this.id2 = id1;
            this.count++;
            addException(this.id1);
        }
    }

    public void addException(int id) {
        if (excMap.containsKey(id)) {
            excMap.put(id, excMap.get(id) + 1);
        } else {
            excMap.put(id, 1);
        }
    }

    @Override
    public void print() {
        // 不确定相等时怎么输出
        System.out.println("er-" + count + ", " +
                id1 + "-" + excMap.get(id1) + ", " + id2 + "-" + excMap.get(id2));

    }
}
