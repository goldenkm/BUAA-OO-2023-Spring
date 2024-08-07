import java.util.ArrayList;
import java.util.HashMap;

public class BorrowingLibrarian {
    private ArrayList<Book> books; //借出失败的书
    private HashMap<String, String> borrowingList; //studentId + bookId

    public BorrowingLibrarian() {
        this.books = new ArrayList<>();
        this.borrowingList = new HashMap<>();
    }

    public HashMap<String, String> getBorrowingList() {
        return borrowingList;
    }

    public boolean check(Student student) {
        return !borrowingList.containsKey(student.getId())
                || borrowingList.get(student.getId()) == null;
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void delBook(Book book) {
        books.remove(book);
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void lend(Student student, Book book) {
        System.out.println("[" + Clock.getDate() + "] " + student.getId() +
                " borrowed " + book.toString() + " from borrowing and returning librarian");
        student.addBook(book);
        borrowingList.put(student.getId(), book.toString());
    }

    public void punish(Student student) {
        //丢失+损毁，print相关信息
        System.out.println("[" + Clock.getDate() + "] " + student.getId()
                + " got punished by borrowing and returning librarian");
    }
}
