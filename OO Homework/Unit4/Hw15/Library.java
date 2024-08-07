import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class Library {
    private String school;
    private BorrowingLibrarian borrowingLibrarian;
    private ArrangingLibrarian arrangingLibrarian;
    private OrderingLibrarian orderingLibrarian;
    private Machine machine;
    private LogisticsDivision logisticsDivision;
    private Bookshelf bookshelf;
    private ManagementDivision managementDivision;
    private HashMap<String, Student> students;

    //为状态图设计的变量
    private int statusB1 = 0;
    private int statusC1 = 0;
    private int statusB2 = 0;
    private int statusC2 = 0;
    private int statusR = 0;
    private int statusRO = 0;

    public Library(String school, Bookshelf bookshelf) {
        this.school = school;
        this.borrowingLibrarian = new BorrowingLibrarian();
        this.arrangingLibrarian = new ArrangingLibrarian();
        this.orderingLibrarian = new OrderingLibrarian();
        this.machine = new Machine();
        this.logisticsDivision = new LogisticsDivision();
        this.managementDivision = new ManagementDivision(school);
        this.bookshelf = bookshelf;
        this.students = new HashMap<>();
    }

    public String getSchool() {
        return school;
    }

    public void setManagementDivision(ManagementDivision managementDivision) {
        this.managementDivision = managementDivision;
    }

    public boolean hasMultiSchoolBook(String bookId) {
        return bookshelf.hasMultiSchoolBook(bookId);
    }

    public boolean checkMultiSchool(String studentId, String bookId,
                                    ArrayList<Pair<Student, String>> requestList, int index) {
        Student student = students.get(studentId);
        if (!student.canBorrow(bookId)) {
            return false;
        }
        //检查requestList里面的请求
        for (int i = 0; i < index; i++) {
            if (requestList.get(i).getKey().getId().equals(studentId)) {
                if (bookId.charAt(0) == 'B') {
                    if (requestList.get(i).getValue().charAt(0) == 'B') {
                        return false;
                    }
                } else if (bookId.charAt(0) == 'C') {
                    if (requestList.get(i).getValue().equals(bookId)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void addBook(Book book) {
        managementDivision.getReturnedFrom().add(book);
        managementDivision.getTemp().add(book);
    }

    public void addStudent(String school, String studentId) {
        if (!students.containsKey(studentId)) {
            students.put(studentId, new Student(school, studentId));
        }
    }

    public void transportFromOther(ArrayList<Pair<Student, String>> requestList) {
        managementDivision.transportBooks(requestList);
    }

    public boolean canBorrowFromOther(String bookId) {
        return managementDivision.canBorrowFromOther(bookId);
    }

    public boolean needToReturn() {
        return managementDivision.getReturnTo().size() > 0;
    }

    public void returnBooksToOther() {
        managementDivision.returnBooks();
    }

    public void receiveBooks() {
        managementDivision.receiveBooks();
    }

    public void dealMultiSchoolRequest(String studentId, String bookId) {
        managementDivision.inform(students, studentId, bookId);
    }

    public Book lendToOtherSchool(String bookId) {
        Book book = bookshelf.getBook(bookId);
        bookshelf.delBook(book);
        System.out.println("[" + Clock.getDate() + "] " +
                book + " got transported by purchasing department in " + school);
        return book;
    }

    public void borrowed(Student student, String bookId) {
        System.out.println("[" + Clock.getDate() + "] " +
                student + " queried " + bookId + " from self-service machine");
        System.out.println("[" + Clock.getDate() + "] " +
                "self-service machine provided information of " + bookId);
        if (bookshelf.hasBook(bookId)) {
            Book book = bookshelf.getBook(bookId);
            if (bookId.charAt(0) != 'A') {
                bookshelf.delBook(book);
            }
            char type = bookId.charAt(0);
            String bookInfo = book.getSchool() + "-" + bookId;
            if (type == 'B') {
                if (student.canBorrow(bookId)) {
                    borrowingLibrarian.lend(student, book);
                    statusB1 = 1;
                    System.out.println("(State)[" + Clock.getDate() + "] " +
                            book.getId() + " transfers from MySchool to MyStudent");
                } else {
                    System.out.println("[" + Clock.getDate() + "] " +
                            "borrowing and returning librarian refused lending " +
                            book + " to " + student);
                    statusB1 = 0;
                    System.out.println("(State)[" + Clock.getDate() + "] " +
                            book.getId() + " transfers from MySchool to MySchool");
                    borrowingLibrarian.addBook(book);
                }
            } else if (type == 'C') {
                if (student.canBorrow(bookId)) {
                    machine.lend(student, book);
                    statusC1 = 1;
                    System.out.println("(State)[" + Clock.getDate() + "] " +
                            book.getId() + " transfers from MySchool to MyStudent");
                } else {
                    System.out.println("[" + Clock.getDate() + "] " +
                            "self-service machine refused lending " +
                            book.getSchool() + "-" + bookInfo + " to " + student);
                    statusC1 = 0;
                    System.out.println("(State)[" + Clock.getDate() + "] " +
                            book.getId() + " transfers from MySchool to MySchool");
                    machine.addBook(book);
                }
            }
            //借到书需要维护预定清单和购买清单
            delOrder(student, bookId);
            managementDivision.delPurchaseRequest(student, bookId);
        } else {
            //校际借阅流程
            MainClass.addRequest(student, bookId);
        }
    }

    public void delPurchasingRequest(Student student, String bookId) {
        managementDivision.delPurchaseRequest(student, bookId);
    }

    public void delOrder(Student student, String bookId) {
        orderingLibrarian.delOrder(student, bookId);
        managementDivision.delPurchaseRequest(student, bookId);
    }

    public void dealOrder(String studentId, String bookId) {
        Student student = students.get(studentId);
        orderingLibrarian.orderNewBook(bookId, student);
        System.out.println("(Sequence)[" + Clock.getDate() + "] "
                + "Library sends a message to OrderingLibrarian");
        if (!bookshelf.hasBookOnList(bookId) && !managementDivision.checkInOtherSchool(bookId)) {
            //购买新书
            HashMap<String, ArrayList<String>> purchasingList
                    = managementDivision.getPurchasingList();
            if (purchasingList.containsKey(bookId)) {
                if (!purchasingList.get(bookId).contains(studentId)) {
                    purchasingList.get(bookId).add(studentId);
                }
            } else {
                ArrayList<String> strings = new ArrayList<>();
                strings.add(studentId);
                purchasingList.put(bookId, strings);
            }
            System.out.println("(Sequence)[" + Clock.getDate() + "] "
                    + "Library sends a message to ManagementDivision");
        }
    }

    public void lost(Student student, String bookId) {
        student.lose(bookId);
        borrowingLibrarian.punish(student);
        bookshelf.delBookFromList(bookId);
    }

    public void repair(boolean flag, Book returnedBook) {
        System.out.println("[" + Clock.getDate() + "] " + returnedBook
                + " got repaired by logistics division in " + school);
        if (flag) {
            statusR = 1;
            System.out.println("(State)[" + Clock.getDate() + "] " +
                    returnedBook.getId() + " transfers from MySchool to MySchool");
            logisticsDivision.addBook(returnedBook);
        } else {
            statusR = 0;
            System.out.println("(State)[" + Clock.getDate() + "] " +
                    returnedBook.getId() + " transfers from OtherSchool to OtherSchool");
        }
    }

    public void returnBook(Student student, String bookId) {
        Book returnedBook = student.returnBook(bookId);
        //记录该书是否跨校
        boolean flag = returnedBook.getSchool().equals(school);
        //不管是哪类书都是借还管理员收罚款
        if (returnedBook.isDamaged()) {
            borrowingLibrarian.punish(student);
            if (returnedBook.getType() == 'B') {
                System.out.println("[" + Clock.getDate() + "] " +
                        student + " returned " + returnedBook +
                        " to borrowing and returning librarian");
                statusB2 = 1;
                System.out.println("[" + Clock.getDate() + "] " +
                        "borrowing and returning librarian collected "
                        + returnedBook + " from " + student);
            } else if (returnedBook.getType() == 'C') {
                System.out.println("[" + Clock.getDate() + "] " + student
                        + " returned " + returnedBook + " to self-service machine");
                statusC2 = 1;
                System.out.println("[" + Clock.getDate() + "] " +
                        "self-service machine collected " + returnedBook + " from " + student);
            }
            System.out.println("(State)[" + Clock.getDate() + "] " +
                    bookId + " transfers from MyStudent to MySchool");
            repair(flag, returnedBook);
        } else {
            if (returnedBook.isOverdue()) {
                borrowingLibrarian.punish(student);
            }
            if (returnedBook.getType() == 'B') {
                if (flag) {
                    borrowingLibrarian.addBook(returnedBook);
                }
                //保证每人只能借一本b类书
                System.out.println("[" + Clock.getDate() + "] " + student
                        + " returned " + returnedBook +
                        " to borrowing and returning librarian");
                System.out.println("[" + Clock.getDate() + "] " +
                        "borrowing and returning librarian collected "
                        + returnedBook + " from " + student);
                statusB2 = 1;
            } else if (returnedBook.getType() == 'C') {
                if (flag) {
                    machine.addBook(returnedBook);
                }
                System.out.println("[" + Clock.getDate() + "] " + student
                        + " returned " + returnedBook + " to self-service machine");
                System.out.println("[" + Clock.getDate() + "] " +
                        "self-service machine collected " + returnedBook + " from " + student);
                statusC2 = 1;
            }
            System.out.println("(State)[" + Clock.getDate() + "] " +
                    bookId + " transfers from MyStudent to MySchool");
        }
        if (!flag) {
            managementDivision.addReturnedBook(returnedBook);
            statusRO = 1;
            System.out.println("(State)[" + Clock.getDate() + "] " +
                    bookId + " transfers from OtherStudent to OtherSchool");
        }
    }

    public void operate(String op, String bookId, String studentId) {
        Student student = students.get(studentId);
        if (op.equals("borrowed")) {
            borrowed(student, bookId);
        } else if (op.equals("smeared")) {
            student.smear(bookId);
        } else if (op.equals("lost")) {
            lost(student, bookId);
        } else {
            returnBook(student, bookId);
        }
    }

    public void purchase() {
        managementDivision.purchase();
    }

    public void arrange() {
        arrangingLibrarian.getBooks().addAll(borrowingLibrarian.getBooks());
        borrowingLibrarian.getBooks().clear();
        arrangingLibrarian.getBooks().addAll(machine.getBooks());
        machine.getBooks().clear();
        arrangingLibrarian.getBooks().addAll(logisticsDivision.getBooks());
        logisticsDivision.getBooks().clear();
        arrangingLibrarian.getBooks().addAll(managementDivision.getPurchasedBooks());
        managementDivision.getPurchasedBooks().clear();
        arrangingLibrarian.getBooks().addAll(managementDivision.getReturnedFrom());
        managementDivision.getReturnedFrom().clear();
        ArrayList<Book> remains = orderingLibrarian.inform(arrangingLibrarian.getBooks());
        //orderingLibrarian.getOrderList().clear();
        arrangingLibrarian.setBooks(remains);
        arrangingLibrarian.returnBook(bookshelf);
    }
}
