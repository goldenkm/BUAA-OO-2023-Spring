import java.math.BigInteger;

public class Bottle extends Equipment {
    private double capacity;
    private boolean filled;

    public Bottle() {
    }

    public Bottle(int id, String name, BigInteger price, double capacity, boolean filled) {
        super(id, name, price);
        this.capacity = capacity;
        this.filled = filled;
    }

    /**
     * 获取
     *
     * @return id
     */

    public double getCapacity() {
        return capacity;
    }

    /**
     * 设置
     *
     * @param capacity
     */
    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    /**
     * 获取
     *
     * @return filled
     */
    public boolean isFilled() {
        return filled;
    }

    /**
     * 设置
     *
     * @param filled
     */
    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    @Override
    public String toString() {
        return "The bottle's id is " + getId() +
                ", name is " + getName() +
                ", capacity is " + capacity +
                ", filled is " + filled + ".";
    }

    @Override

    public void usedBy(Adventurer user) throws Exception {
        if (!isFilled()) {
            throw new Exception("Failed to use " + getName() + " because it is empty.");
        }
        user.setHealth(user.getHealth() + capacity / 10);
        setFilled(false);
        setPrice(getPrice().divide(BigInteger.TEN));

        System.out.println(user.getName() +
                " drank " + getName() +
                " and recovered " + capacity / 10 +
                ".");
    }
}
