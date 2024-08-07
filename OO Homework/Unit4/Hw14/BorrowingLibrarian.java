import java.util.ArrayList;

public class BorrowingLibrarian {
    private ArrayList<Book> books; //借出失败的书

    public BorrowingLibrarian() {
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void lend(Student student, Book book) {
        System.out.println("[" + Clock.getDate() + "] "
                + "borrowing and returning librarian lent " + book + " to " + student);
        System.out.println("[" + Clock.getDate() + "] " + student +
                " borrowed " + book + " from borrowing and returning librarian");
        student.addBook(book);
    }

    public void punish(Student student) {
        //丢失+损毁，print相关信息
        System.out.println("[" + Clock.getDate() + "] " + student
                + " got punished by borrowing and returning librarian");
        System.out.println("[" + Clock.getDate() + "] "
                + "borrowing and returning librarian received " + student + "'s fine");
    }
}
