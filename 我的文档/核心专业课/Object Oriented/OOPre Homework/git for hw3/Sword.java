import java.math.BigInteger;

public class Sword extends Equipment {
    private double sharpness;

    public Sword(int id, String name, BigInteger price, double sharpness) {
        super(id, name, price);
        this.sharpness = sharpness;
    }

    public double getSharpness() {
        return sharpness;
    }

    public void setSharpness(double sharpness) {
        this.sharpness = sharpness;
    }

    @Override
    public String toString() {
        return "The sword's id is " + super.getId()
                + ", name is " + super.getName()
                + ", sharpness is " + getSharpness()
                + ".";
    }

    public Sword() {
    }

    @Override
    public void usedBy(Adventurer user) {
        super.usedBy(user);
        double health = user.getHealth();
        health -= 10.0;
        double exp = user.getExp();
        exp += 10.0;
        double money = user.getMoney();
        money += sharpness;
        user.setHealth(health);
        user.setExp(exp);
        user.setMoney(money);
        System.out.println(user.getName() +
                " used "
                + getName() + " and earned "
                + sharpness
                + ".");
    }
}
