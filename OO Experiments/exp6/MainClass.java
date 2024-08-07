import java.util.HashMap;

public class MainClass {
    public static void main(String[] args) {
        int[] list1 = {1, 2, 3, 1, 4, 1};
        int[] list2 = {4, 1, 2, 3, 4};
        int[] list3 = {1, 2, 3, 1, 4, 5};
        Path path1 = new Path(list1);
        Path path2 = new Path(list2);
        Path path3 = new Path(list3);
        PathContainer pathContainer = new PathContainer();
        pathContainer.addPath(path1);
        pathContainer.addPath(path2);
        pathContainer.addPath(path3);
        try {
           pathContainer.beautifyPaths();
        } catch (LoopDuplicateException e) {
            throw new RuntimeException(e);
        }
    }

}
