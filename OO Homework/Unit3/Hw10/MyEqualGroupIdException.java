import com.oocourse.spec2.exceptions.EqualGroupIdException;

import java.util.HashMap;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private int id;
    private static int count = 0;
    private static HashMap<Integer, Integer> excMap = new HashMap<>();

    public MyEqualGroupIdException(int id) {
        this.id = id;
        this.count++;
        if (excMap.containsKey(id)) {
            excMap.put(id, excMap.get(id) + 1);
        } else {
            excMap.put(id, 1);
        }
    }

    @Override
    public void print() {
        System.out.println("egi-" + count + ", " + id + "-" + excMap.get(id));
    }
}
