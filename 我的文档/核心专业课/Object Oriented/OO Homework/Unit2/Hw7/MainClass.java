import com.oocourse.elevator3.TimableOutput;

import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();  // 初始化时间戳
        // 创建输入处理器
        InputHandler inputHandler = new InputHandler();
        RequestQueue requestQueue = new RequestQueue();
        inputHandler.setRequestQueue(requestQueue);
        // 创建调度器
        Scheduler scheduler = new Scheduler(requestQueue);
        inputHandler.setScheduler(scheduler);
        // 代码顺序很重要！！！
        // 启动电梯线程
        scheduler.setElevators(initElevators(scheduler));
        // 启动输入线程
        new Thread(inputHandler, "inputHandler").start();
        /*
        // 启动电梯线程
        scheduler.setElevators(initElevators(scheduler));
         */
    }

    public static ArrayList<Elevator> initElevators(Scheduler scheduler) {
        ArrayList<Elevator> elevators = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            Elevator elevator = new Elevator(i, scheduler, 1, 6, 0.4, 2047);
            elevators.add(elevator);
        }
        FloorGraph.updateGraph(elevators);
        for (Elevator elevator : elevators) {
            new Thread(elevator, "e" + elevator.getId()).start();
        }
        return elevators;
    }
}
