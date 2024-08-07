import java.util.ArrayList;

public class Bookshelf {
    private ArrayList<Book> booksA;
    private ArrayList<Book> booksB;
    private ArrayList<Book> booksC;

    public Bookshelf() {
        this.booksA = new ArrayList<>();
        this.booksB = new ArrayList<>();
        this.booksC = new ArrayList<>();
    }

    public void addBook(Book book) {
        char type = book.getType();
        if (type == 'A') {
            booksA.add(book);
        } else if (type == 'B') {
            booksB.add(book);
        } else {
            booksC.add(book);
        }
    }

    public void delBook(Book book) {
        //有多个副本，但只能删除一个
        char type = book.getType();
        if (type == 'A') {
            for (int i = 0; i < booksA.size(); i++) {
                if (booksA.get(i).equals(book)) {
                    booksA.remove(i);
                    return;
                }
            }
        } else if (type == 'B') {
            for (int i = 0; i < booksB.size(); i++) {
                if (booksB.get(i).equals(book)) {
                    booksB.remove(i);
                    return;
                }
            }
        } else {
            for (int i = 0; i < booksC.size(); i++) {
                if (booksC.get(i).equals(book)) {
                    booksC.remove(i);
                    return;
                }
            }
        }
    }

    public boolean hasBook(String bookId) {
        char type = bookId.charAt(0);
        if (type == 'A') {
            for (Book book : booksA) {
                if (book.toString().equals(bookId)) {
                    return true;
                }
            }
        } else if (type == 'B') {
            for (Book book : booksB) {
                if (book.toString().equals(bookId)) {
                    return true;
                }
            }
        } else {
            for (Book book : booksC) {
                if (book.toString().equals(bookId)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Book getBook(String bookId) {
        char type = bookId.charAt(0);
        if (type == 'A') {
            for (Book book : booksA) {
                if (book.toString().equals(bookId)) {
                    return book;
                }
            }
        } else if (type == 'B') {
            for (Book book : booksB) {
                if (book.toString().equals(bookId)) {
                    return book;
                }
            }
        } else {
            for (Book book : booksC) {
                if (book.toString().equals(bookId)) {
                    return book;
                }
            }
        }
        return null;
    }
}
