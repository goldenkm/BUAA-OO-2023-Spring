import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class FloorGraph {
    private static int[][] floorGraph = new int[11][11];

    public static void clearGraph() {
        for (int i = 0; i < floorGraph.length; i++) {
            for (int j = 0; j < floorGraph[0].length; j++) {
                floorGraph[i][j] = 0;
            }
        }
    }

    public static void updateGraph(ArrayList<Elevator> elevators) {
        clearGraph();
        synchronized (elevators) {
            for (int i = 0; i < 11; i++) {
                for (Elevator elevator : elevators) {
                    if (!elevator.isAccessible(i + 1)) {
                        continue;
                    }
                    for (Integer reachedFloor : elevator.getAccess()) {
                        if (floorGraph[i][reachedFloor - 1] == 0) {
                            floorGraph[i][reachedFloor - 1] = 1;
                            floorGraph[reachedFloor - 1][i] = 1;
                        }
                    }
                }
            }
        }
    }

    public static ArrayList findShortestPath(int fromFloor, int toFloor) {
        /*for (int i = 0; i < floorGraph.length; i++) {
            for (int j = 0; j < floorGraph[0].length; j++) {
                System.out.print(floorGraph[i][j]+" ");
            }
            System.out.println();
        }*/
        //定义 visited 数组，防止对节点进行重复遍历
        int start = fromFloor - 1;
        int end = toFloor - 1;
        boolean[] visited = new boolean[12];
        Queue<Integer> queue = new LinkedList<>();
        int[] ans = new int[12];
        //起点入队
        queue.offer(start);
        //标记起点
        ans[0] = start;
        visited[start] = true;
        while (!queue.isEmpty()) {
            int node = queue.poll();
            //q
            for (int i = 0; i < floorGraph[node].length; i++) {
                //System.out.println(node+" "+i+": "+graph[node][i]+" and "+visited[i]);
                if (floorGraph[node][i] != 0 && !visited[i]) {
                    ans[i] = node;
                    if (i == end) {
                        int pos = i;
                        ArrayList<Integer> path = new ArrayList<>();
                        path.add(end + 1);
                        while (ans[pos] != start) {
                            path.add(ans[pos] + 1);
                            pos = ans[pos];
                        }
                        path.add(start + 1);
                        Collections.reverse(path);
                        return path;
                    }
                    visited[i] = true;
                    queue.offer(i);
                }
            }
        }
        return null;
    }
}
