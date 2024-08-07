
import java.util.ArrayList;
import java.util.HashMap;

public class Student {
    private String school;
    private String id;
    private ArrayList<Book> books;
    private ArrayList<String> orderList;
    private HashMap<String, Integer> borrowingCount; //date + count, 仅负责B类

    public Student(String school, String id) {
        this.id = id;
        this.school = school;
        this.books = new ArrayList<>();
        this.orderList = new ArrayList<>();
        this.borrowingCount = new HashMap<>();
    }

    public String getSchool() {
        return school;
    }

    public String getId() {
        return id;
    }

    public void addBook(Book book) {
        //MainClass.getLibraries().get(school).delOrder(id, book.getId());
        book.setReturnDate();
        books.add(book);
    }

    public void addOrder(String bookId) {
        orderList.add(bookId);
    }

    public void removeOrder(String bookId) {
        orderList.remove(bookId);
    }

    public boolean canBorrow(String bookId) {
        char type = bookId.charAt(0);
        if (type == 'B') {
            if (!canBorrowBookB()) {
                return false;
            }
        }
        for (Book book : books) {
            if (book.toString().equals(bookId)) {
                return false;
            }
        }
        return true;
    }

    public boolean canBorrowBookB() {
        for (Book book : books) {
            if (book.getType() == 'B') {
                return false;
            }
        }
        return true;
    }

    //必须要用bookId查，不然书架上没有的书，一定是null
    public boolean canOrder(String bookId) {
        char type = bookId.charAt(0);
        //手中无B类+同书号
        for (Book myBook : books) {
            if (type == 'B' && myBook.getType() == 'B') {
                return false;
            }
            if (bookId.equals(myBook.toString())) {
                return false;
            }
        }
        String date = Clock.getDate();
        //一天三本以内
        if (borrowingCount.containsKey(date) && borrowingCount.get(date) >= 3) {
            return false;
        }
        //注意hashmap的添加细节
        if (borrowingCount.containsKey(date)) {
            borrowingCount.put(date, borrowingCount.get(date) + 1);
        } else {
            borrowingCount.put(date, 1);
        }
        return true;
    }

    public void smear(String bookId) {
        for (Book myBook : books) {
            if (myBook.getId().equals(bookId)) {
                myBook.setDamaged(true);
            }
        }
    }

    public void lose(String bookId) {
        for (int i = 0; i < books.size(); i++) {
            if (bookId.equals(books.get(i).getId())) {
                books.remove(i);
                return;
            }
        }
    }

    public Book returnBook(String bookId) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId().equals(bookId)) {
                Book tmp = books.get(i);
                books.remove(i);
                return tmp;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.school + "-" + this.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Student) {
            return o.toString().equals(this.toString());
        }
        return false;
    }
}
