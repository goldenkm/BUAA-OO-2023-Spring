import com.oocourse.spec2.exceptions.AcquaintanceNotFoundException;

import java.util.HashMap;

public class MyAcquaintanceNotFoundException extends AcquaintanceNotFoundException {
    private int id;
    private static int count = 0;
    private static HashMap<Integer, Integer> excMap = new HashMap<>();

    public MyAcquaintanceNotFoundException(int id) {
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
        System.out.println("anf-" + count + ", " + id + "-" + excMap.get(id));
    }

}
