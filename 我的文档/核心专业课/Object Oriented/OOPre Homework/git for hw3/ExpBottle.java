import java.math.BigInteger;

public class ExpBottle extends Bottle {
    private double expRatio;

    public ExpBottle(int id, String name, BigInteger price,
                     double capacity, boolean filled, double expRatio) {
        super(id, name, price, capacity, filled);
        this.expRatio = expRatio;
    }

    public ExpBottle() {
    }

    public double getExpRatio() {
        return expRatio;
    }

    public void setExpRatio(double expRatio) {
        this.expRatio = expRatio;
    }

    @Override
    public String toString() {
        return "The expBottle's id is " + super.getId()
                + ", name is " + super.getName()
                + ", capacity is " + super.getCapacity()
                + ", filled is " + super.isFilled()
                + ", expRatio is " + getExpRatio()
                + ".";
    }

    @Override
    public void usedBy(Adventurer user) {
        if (!this.isFilled()) {
            System.out.println("Failed to use " + getName() + " because it is empty.");
        } else {
            super.usedBy(user);
            double exp = user.getExp() * expRatio;
            user.setExp(exp);
            System.out.println(user.getName() +
                    "'s exp became "
                    + exp
                    + ".");
        }
    }
}
