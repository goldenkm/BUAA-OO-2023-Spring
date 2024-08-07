import java.math.BigInteger;

public class HealingPotion extends Bottle {
    private double efficiency;

    public HealingPotion(int id, String name, BigInteger price,
                         double capacity, boolean filled, double efficiency) {
        super(id, name, price, capacity, filled);
        this.efficiency = efficiency;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }

    @Override
    public String toString() {
        return "The healingPotion's id is " + super.getId()
                + ", name is " + super.getName()
                + ", capacity is " + super.getCapacity()
                + ", filled is " + super.isFilled()
                + ", efficiency is " + getEfficiency()
                + ".";
    }

    @Override
    public void usedBy(Adventurer user) throws Exception {
        if (!isFilled()) {
            throw new Exception("Failed to use " + getName() + " because it is empty.");
        }
        super.usedBy(user);
        double extraHealth = super.getCapacity() * this.efficiency;
        double health = user.getHealth();
        health += extraHealth;
        user.setHealth(health);
        System.out.println(user.getName() +
                " recovered extra "
                + extraHealth
                + ".");
    }
}
