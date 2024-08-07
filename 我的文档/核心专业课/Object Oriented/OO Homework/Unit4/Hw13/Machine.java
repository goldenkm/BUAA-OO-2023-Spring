import java.util.ArrayList;

public class Machine {
    private ArrayList<Book> books;

    public Machine() {
        this.books = new ArrayList<>();
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public boolean check(Student student, Book book) {
        return !student.hasBook(book);
    }

    public void lend(Student student, Book book) {
        System.out.println("[" + Clock.getDate() + "] " + student.getId() +
                " borrowed " + book.toString() + " from self-service machine");
        student.addBook(book);
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void delBook(Book book) {
        books.remove(book);
    }
}
