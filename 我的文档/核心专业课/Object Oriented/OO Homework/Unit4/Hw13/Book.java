public class Book {
    private char type;
    private String id;
    private boolean damaged;
    private boolean lost;

    public Book(char type, String id) {
        this.type = type;
        this.id = id;
        this.damaged = false;
        this.lost = false;
    }

    public char getType() {
        return type;
    }

    public boolean isDamaged() {
        return damaged;
    }

    public boolean isLost() {
        return lost;
    }

    public void setDamaged(boolean damaged) {
        this.damaged = damaged;
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }

    @Override
    public String toString() {
        return type + "-" + id;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Book) {
            return this.toString().equals(o.toString());
        }
        return false;
    }
}
