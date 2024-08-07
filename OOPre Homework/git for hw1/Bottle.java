public class Bottle {
    private int id;
    private String name;
    private long price;
    private double capacity;
    private boolean filled;

    public Bottle() {
    }

    public Bottle(int id, String name, long price, double capacity, boolean filled) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.capacity = capacity;
        this.filled = filled;
    }

    /**
     * 获取
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * 设置
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 获取
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取
     *
     * @return price
     */
    public long getPrice() {
        return price;
    }

    /**
     * 设置
     *
     * @param price
     */
    public void setPrice(long price) {
        this.price = price;
    }

    /**
     * 获取
     *
     * @return capacity
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

    public String toString() {
        return "Bottle{id = " + id + ", name = " + name + ", price = " + price + ", capacity = " + capacity + ", filled = " + filled + "}";
    }
}
