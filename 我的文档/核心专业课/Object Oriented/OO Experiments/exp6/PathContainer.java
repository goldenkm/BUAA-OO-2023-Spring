import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PathContainer {
    //@ public instance model non_null Path[] pList;
    //@ public instance model non_null int[] pidList;
    private HashMap<Path, Integer> pathList;        // 描述id到path之间的映射关系
    private HashMap<Integer, Path> pathIdList;      // 描述path到id之间的映射关系，两个map加在一起对应上面两个数组

    private int idCounter;

    //@ public instance model non_null int[] nodes;
    //@ public instance model non_null int[] nodeToCount;
    private HashMap<Integer, Integer> globalNodesCount;     // 用一个HashMap实现规格中的nodes, nodeToCount两个数组

    public PathContainer() {
        pathList = new HashMap<>();
        pathIdList = new HashMap<>();
        globalNodesCount = new HashMap<>();
        idCounter = 0;
    }

    //@ ensures \result == pList.length;
    public /*@ pure @*/ int size() {
        return pathList.size();
    }

    /*@ requires path != null;
      @ assignable \nothing;
      @ ensures \result == (\exists int i; 0 <= i && i < pList.length;
      @                     pList[i].equals(path));
      @*/
    public /*@ pure @*/ boolean containsPath(Path path) {
        if (path == null) {
            System.err.println("path in containsPath(path) is null !");
            return false;
        }
        return (pathList.get(path) != null);
    }

    /*@ ensures \result == (\exists int i; 0 <= i && i < pidList.length;
      @                      pidList[i] == pathId);
      @*/
    public /*@ pure @*/ boolean containsPathId(int pathId) {
        return (pathIdList.get(pathId) != null);
    }

    /*@ public normal_behavior
      @ requires containsPathId(pathId);
      @ assignable \nothing;
      @ ensures (\exists int i; 0 <= i && i < pList.length; pidList[i] == pathId && \result == pList[i]);
      @ also
      @ public exceptional_behavior
      @ requires !containsPathId(pathId);
      @ assignable \nothing;
      @ signals_only PathIdNotFoundException;
      @*/
    public /*@ pure @*/ Path getPathById(int pathId) throws PathIdNotFoundException {
        if (containsPathId(pathId)) {
            return pathIdList.get(pathId);
        }
        throw new PathIdNotFoundException(pathId);
    }

    /*@ public normal_behavior
      @ requires path != null && path.isValid() && containsPath(path);
      @ assignable \nothing;
      @ ensures (\exists int i; 0 <= i && i < pList.length; pList[i].equals(path) && pidList[i] == \result);
      @ also
      @ public exceptional_behavior
      @ requires path == null || !path.isValid() || !containsPath(path);
      @ signals_only PathNotFoundException;
      @*/
    public /*@ pure @*/ int getPathId(Path path) throws PathNotFoundException {
        if (path != null && path.isValid() && containsPath(path)) {
            return pathList.get(path);
        } else {
            throw new PathNotFoundException(path);
        }
    }

    //@ ensures \result == (\exists int i; 0 <= i < nodes.length; nodes[i] == node);
    public /*@ pure @*/ boolean containsNode(int node) {
        return globalNodesCount.containsKey(node);
    }

    /*@ normal_behavior
      @ requires containsNode(node);
      @ ensures (\exists int i; 0 <= i < nodes.length; nodes[i] == node && \result == nodeToCount[i]);
      @ also
      @ normal_behavior
      @ requires !containsNode(node);
      @ ensures \result == 0;
      @*/
    public /*@ pure @*/ int getNodeCount(int node) {
        return globalNodesCount.getOrDefault(node, 0);
    }

    /*@ normal_behavior
      @ requires path != null && path.isValid() && !containsPath(path);
      @ assignable pList, pidList, nodes, nodeToCount;
      @ ensures \result == \old(pList.length);
      @ ensures (\exists int i; 0 <= i && i < pList.length; pList[i].equals(path) &&
      @           \old(pList.length) == pidList[i]);
      @ ensures  pList.length == (\old(pList.length) + 1) &&
      @          pidList.length == (\old(pidList.length) + 1);
      @ ensures (\forall int i; 0 <= i && i < \old(pList.length);
      @           (\exists int j; 0 <= j && j < pList.length;
      @             \old(pList[i]).equals(pList[j]) && \old(pidList[i]) == pidList[j]));
      @ ensures (\forall int i; path.containsNode(i) || \old(this.containsNode(i));
      @          this.getNodeCount(i) == \old(this.getNodeCount(i)) + path.getNodeCount(i));
      @ also
      @ normal_behavior
      @ requires path == null || path.isValid() == false || containsPath(path);
      @ assignable \nothing;
      @ ensures \result == 0;
      @*/
    public int addPath(Path path) {
        if (path != null && path.isValid() && !containsPath(path)) {
            pathList.put(path, idCounter);
            pathIdList.put(idCounter, path);
            for (Integer node : path) {
                Integer prev = globalNodesCount.get(node);
                if (prev == null) {
                    globalNodesCount.put(node, 1);
                } else {
                    globalNodesCount.put(node, prev + 1);
                }
            }
            return idCounter++;
        }
        return 0;
    }

    /*@ public normal_behavior
      @ requires containsPathId(pathId);
      @ assignable pList, pidList, nodes, nodeToCount;
      @ ensures pList.length == (\old(pList.length) - 1) &&
      @          pidList.length == (\old(pidList.length) - 1);
      @ ensures (\forall int i; 0 <= i && i < \old(pList.length) && \old(pidList[i]) != pathId;
      @          (\exists int j; 0 <= j && j < pList.length;
      @             \old(pList[i]).equals(pList[j]) && pidList[i] == pidList[j]));
      @ ensures (\forall int i; 0 <= i && i < pidList.length; pidList[i] != pathId);
      @ ensures (\forall int i; 0 <= i && i < pList.length; !pList[i].equals(\old(getPathById(pathId))));
      @ ensures (\forall int i; \old(getPathById(pathId).containsNode(i)); this.getNodeCount(i) ==
      @             \old(this.getNodeCount(i)) - \old(getPathById(pathId).getNodeCount(i)));
      @ also
      @ public exceptional_behavior
      @ assignable \nothing;
      @ signals (PathIdNotFoundException e) !containsPathId(pathId);
      @*/
    public void removePathById(int pathId) throws PathIdNotFoundException {
        if (containsPathId(pathId)) {
            Path path = pathIdList.remove(pathId);
            pathList.remove(path);
            for (Integer node : path) {
                Integer prev = globalNodesCount.get(node);
                globalNodesCount.put(node, prev - 1);
            }
        } else {
            throw new PathIdNotFoundException(pathId);
        }
    }

    /*@
     @ TODO 1: 编写规格
     @ public normal_behavior
     @ requires (\forall int i; 0 <= i && i < oldPList.length();
     @  (\forall int j; 0 <= j && j <= oldPList.length();
     @   (i!=j && oldPList[i].isUglyPath() && oldPList[j].isUglyPath()) ==> oldPList[i].extractLoopPath()!= oldPList[j].extractLoopPath())));
     @ assignable pList, pidList, nodes, nodeToCount
     @ 1 ensures pList.size() == pidList.size()
     @ 2 ensures (\forall Path path; \old(containsPath(path)); !path.isUglyPath() ==> containsPath(path))
     @ 3 ensures (\forall Path path; \old(containsPath(path)); path.isUglyPath() ==> !containsPath(path))
     @ 4 ensures (\forall Path path; \old(containsPath(path)) && path.isUglyPath();
     @            !\old(containsPath(path.extractLoopPath())) ==> containsPath(path.extractLoopPath))
     @ 5 ensures (\forall Path path; containsPath(path); !path.isUglyPath())
     @ 6 ensures (\forall int i; 0 < i && i < \old(nodeToCount.length); \old(this.getNodeCount(nodes[i]))
     @             == this.getNodeCount(nodes[i]) +
     @             (\sum Path path; \old(path.containsNode(nodes[i]));
     @               !path.isUglyPath() ? 0 :
     @               path.isUglyPath() && !path.extractLoopPath().containsNode(nodes[i]) ? -1 :
     @               path.isUglyPath() && path.extractLoopPath().containsNode(nodes[i]) &&
     @               (\exists Path pathNow; containsPath(pathNow); pathNow.equals(path)) ? 0 :
     @               path.isUglyPath() && path.extractLoopPath().containsNode(nodes[i]) &&
     @               (\non_exists Path pathNow; containsPath(pathNow); pathNow.equals(path)) ? -1 : 0)
     @ 7 ensures (\forall int i; 0 < i && i < pList.length;
     @             (\exists int j; 0 < j && j < pidList.length; pList[i].getId() == pidList[j])
     @ also
     @ public exception_behavior
     @ assignable \nothing
     @ signals (LoopDuplicateException e) (\exists Path path;
     @         containsPath(path) && path.isUglyPath();
     @         (\exists Path path2;
     @         containsPath(path2) &&
     @         path2.isUglyPath() &&
     @         !path2.equals(path);
     @*/

    public boolean beautifyPathsOk(
            HashMap<Path, Integer> beforePathList, HashMap<Path, Integer> afterPathList,
            HashMap<Integer, Path> beforePathIdList, HashMap<Integer, Path> afterPathIdList,
            HashMap<Integer, Integer> beforeNodeCount, HashMap<Integer, Integer> afterNodeCount) {

        ArrayList<Path> pList = new ArrayList<>();              // 对应 pList
        ArrayList<Path> oldPList = new ArrayList<>();           // 对应 \old(pList)
        ArrayList<Integer> pidList = new ArrayList<>();         // 对应 pidList
        ArrayList<Integer> oldPidList = new ArrayList<>();      // 对应 \old(pidList)
        ArrayList<Integer> nodes = new ArrayList<>();           // 对应 nodes
        ArrayList<Integer> oldNodes = new ArrayList<>();        // 对应 \old(nodes)
        ArrayList<Integer> nodeToCount = new ArrayList<>();     // 对应 nodeToCount
        ArrayList<Integer> oldNodeToCount = new ArrayList<>();  // 对应 \old(nodeToCount)

        HashMap<Path, Integer> deletedPaths = new HashMap<>();
        // 构造 oldPList 和 oldPidList
        for (Map.Entry<Path, Integer> e : beforePathList.entrySet()) {
            if (!beforePathIdList.get(e.getValue()).equals(e.getKey())) {
                return false;
            }
            if (e.getKey().isUglyPath()) {
                deletedPaths.put(e.getKey(), e.getValue());
            }
            oldPList.add(e.getKey());
            oldPidList.add(e.getValue());
        }

        // 构造 pList 和 pIdList
        for (Map.Entry<Path, Integer> e : afterPathList.entrySet()) {
            if (!afterPathIdList.get(e.getValue()).equals(e.getKey())) {
                return false;
            }
            pList.add(e.getKey());
            pidList.add(e.getValue());
        }

        // 构造 nodes 和 oldNodes
        nodes.addAll(afterNodeCount.keySet());
        oldNodes.addAll(beforeNodeCount.keySet());

        // 构造 nodeToCount 和 oldNodeToCount
        nodeToCount.addAll(afterNodeCount.values());
        oldNodeToCount.addAll(beforeNodeCount.values());

        // TODO 2: 根据规格编写 ok 方法
        //2
        if (pList.size() != oldPList.size() - deletedPaths.size()) {
            return false;
        }
        if (pidList.size() != oldPidList.size() - deletedPaths.size()) {
            return false;
        }
        //3
        for (Path path : oldPList) {
            if (path.isUglyPath()) {
                Path newPath = path.extractLoopPath();
                for (Integer node : path) {
                    //如果newPath与已有路径相同，或者newPath不包含该节点
                    if (!newPath.containsNode(node) || oldPList.contains(newPath)) {
                        beforeNodeCount.put(node, beforeNodeCount.get(node) - 1);
                    }
                }
                if (pList.contains(path)) {
                    return false;
                }
                if (pidList.contains(deletedPaths.get(path))) {
                    return false;
                }
            }
        }
        //4
        for (int i = 0; i < pList.size(); i++) {
            if (pList.get(i).isUglyPath()) {
                return false;
            }
            for (int j = i + 1; j < pList.size(); j++) {
                if (pList.get(i).equals(pList.get(j))) {
                    return false;
                }
            }
        }
        //5
        if (!beforeNodeCount.equals(afterNodeCount)) {
            return false;
        }
        return true;
    }

    public void beautifyPaths() throws LoopDuplicateException {
        // TODO 3: 编写业务代码
        ArrayList<Path> deletedPaths = new ArrayList<>();
        for (Map.Entry<Path, Integer> entry1 : pathList.entrySet()) {
            if (!entry1.getKey().isUglyPath()) {
                continue;
            }
            deletedPaths.add(entry1.getKey());
            for (Map.Entry<Path, Integer> entry2 : pathList.entrySet()) {
                if (entry2.getKey().equals(entry1.getKey())) {
                    continue;
                }
                if (entry2.getKey().extractLoopPath().equals(entry1.getKey().extractLoopPath())) {
                    throw new LoopDuplicateException();
                }
            }
        }
        for (Path path : deletedPaths) {
            Path newPath = path.extractLoopPath();
            addPath(newPath);
            try {
                removePathById(pathList.get(path));
            } catch (PathIdNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /*@ public normal_behavior
      @ assignable \nothing;
      @ ensures (\exists int[] arr;
      @            (\forall int i, j; 0 <= i && i < j && j < arr.length; arr[i] != arr[j]);
      @            (\forall int i; 0 <= i && i < arr.length;
      @                 (\exists Path p; this.containsPath(p); p.containsNode(arr[i]))) &&
      @            (\forall Path p; this.containsPath(p);
      @                 (\forall int node; p.containsNode(node);
      @                     (\exists int i; 0 <= i && i < arr.length; node == arr[i]))) &&
      @            (\result == arr.length));
      @*/
    public /*@ pure @*/ int getDistinctNodeCount() {
        int count = 0;
        for (Map.Entry<Integer, Integer> entry : globalNodesCount.entrySet()) {
            if (entry.getValue() > 0) {
                count++;
            }
        }
        return count;
    }
}
