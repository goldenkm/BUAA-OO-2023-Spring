import java.util.LinkedList;
import java.util.Scanner;

public class MainClass {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Bookshelf bookshelf = new Bookshelf();
        while (n > 0) {
            String bookInfo = scanner.nextLine();
            String[] records = bookInfo.split(" ");
            int count = Integer.parseInt(records[1]);
            String[] strings = records[0].split("-");
            char type = strings[0].charAt(0);
            String  id = strings[1];
            while (count > 0) {
                Book book = new Book(type, id);
                bookshelf.addBook(book);
                count--;
            }
            n--;
        }
        Library library = new Library(bookshelf);
        int m = scanner.nextInt();
        scanner.nextLine();
        LinkedList<String> behaviors = new LinkedList<>();
        while (m > 0) {
            String behavior = scanner.nextLine();
            behaviors.offer(behavior);
            m--;
        }
        Clock.initialClock();
        while (!behaviors.isEmpty()) {
            String behavior = behaviors.poll();
            String[] records = behavior.split(" ");
            String date = records[0].substring(1, records[0].length() - 1);
            while (!Clock.getDate().equals(date)) {
                Clock.addByDay();
                if (Clock.needToArrange()) {
                    library.arrange();
                    Clock.clearInterval();
                }
            }
            String studentId = records[1];
            library.addStudent(studentId);
            String op = records[2];
            String bookId = records[3];
            //System.out.println(behavior+"!!!");
            library.operate(op, bookId, studentId);
        }
    }
}
