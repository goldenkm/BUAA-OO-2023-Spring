import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OkTest {
    public static int okTest(int id1, int id2, int value,
                             HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                             HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        if (hasException(beforeData, id1, id2)) {
            return beforeData.equals(afterData) ? 0 : -1;
        }
        if (beforeData.size() != afterData.size()) {
            return 1;
        }
        // 检查前后people是否相等
        if (!isSameSet(beforeData.keySet(), afterData.keySet())) {
            return 2;
        }
        for (Map.Entry<Integer, HashMap<Integer, Integer>> entry : beforeData.entrySet()) {
            if (entry.getKey() != id1 && entry.getKey() != id2) {
                if (!entry.getValue().equals(afterData.get(entry.getKey()))) {
                    return 3;
                }
            }
        }
        int valueBefore = beforeData.get(id1).get(id2);
        if (valueBefore + value > 0) {
            return test4to12(id1, id2, value, beforeData, afterData);
        } else {
            return test15to21(id1, id2, value, beforeData, afterData);
        }
    }

    public static int test4to12(int id1, int id2, int value,
                                HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                                HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        HashMap<Integer, Integer> acBefore1 = beforeData.get(id1);
        HashMap<Integer, Integer> acBefore2 = beforeData.get(id2);
        HashMap<Integer, Integer> acAfter1 = afterData.get(id1);
        HashMap<Integer, Integer> acAfter2 = afterData.get(id2);
        //检查id1和id2是否仍然认识
        if (!acAfter1.containsKey(id2) || !acAfter2.containsKey(id1)) {
            return 4;
        }
        if (acAfter1.get(id2) != acBefore1.get(id2) + value) {
            return 5;
        }
        if (acAfter2.get(id1) != acBefore2.get(id1) + value) {
            return 6;
        }
        //判断认识的人是不是一样多
        if (acBefore1.size() != acAfter1.size()) {
            return 7;
        }
        if (acBefore2.size() != acAfter2.size()) {
            return 8;
        }
        // 判断认识的人是不是仍然相等
        if (!isSameSet(acBefore1.keySet(), acAfter1.keySet())) {
            return 9;
        }
        if (!isSameSet(acBefore2.keySet(), acAfter2.keySet())) {
            return 10;
        }
        //除了id1，id2，其他的value是否相同
        for (Map.Entry<Integer, Integer> entry : acAfter1.entrySet()) {
            if (entry.getKey() != id2) {
                if (entry.getValue() != acBefore1.get(entry.getKey())) {
                    return 11;
                }
            }
        }
        for (Map.Entry<Integer, Integer> entry : acAfter2.entrySet()) {
            if (entry.getKey() != id1) {
                if (entry.getValue() != acBefore2.get(entry.getKey())) {
                    return 12;
                }
            }
        }
        //13、14看起来永远满足
        return 0;
    }

    public static int test15to21(int id1, int id2, int value,
                                 HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                                 HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        HashMap<Integer, Integer> acBefore1 = beforeData.get(id1);
        HashMap<Integer, Integer> acBefore2 = beforeData.get(id2);
        HashMap<Integer, Integer> acAfter1 = afterData.get(id1);
        HashMap<Integer, Integer> acAfter2 = afterData.get(id2);
        acBefore1.remove(id2);
        acBefore2.remove(id1);
        // 检查id1，id2是否还认识
        if (acAfter1.containsKey(id2) || acAfter2.containsKey(id1)) {
            return 15;
        }
        // 检查acquaintance的size变化
        if (acBefore1.size() != acAfter1.size()) {
            return 16;
        }
        if (acBefore2.size() != acAfter2.size()) {
            return 17;
        }
        //检查after的
        if (!acBefore1.keySet().containsAll(acAfter1.keySet())
                || !acBefore1.values().containsAll(acAfter1.values())) {
            return 20;
        }
        if (!acBefore2.keySet().containsAll(acAfter2.keySet())
                || !acBefore2.values().containsAll(acAfter2.values())) {
            return 21;
        }
        return 0;
    }

    public static boolean isSameSet(Set<Integer> set1, Set<Integer> set2) {
        return set1.containsAll(set2) && set2.containsAll(set1);
    }

    public static boolean hasException(HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                                       int id1, int id2) {
        if (!beforeData.containsKey(id1) || !beforeData.containsKey(id2)
                || id1 == id2 || !beforeData.get(id1).containsKey(id2)
                || !beforeData.get(id2).containsKey(id1)) {
            return true;
        }
        return false;
    }
}
