import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManagementDivision {
    private String school;
    private HashMap<String, ArrayList<String>> purchasingList; //bookId + student[*]
    private ArrayList<Library> friendSchool;
    private ArrayList<Book> receivedBooks; //别的学校借过来的书
    private ArrayList<Book> purchasedBooks; //处理新书加购
    private ArrayList<Book> returnTo; //需要归还的校际借阅的书
    private ArrayList<Book> returnedFrom; //别的学校还回来的书
    private ArrayList<Book> temp; //缓冲区，以保证received的信息只输出一遍
    //为了状态图设计的变量
    private String school1P = "";
    private String school2P = "";
    private int statusLend = 0;

    public ManagementDivision(String school) {
        this.school = school;
        this.purchasingList = new HashMap<>();
        this.friendSchool = new ArrayList<>();
        this.receivedBooks = new ArrayList<>();
        this.purchasedBooks = new ArrayList<>();
        this.returnTo = new ArrayList<>();
        this.returnedFrom = new ArrayList<>();
        this.temp = new ArrayList<>();
    }

    public ArrayList<Book> getTemp() {
        return temp;
    }

    public ArrayList<Book> getReturnTo() {
        return returnTo;
    }

    public ArrayList<Book> getReturnedFrom() {
        return returnedFrom;
    }

    public ArrayList<Book> getPurchasedBooks() {
        return purchasedBooks;
    }

    public void addReturnedBook(Book book) {
        returnTo.add(book);
    }

    public void connect(Library library) {
        friendSchool.add(library);
    }

    public HashMap<String, ArrayList<String>> getPurchasingList() {
        return purchasingList;
    }

    public boolean checkInOtherSchool(String bookId) {
        for (Library library : friendSchool) {
            if (library.hasMultiSchoolBook(bookId)) {
                return true;
            }
        }
        return false;
    }

    public void delPurchaseRequest(Student student, String bookId) {
        char type = bookId.charAt(0);
        for (Map.Entry<String, ArrayList<String>> entry : purchasingList.entrySet()) {
            if (type == 'B' && entry.getKey().charAt(0) == 'B') {
                entry.getValue().remove(student.getId());
            } else if (type == 'C' && entry.getKey().equals(bookId)) {
                entry.getValue().remove(student.getId());
            }
        }
    }

    public void purchase() {
        for (Map.Entry<String, ArrayList<String>> entry : purchasingList.entrySet()) {
            String bookId = entry.getKey();
            char type = bookId.charAt(0);
            if (entry.getValue().size() == 0) {
                continue;
            }
            int count = Math.max(entry.getValue().size(), 3);
            System.out.println("[" + Clock.getDate() + "] " + school + "-" + bookId
                    + " got purchased by purchasing department in " + school);
            while (count > 0) {
                Book newBook = new Book(type, bookId, school);
                //新书默认允许校际借阅
                newBook.setMultiSchool(true);
                purchasedBooks.add(newBook);
                count--;
            }
        }
        if (purchasingList.size() > 0) {
            System.out.println("(Sequence)[" + Clock.getDate() + "] "
                    + "ManagementDivision sends a message to Library");
        }
        purchasingList.clear();
    }

    public boolean canBorrowFromOther(String bookId) {
        for (Library library : friendSchool) {
            if (library.hasMultiSchoolBook(bookId)) {
                return true;
            }
        }
        return false;
    }

    public void transportBooks(ArrayList<Pair<Student, String>> requestList) {
        //先删除相同的请求
        ArrayList<Pair<Student, String>> list = new ArrayList<>();
        for (Pair<Student, String> pair : requestList) {
            boolean flag = false;
            for (Pair<Student, String> newPair : list) {
                if (pair.getKey().equals(newPair.getKey())
                        && pair.getValue().equals(newPair.getValue())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                list.add(pair);
            }
        }
        for (Pair<Student, String> pair : list) {
            String school = pair.getKey().getSchool();
            String bookId = pair.getValue();
            if (school.equals(this.school)) {
                for (Library library : friendSchool) {
                    if (library.hasMultiSchoolBook(bookId)) {
                        statusLend = 1;
                        System.out.println("(State)[" + Clock.getDate() + "] " +
                                bookId + " transfers from MySchool to OtherSchool");
                        Book book = library.lendToOtherSchool(bookId);
                        receivedBooks.add(book);
                        break;
                    }
                }
            }
        }
    }

    public void receiveBooks() {
        for (Book book : receivedBooks) {
            System.out.println("[" + Clock.getDate() + "] " + book +
                    " got received by purchasing department in " + school);
        }
        for (Book book : temp) {
            System.out.println("[" + Clock.getDate() + "] " + book +
                    " got received by purchasing department in " + school);
        }
        temp.clear();
    }

    public void returnBooks() {
        for (Book book : returnTo) {
            for (Library library : friendSchool) {
                school1P = book.getSchool();
                school2P = library.getSchool();
                if (book.getSchool().equals(library.getSchool())) {
                    System.out.println("[" + Clock.getDate() + "] " +
                            book + " got transported by purchasing department in " + school);
                    System.out.println("(State)[" + Clock.getDate() + "] " +
                            book.getId() + " transfers from OtherSchool to MySchool");
                    library.addBook(book);
                    break;
                }
            }
        }
        returnTo.clear();
    }

    public void inform(HashMap<String, Student> students, String studentId, String bookId) {
        Student student = students.get(studentId);
        for (int i = 0; i < receivedBooks.size(); i++) {
            Book book = receivedBooks.get(i);
            if (book.getId().equals(bookId)) {
                System.out.println("[" + Clock.getDate() + "] " +
                        "purchasing department lent " + book + " to " + school + "-" + studentId);
                System.out.println("[" + Clock.getDate() + "] " + student +
                        " borrowed " + book + " from purchasing department");
                student.addBook(book);
                MainClass.getLib(school).delOrder(student, bookId);
                delPurchaseRequest(student, bookId);
                receivedBooks.remove(i);
                break;
            }
        }
    }
}
