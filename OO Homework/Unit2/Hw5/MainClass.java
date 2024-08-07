import com.oocourse.elevator1.TimableOutput;
import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();  // 初始化时间戳
        // 创建输入处理器
        InputHandler inputHandler = new InputHandler();
        RequestQueue requestQueue = new RequestQueue();
        inputHandler.setRequestQueue(requestQueue);
        // 启动输入线程
        new Thread(inputHandler, "inputHandler").start();
        // 创建调度器
        Scheduler scheduler = new Scheduler(requestQueue);
        // 启动电梯线程
        scheduler.setElevators(initElevators(scheduler));
    }

    public static ArrayList<Elevator> initElevators(Scheduler scheduler) {
        ArrayList<Elevator> elevators = new ArrayList<>();
        Elevator elevator1 = new Elevator(1, scheduler);
        elevators.add(elevator1);
        Elevator elevator2 = new Elevator(2, scheduler);
        elevators.add(elevator2);
        Elevator elevator3 = new Elevator(3, scheduler);
        elevators.add(elevator3);
        Elevator elevator4 = new Elevator(4, scheduler);
        elevators.add(elevator4);
        Elevator elevator5 = new Elevator(5, scheduler);
        elevators.add(elevator5);
        Elevator elevator6 = new Elevator(6, scheduler);
        elevators.add(elevator6);
        new Thread(elevator1, "e1").start();
        new Thread(elevator2, "e2").start();
        new Thread(elevator3, "e3").start();
        new Thread(elevator4, "e4").start();
        new Thread(elevator5, "e5").start();
        new Thread(elevator6, "e6").start();
        return elevators;
    }
}
