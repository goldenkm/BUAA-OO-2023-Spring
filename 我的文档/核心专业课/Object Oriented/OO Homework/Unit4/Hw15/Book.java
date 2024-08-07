import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Book {
    private char type;
    private String id;
    private String school;
    private boolean damaged;
    private String returnDate;
    private boolean multiSchool;

    public Book(char type, String id, String school) {
        this.type = type;
        this.id = id;
        this.school = school;
        this.damaged = false;
        this.returnDate = "";
        this.multiSchool = false;
    }

    public String getId() {
        return id;
    }

    public char getType() {
        return type;
    }

    public boolean isDamaged() {
        return damaged;
    }

    public boolean isMultiSchool() {
        return multiSchool;
    }

    public boolean isOverdue() {
        return Clock.getDate().compareTo(returnDate) > 0;
    }

    public String getSchool() {
        return school;
    }

    public void setDamaged(boolean damaged) {
        this.damaged = damaged;
    }

    public void setReturnDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate;
        try {
            startDate = format.parse(Clock.getDate());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        long startTime = startDate.getTime();
        long period = 0;
        if (this.type == 'B') {
            period = 30L * 24 * 60 * 60 * 1000;
        } else if (type == 'C') {
            period = 60L * 24 * 60 * 60 * 1000;
        }
        long returnTime = startTime + period;
        this.returnDate = format.format(returnTime);
    }

    public void setMultiSchool(boolean multiSchool) {
        this.multiSchool = multiSchool;
    }

    @Override
    public String toString() {
        return school + "-" + id;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Book) {
            return this.toString().equals(o.toString());
        }
        return false;
    }
}
