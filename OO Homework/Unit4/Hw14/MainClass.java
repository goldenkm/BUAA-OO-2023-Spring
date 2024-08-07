import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class MainClass {
    private static ArrayList<Library> libraries = new ArrayList<>(); //school+library
    private static ArrayList<Pair<Student, String>> requestList = new ArrayList<>(); //student+book
    private static Scanner scanner = new Scanner(System.in);
    //为了状态图设置的变量
    private static String school1B = "";
    private static String school2B = "";

    public static void main(String[] args) {
        int t = scanner.nextInt();
        scanner.nextLine();
        createLibraries(t);
        buildNetwork();
        int m = scanner.nextInt();
        scanner.nextLine();
        LinkedList<String> behaviors = new LinkedList<>();
        while (m > 0) {
            String behavior = scanner.nextLine();
            behaviors.offer(behavior);
            m--;
        }
        Clock.initialClock();
        System.out.println("[2023-01-01] arranging librarian arranged all the books");
        while (!behaviors.isEmpty()) {
            String behavior = behaviors.poll();
            String[] records = behavior.split(" ");
            String date = records[0].substring(1, records[0].length() - 1);
            String school = records[1].split("-")[0];
            Library library = getLib(school);
            while (!Clock.getDate().equals(date)) {
                endWork();
                Clock.addByDay();
                startWork();
            }
            String studentId = records[1].split("-")[1];
            library.addStudent(school, studentId);
            String op = records[2];
            String bookId = records[3];
            library.operate(op, bookId, studentId);
        }
        if (!canEnd()) {
            endWork();
        }
    }

    public static void createLibraries(int num) {
        int t = num;
        while (t > 0) {
            String school = scanner.next();
            int n = scanner.nextInt();
            scanner.nextLine();
            Bookshelf bookshelf = new Bookshelf();
            while (n > 0) {
                String bookInfo = scanner.nextLine();
                String[] records = bookInfo.split(" ");
                int count = Integer.parseInt(records[1]);
                String multiSchoolFlag = records[2];
                char type = records[0].charAt(0);
                String id = records[0];
                while (count > 0) {
                    Book book = new Book(type, id, school);
                    if (multiSchoolFlag.equals("Y")) {
                        book.setMultiSchool(true);
                    }
                    bookshelf.addBook(book);
                    count--;
                }
                n--;
            }
            Library library = new Library(school, bookshelf);
            libraries.add(library);
            t--;
        }
    }

    public static void buildNetwork() {
        //建立学校之间的关系
        for (Library me : libraries) {
            ManagementDivision managementDivision = new ManagementDivision(me.getSchool());
            for (Library other : libraries) {
                if (!other.getSchool().equals(me.getSchool())) {
                    managementDivision.connect(other);
                }
            }
            me.setManagementDivision(managementDivision);
        }
    }

    public static void startWork() {
        //3、输出receive的信息
        for (Library lib : libraries) {
            lib.receiveBooks();
        }
        //4、通知校际借阅的同学来领书
        ArrayList<Student> temp = new ArrayList<>();
        for (Library lib : libraries) {
            for (int i = 0; i < requestList.size(); i++) {
                Pair<Student, String> pair = requestList.get(i);
                String s = pair.getKey().getSchool();
                school1B = s;
                school2B = lib.getSchool();
                if (lib.getSchool().equals(s)) {
                    System.out.println("(State)[" + Clock.getDate() + "] " +
                            pair.getValue() + " transfers from OtherSchool to OtherStudent");
                    temp.add(pair.getKey());
                    Library l = getLib(s);
                    l.dealMultiSchoolRequest(pair.getKey().getId(), pair.getValue());
                }
            }
        }
        //该请求清单里的请求应该一定会被满足
        requestList.clear();
        if (Clock.needToArrange()) {
            //写法非常傻逼，但是为了输出顺序不得不这么写
            for (Library lib : libraries) {
                lib.purchase();
            }
            System.out.println("[" + Clock.getDate() + "] "
                    + "arranging librarian arranged all the books");
            for (Library lib : libraries) {
                lib.arrange();
            }
            Clock.clearInterval();
        }
    }

    public static void endWork() {
        //一天的结束需要做的工作
        //1、处理请求清单
        for (Library lib : libraries) {
            for (int i = 0; i < requestList.size(); i++) {
                Pair<Student, String> pair = requestList.get(i);
                String school = pair.getKey().getSchool();
                if (school.equals(lib.getSchool())) {
                    if (!lib.checkMultiSchool(pair.getKey().getId(),
                            pair.getValue(), requestList, i)) {
                        //不符合校际借阅要求
                        requestList.remove(i);
                        i--;
                        continue;
                    }
                    boolean borrowFromOtherSchool = lib.canBorrowFromOther(pair.getValue());
                    if (!borrowFromOtherSchool) {
                        if (pair.getKey().canOrder(pair.getValue())) {
                            lib.dealOrder(pair.getKey().getId(), pair.getValue());
                            requestList.remove(i);
                            i--;
                        }
                    }
                }
            }
        }
        //2、将校际借阅的书还回去
        for (Library lib : libraries) {
            lib.transportFromOther(requestList);
            lib.returnBooksToOther();
        }
    }

    public static void addRequest(Student student, String bookId) {
        requestList.add(new Pair<>(student, bookId));
    }

    public static boolean canEnd() {
        for (Library library : libraries) {
            if (library.needToReturn()) {
                return false;
            }
        }
        return requestList.isEmpty();
    }

    public static Library getLib(String school) {
        for (Library library : libraries) {
            if (library.getSchool().equals(school)) {
                return library;
            }
        }
        return null;
    }
}