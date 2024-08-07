import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

public class Adventurer implements Commodity {
    private int id;
    private String name;
    private BigInteger commodity = new BigInteger("0");
    private ArrayList<Commodity> commodities;
    private double health = 100.0;
    private double exp;
    private double money;

    public Adventurer() {
    }

    public Adventurer(int id) {
        this.id = id;
    }

    public Adventurer(int id, String name, BigInteger commodity, ArrayList<Commodity> commodities,
                      double health, double exp, double money) {
        this.id = id;
        this.name = name;
        this.commodity = commodity;
        this.commodities = commodities;
        this.health = health;
        this.exp = exp;
        this.money = money;
    }

    public ArrayList<Commodity> getCommodities() {
        return commodities;
    }

    public void setCommodities(ArrayList<Commodity> commodities) {
        this.commodities = commodities;
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

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getExp() {
        return exp;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    @Override
    public BigInteger getCommodity() {
        BigInteger totalCommodity = new BigInteger("0");
        for (Commodity commodity1 : this.getCommodities()) {
            totalCommodity = totalCommodity.add(commodity1.getCommodity());
        }
        return totalCommodity;
    }

    public void setCommodity(BigInteger commodity) {
        this.commodity = commodity;
    }

    public void use(Commodity commodity) {
        commodity.usedBy(this);
    }

    @Override
    public void usedBy(Adventurer user) {
        Collections.sort(commodities);
        for (Commodity commodity1 : commodities) {
            user.use(commodity1);
        }
        //System.out.println(toString());
    }

    @Override
    public String toString() {
        return "The adventurer's " +
                "id is " + id +
                ", name is " + name +
                ", health is " + health +
                ", exp is " + exp +
                ", money is " + money +
                '.';
    }

    @Override
    public int compareTo(Commodity other) {
        if (this.getCommodity().compareTo(other.getCommodity()) != 0) {
            return other.getCommodity().compareTo(this.getCommodity());
        } else {
            return other.getId() - this.getId();
        }
    }
}
