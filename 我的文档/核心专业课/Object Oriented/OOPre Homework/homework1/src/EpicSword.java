import java.math.BigInteger;

public class EpicSword extends Sword {
    private double evolveRatio;

    public EpicSword(int id, String name, BigInteger price, double sharpness, double evolveRatio) {
        super(id, name, price, sharpness);
        this.evolveRatio = evolveRatio;
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

    public void usedBy(Adventurer user) throws Exception {
        super.usedBy(user);
        setSharpness(getSharpness() * evolveRatio);
        System.out.println(getName() +
                "'s sharpness became "
                + getSharpness()
                + ".");
    }
}
