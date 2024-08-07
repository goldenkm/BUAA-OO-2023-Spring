import java.util.ArrayList;

public class LogisticsDivision {
    private ArrayList<Book> books;

    public LogisticsDivision() {
        this.books = new ArrayList<>();
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void addBook(Book book) {
        book.setDamaged(false);
        books.add(book);
    }
}
