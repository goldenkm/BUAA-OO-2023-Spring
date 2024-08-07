import java.util.ArrayList;

public class Machine {
    private ArrayList<Book> books;

    public Machine() {
        this.books = new ArrayList<>();
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void lend(Student student, Book book) {
        System.out.println("[" + Clock.getDate() + "] "
                + "self-service machine lent " + book + " to " + student);
        System.out.println("[" + Clock.getDate() + "] " + student +
                " borrowed " + book + " from self-service machine");
        student.addBook(book);
    }

    public void addBook(Book book) {
        books.add(book);
    }
}
