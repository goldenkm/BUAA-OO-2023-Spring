import java.util.ArrayList;
import java.util.HashMap;

public class Library {
    private BorrowingLibrarian borrowingLibrarian;
    private ArrangingLibrarian arrangingLibrarian;
    private OrderingLibrarian orderingLibrarian;
    private Machine machine;
    private LogisticsDivision logisticsDivision;
    private Bookshelf bookshelf;
    private HashMap<String, Student> students;

    public Library(Bookshelf bookshelf) {
        this.borrowingLibrarian = new BorrowingLibrarian();
        this.arrangingLibrarian = new ArrangingLibrarian();
        this.orderingLibrarian = new OrderingLibrarian();
        this.machine = new Machine();
        this.logisticsDivision = new LogisticsDivision();
        this.bookshelf = bookshelf;
        this.students = new HashMap<>();
    }

    public void addStudent(String studentId) {
        if (!students.containsKey(studentId)) {
            students.put(studentId, new Student(studentId));
        }
    }

    public void borrowed(Student student, String bookId) {
        System.out.println("[" + Clock.getDate() + "] " +
                student.getId() + " queried " + bookId + " from self-service machine");
        if (bookshelf.hasBook(bookId)) {
            Book book = bookshelf.getBook(bookId);
            if (bookId.charAt(0) != 'A') {
                bookshelf.delBook(book);
            }
            char type = bookId.charAt(0);
            if (type == 'B') {
                if (borrowingLibrarian.check(student) && student.canBorrowBookB()) {
                    borrowingLibrarian.lend(student, book);
                    orderingLibrarian.delOrder(student.getId());
                } else {
                    borrowingLibrarian.addBook(book);
                }
            } else if (type == 'C') {
                if (machine.check(student, book)) {
                    machine.lend(student, book);
                } else {
                    machine.addBook(book);
                }
            }
        } else {
            orderingLibrarian.order(bookId, student, Clock.getDate());
        }
    }

    public void operate(String op, String bookId, String studentId) {
        Student student = students.get(studentId);
        if (op.equals("borrowed")) {
            borrowed(student, bookId);
        } else if (op.equals("smeared")) {
            student.smear(bookId);
        } else if (op.equals("lost")) {
            student.lose(bookId);
            borrowingLibrarian.punish(student);
            if (bookId.charAt(0) == 'B' &&
                    borrowingLibrarian.getBorrowingList().containsKey(studentId)) {
                borrowingLibrarian.getBorrowingList().remove(studentId);
            }
        } else {
            Book returnedBook = student.returnBook(bookId);
            //不管是哪类书都是借还管理员收罚款
            if (returnedBook.isDamaged()) {
                borrowingLibrarian.punish(student);
                if (returnedBook.getType() == 'B') {
                    borrowingLibrarian.getBorrowingList().remove(studentId);
                    System.out.println("[" + Clock.getDate() + "] " +
                            studentId + " returned " + bookId +
                            " to borrowing and returning librarian");
                } else if (returnedBook.getType() == 'C') {
                    System.out.println("[" + Clock.getDate() + "] " + studentId
                            + " returned " + bookId + " to self-service machine");
                }
                logisticsDivision.addBook(returnedBook);
            } else {
                if (returnedBook.getType() == 'B') {
                    borrowingLibrarian.addBook(returnedBook);
                    //保证每人只能借一本b类书
                    borrowingLibrarian.getBorrowingList().remove(studentId);
                    System.out.println("[" + Clock.getDate() + "] " + studentId
                            + " returned " + bookId +
                            " to borrowing and returning librarian");
                } else if (returnedBook.getType() == 'C') {
                    machine.addBook(returnedBook);
                    System.out.println("[" + Clock.getDate() + "] " + studentId
                            + " returned " + bookId + " to self-service machine");
                }
            }
        }
    }

    public void arrange() {
        arrangingLibrarian.getBooks().addAll(borrowingLibrarian.getBooks());
        borrowingLibrarian.getBooks().clear();
        arrangingLibrarian.getBooks().addAll(machine.getBooks());
        machine.getBooks().clear();
        arrangingLibrarian.getBooks().addAll(logisticsDivision.getBooks());
        logisticsDivision.getBooks().clear();
        ArrayList<Book> remains = orderingLibrarian.inform(arrangingLibrarian.getBooks(), students);
        arrangingLibrarian.setBooks(remains);
        arrangingLibrarian.returnBook(bookshelf);
    }
}
