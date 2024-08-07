import com.oocourse.spec3.exceptions.RelationNotFoundException;

import java.util.HashMap;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private int id1;
    private int id2;
    private static int count = 0;
    private static HashMap<Integer, Integer> excMap = new HashMap<>();

    public MyRelationNotFoundException(int id1, int id2) {
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id1, id2);
        this.count++;
        addException(this.id1);
        addException(this.id2);
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
        System.out.println("rnf-" + count + ", "
                + id1 + "-" + excMap.get(id1) + ", " + id2 + "-" + excMap.get(id2));
    }
}
