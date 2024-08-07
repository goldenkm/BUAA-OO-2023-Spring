import com.oocourse.spec3.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class Graph {
    public static boolean bfs(int id1, int id2, ArrayList<MyPerson> people) {
        //bfs
        if (id1 == id2) {
            return true;
        }
        MyPerson person1 = (MyPerson) getPerson(id1, people);
        ArrayList<Integer> visited = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.addAll(person1.getAcquaintance().keySet());
        // 标记起点
        visited.add(id1);
        while (!queue.isEmpty()) {
            int id = queue.poll();
            if (!visited.contains(id)) {
                if (id == id2) {
                    return true;
                }
                visited.add(id);
                MyPerson tmp = (MyPerson) getPerson(id, people);
                queue.addAll(tmp.getAcquaintance().keySet());
            }
        }
        return false;
    }

    public static Person getPerson(int id, ArrayList<MyPerson> people) {
        for (MyPerson person : people) {
            if (person.getId() == id) {
                return person;
            }
        }
        return null;
    }

    public static HashMap<Integer, Node> dijkstra(int start, ArrayList<MyPerson> people) {
        HashMap<Integer, Node> dis = new HashMap<>();
        HashMap<Integer, Boolean> visited = new HashMap<>();
        // Pair: id + value
        PriorityQueue<MyPair> queue = new PriorityQueue<>();
        // 初始化dis
        for (MyPerson person : people) {
            dis.put(person.getId(), new Node(person.getId()));
            visited.put(person.getId(), false);
        }
        dis.get(start).setMinValue(0);
        MyPerson person = (MyPerson) getPerson(start, people);
        for (Map.Entry<Integer, Person> entry : person.getAcquaintance().entrySet()) {
            int id = entry.getKey();
            dis.get(id).setOrigin1(id);
            int value = entry.getValue().queryValue(person);
            dis.get(id).setMinValue(value);
            queue.add(new MyPair(id, value));
        }
        visited.put(start, true);
        while (!queue.isEmpty()) {
            MyPair cur = queue.poll();
            int curId = cur.getId();
            if (visited.get(curId)) {
                continue;
            }
            person = (MyPerson) getPerson(curId, people);
            visited.put(curId, true);
            for (Map.Entry<Integer, Person> entry : person.getAcquaintance().entrySet()) {
                int weight = entry.getValue().queryValue(person);
                int friendId = entry.getKey();
                Node node1 = dis.get(curId);
                Node node2 = dis.get(friendId);
                if (!visited.get(friendId)) {
                    update(node1, node2, weight, node1.getMinValue(), queue);
                    update(node1, node2, weight, node1.getMinValue2(), queue);
                }
            }
        }
        return dis;
    }

    public static void update(Node node1, Node node2, int weight,
                              int dis, PriorityQueue<MyPair> heap) {
        if (dis + weight < node2.getMinValue() && dis != Integer.MAX_VALUE) {
            //当前v的最短路可被更新
            if (node1.getOrigin1() != node2.getOrigin1()) {
                node2.setOrigin2(node2.getOrigin1());
                node2.setMinValue2(node2.getMinValue());
            }
            node2.setOrigin1(node1.getOrigin1());
            node2.setMinValue(dis + weight);
            heap.add(new MyPair(node2.getTo(), dis + weight));
        } else if (dis + weight < node2.getMinValue2() && dis != Integer.MAX_VALUE) {
            //当前v的次短路可被更新
            if (node1.getOrigin1() != node2.getOrigin1()) {
                node2.setOrigin2(node1.getOrigin1());
                node2.setMinValue2(dis + weight);
            }
        }
    }

}