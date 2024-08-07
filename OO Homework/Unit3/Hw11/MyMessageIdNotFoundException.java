import com.oocourse.spec3.exceptions.MessageIdNotFoundException;

import java.util.HashMap;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {
    private int id;
    private static int count = 0;
    private static HashMap<Integer, Integer> excMap = new HashMap<>();

    public MyMessageIdNotFoundException(int id) {
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
        System.out.println("minf-" + count + ", " + id + "-" + excMap.get(id));
    }
}
