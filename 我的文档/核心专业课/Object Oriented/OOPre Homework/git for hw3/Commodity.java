import java.math.BigInteger;

public interface Commodity extends Comparable<Commodity> {
    public BigInteger getCommodity();

    public void usedBy(Adventurer adventurer);

    public String toString();

    public int getId();

    public void setId(int id);

    @Override
    public int compareTo(Commodity other);
}
