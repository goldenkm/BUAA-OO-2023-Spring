import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderingLibrarian {
    private ArrayList<Pair<String, String>> orderList; //studentId + bookId

    public OrderingLibrarian() {
        this.orderList = new ArrayList<>();
    }

    public void order(String bookId, Student student, String date) {
        if (student.canOrder(bookId, date)) {
            System.out.println("[" + Clock.getDate() + "] "
                    + student.getId() + " ordered " + bookId + " from ordering librarian");
            orderList.add(new Pair<>(student.getId(), bookId));
            student.addOrder(bookId);
        }
    }

    public void delOrder(String studentId) {
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getKey().equals(studentId)) {
                if (orderList.get(i).getValue().charAt(0) == 'B') {
                    orderList.remove(i);
                    i--;
                }
            }
        }
    }

    public ArrayList<Book> inform(ArrayList<Book> books, HashMap<String, Student> students) {
        HashMap<String, Boolean> visited = new HashMap<>(); //用来记录那些已经接到一本b类书的人
        ArrayList<Book> remains = new ArrayList<>();
        remains.addAll(books);
        for (int i = 0; i < orderList.size(); i++) {
            if (visited.containsKey(orderList.get(i).getKey())) {
                orderList.remove(i);
                i--;
                continue;
            }
            if (books.size() == 0) {
                //减少遍历时间
                break;
            }
            for (Book book : books) {
                Pair pair = orderList.get(i);
                if (book.toString().equals(pair.getValue())) {
                    Student student = students.get(pair.getKey());
                    if (!student.canBorrowBookB() && book.getType() == 'B') {
                        continue;
                    }
                    System.out.println("[" + Clock.getDate() + "] "
                            + pair.getKey() + " borrowed " + pair.getValue() +
                            " from ordering librarian");
                    student.addBook(book);
                    //考虑借到一本B类书的情况
                    if (orderList.get(i).getValue().charAt(0) == 'B') {
                        visited.put(orderList.get(i).getKey(), true);
                    }
                    for (Book remain : remains) {
                        if (remain.toString().equals(orderList.get(i).getValue())) {
                            remains.remove(remain);
                            break;
                        }
                    }
                    orderList.remove(i);
                    i--;
                    books.remove(book);
                    break;
                }
            }
        }
        return remains;
    }
}
