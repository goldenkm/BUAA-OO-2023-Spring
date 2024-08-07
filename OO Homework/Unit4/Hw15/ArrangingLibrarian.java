import java.util.ArrayList;

public class ArrangingLibrarian {
    private ArrayList<Book> books;

    public ArrangingLibrarian() {
        this.books = new ArrayList<>();

    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void returnBook(Bookshelf bookshelf) {
        for (Book book : books) {
            bookshelf.addBook(book);
        }
        books.clear();
    }
}
