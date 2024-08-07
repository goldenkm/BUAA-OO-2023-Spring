import java.math.BigInteger;

public class EpicSword extends Sword {
    private double evolveRatio;

    public EpicSword(int id, String name, BigInteger price, double sharpness, double evolveRatio) {
        super(id, name, price, sharpness);
        this.evolveRatio = evolveRatio;
    }

    public EpicSword() {
    }

    public EpicSword(int id, String name, BigInteger price, double sharpness) {
        super(id, name, price, sharpness);
    }

    public double getEvolveRatio() {
        return evolveRatio;
    }

    public void setEvolveRatio(double evolveRatio) {
        this.evolveRatio = evolveRatio;
    }

    @Override
    public String toString() {
        return "The epicSword's id is " + super.getId()
                + ", name is " + super.getName()
                + ", sharpness is " + super.getSharpness()
                + ", evolveRatio is " + evolveRatio
                + ".";
    }

    public void usedBy(Adventurer user) {
        super.usedBy(user);
        this.setSharpness(getSharpness() * evolveRatio);
        System.out.println(this.getName() +
                "'s sharpness became "
                + this.getSharpness()
                + ".");
    }
}
