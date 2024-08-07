import java.util.ArrayList;
import java.util.HashMap;

public class Student {
    private String id;
    private ArrayList<Book> books;
    private ArrayList<String> orderList;
    private HashMap<String, Integer> borrowingCount; //date + count, 仅负责B类

    public Student(String id) {
        this.id = id;
        this.books = new ArrayList<>();
        this.orderList = new ArrayList<>();
        this.borrowingCount = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void addBook(Book book) {
        delOrder(book.toString());
        books.add(book);
    }

    public void addOrder(String bookId) {
        orderList.add(bookId);
    }

    public void delOrder(String bookId) {
        orderList.remove(bookId);
        //借到一本b，删除所有b的预定
        for (int i = 0; i < orderList.size(); i++) {
            if (bookId.charAt(0) == 'B') {
                if (orderList.get(i).charAt(0) == 'B') {
                    orderList.remove(i);
                    i--;
                }
            }
        }
    }

    public boolean hasBook(Book book) {
        return books.contains(book);
    }

    public boolean canBorrowBookB() {
        if (books.size() == 0) {
            return true;
        }
        for (Book book : books) {
            if (book.getType() == 'B') {
                return false;
            }
        }
        return true;
    }

    //必须要用bookId查，不然书架上没有的书，一定是null
    public boolean canOrder(String bookId, String date) {
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
        //对于相同书号仅能预定一次
        for (String ordered : orderList) {
            if (ordered.equals(bookId)) {
                return false;
            }
        }
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
            if (myBook.toString().equals(bookId)) {
                myBook.setDamaged(true);
            }
        }
    }

    public void lose(String bookId) {
        for (int i = 0; i < books.size(); i++) {
            if (bookId.equals(books.get(i).toString())) {
                books.get(i).setLost(true);
                books.remove(i);
                return;
            }
        }
    }

    public Book returnBook(String bookId) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).toString().equals(bookId)) {
                Book tmp = books.get(i);
                books.remove(i);
                return tmp;
            }
        }
        return null;
    }
}
