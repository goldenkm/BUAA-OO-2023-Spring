import javafx.util.Pair;

import java.util.ArrayList;

public class OrderingLibrarian {
    private ArrayList<Pair<Student, String>> orderList; //studentId + bookId

    public OrderingLibrarian() {
        this.orderList = new ArrayList<>();
    }

    public void orderNewBook(String bookId, Student student) {
        System.out.println("[" + Clock.getDate() + "] "
                + student + " ordered " + student.getSchool()
                + "-" + bookId + " from ordering librarian");
        System.out.println("[" + Clock.getDate() + "] "
                + "ordering librarian recorded " + student + "'s order of "
                + student.getSchool() + "-" + bookId);
        addOrder(student, bookId);
    }

    public void addOrder(Student student, String bookId) {
        orderList.add(new Pair<>(student, bookId));
    }

    public void delOrder(Student student, String bookId) {
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getKey().equals(student.getId())) {
                if (bookId.charAt(0) == 'B' && orderList.get(i).getValue().charAt(0) == 'B') {
                    orderList.remove(i);
                    student.removeOrder(bookId);
                    i--;
                } else if (bookId.charAt(0) == 'C' && orderList.get(i).getValue().equals(bookId)) {
                    orderList.remove(i);
                    student.removeOrder(bookId);
                    i--;
                }
            }
        }
    }

    public ArrayList<Book> inform(ArrayList<Book> books) {
        if (orderList.size() > 0) {
            System.out.println("(Sequence)[" + Clock.getDate() + "] "
                    + "Library sends a message to OrderingLibrarian");
        }
        ArrayList<Book> remains = new ArrayList<>();
        remains.addAll(books);
        for (int i = 0; i < orderList.size(); i++) {
            for (Book book : books) {
                Pair<Student, String> pair = orderList.get(i);
                if (book.getId().equals(pair.getValue())) {
                    Student student = pair.getKey();
                    if (student.canBorrow(book.getId())) {
                        System.out.println("[" + Clock.getDate() + "] "
                                + "ordering librarian lent " + book + " to " + student);
                        System.out.println("[" + Clock.getDate() + "] " + student +
                                " borrowed " + book + " from ordering librarian");
                        System.out.println("(Sequence)[" + Clock.getDate() + "] "
                                + "OrderingLibrarian sends a message to Student");
                        student.addBook(book);
                        MainClass.getLib(student.getSchool()).
                                delPurchasingRequest(student, book.getId());
                        delOrder(student, book.getId());
                        for (Book remain : remains) {
                            if (remain.getId().equals(orderList.get(i).getValue())) {
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
        }
        return remains;
    }
}
