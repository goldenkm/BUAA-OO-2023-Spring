import java.math.BigInteger;

public class Equipment implements Commodity {
    private int id;
    private String name;
    private BigInteger price;
    private BigInteger commodity;
    private String branch;

    public Equipment(int id, String name, BigInteger price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Equipment() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getPrice() {
        return price;
    }

    public void setPrice(BigInteger price) {
        this.price = price;
    }

    @Override
    public BigInteger getCommodity() {
        return price;
    }

    public void setCommodity(BigInteger commodity) {
        this.commodity = commodity;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    @Override
    public String toString() {
        return "id is " + id +
                ", name is " + name +
                ", price is " + price;
    }

    public void usedBy(Adventurer adventurer) {

    }

    @Override
    public int compareTo(Commodity other) {
        BigInteger otherCommodity = other.getCommodity();
        if (this.getCommodity().compareTo(other.getCommodity()) != 0) {
            return other.getCommodity().compareTo(this.getCommodity());
        } else {
            return other.getId() - this.getId();
        }
    }
}
