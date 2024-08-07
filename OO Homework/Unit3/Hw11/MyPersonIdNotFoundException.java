import com.oocourse.spec3.exceptions.PersonIdNotFoundException;

import java.util.HashMap;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private int id;
    private static int count = 0;
    private static HashMap<Integer, Integer> excMap = new HashMap<>();

    public MyPersonIdNotFoundException(int id) {
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
        System.out.println("pinf-" + count + ", " + id + "-" + excMap.get(id));
    }
}
