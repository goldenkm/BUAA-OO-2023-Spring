import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OkTest {
    public static int okTest(int limit,
                             ArrayList<HashMap<Integer, Integer>> beforeData,
                             ArrayList<HashMap<Integer, Integer>> afterData, int result) {
        HashMap<Integer, Integer> beforeEmojis = beforeData.get(0);
        HashMap<Integer, Integer> afterEmojis = afterData.get(0);
        int size = 0;
        for (Map.Entry<Integer, Integer> entry : beforeEmojis.entrySet()) {
            if (entry.getValue() >= limit) {
                size++;
                //每一个after里面的id都有原来的与之对应
                if (!afterEmojis.containsKey(entry.getKey())) {
                    return 1;
                }
            }
        }
        for (Map.Entry<Integer, Integer> entry : afterEmojis.entrySet()) {
            //对于每个after的heat都有before的heat与之相等
            if (entry.getValue() != beforeEmojis.get(entry.getKey())) {
                return 2;
            }
        }
        //检查after的size
        if (size != afterEmojis.size()) {
            return 3;
        }
        HashMap<Integer, Integer> beforeMessage = beforeData.get(1);
        HashMap<Integer, Integer> afterMessage = afterData.get(1);
        for (Map.Entry<Integer, Integer> entry : beforeMessage.entrySet()) {
            if (entry.getValue() != null) {
                if (entry.getValue() >= limit) {
                    if (!afterMessage.containsKey(entry.getKey())) {
                        return 5;
                    } else if (afterMessage.get(entry.getKey()) != entry.getValue()) {
                        return 5;
                    }
                }
            } else {
                if (!afterMessage.containsKey(entry.getKey())
                        || afterMessage.get(entry.getKey()) != null) {
                    return 6;
                }
            }
        }
        //检查messages的数量
        if (beforeMessage.size() - (beforeEmojis.size() - size) != afterMessage.size()) {
            return 7;
        }
        if (size != result) {
            return 8;
        }
        return 0;
    }
}
