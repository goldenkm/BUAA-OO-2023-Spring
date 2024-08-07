import java.util.HashMap;

public class UnionFind {
    private HashMap<Integer, Integer> node;

    public UnionFind() {
        this.node = new HashMap<>();
    }

    public void addNode(int id) {
        if (!node.containsKey(id)) {
            node.put(id, id);
        }
    }

    public void merge(int id1, int id2) {
        int ancestor1 = find(id1);
        int ancestor2 = find(id2);
        if (ancestor1 != ancestor2) {
            node.put(ancestor1, ancestor2);
        }
    }

    public int find(int id) {
        int root = id;
        while (root != node.get(root)) {
            root = node.get(root);
        }
        //路径压缩
        int now = id;
        while (now != root) {
            int father = node.get(now);
            node.put(father, root);
            now = father;
        }
        return root;
    }
}
