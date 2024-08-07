import java.util.ArrayList;

public class Adventurer {
    private int id;
    private String name;
    private ArrayList<Equipment> equipments;
    private double health = 100.0;
    private double exp;
    private double money;

    public ArrayList<Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(ArrayList<Equipment> equipments) {
        this.equipments = equipments;
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

    public void use(Equipment equipment) {
        try {
            equipment.usedBy(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
}
