import java.math.BigInteger;

public class RareSword extends Sword {
    private double extraExpBonus;

    public RareSword(int id, String name, BigInteger price, double sharpness,
                     double extraExpBonus) {
        super(id, name, price, sharpness);
        this.extraExpBonus = extraExpBonus;
    }

    public RareSword() {

    }

    public RareSword(int id, String name, BigInteger price, double sharpness) {
        super(id, name, price, sharpness);
    }

    public double getExtraExpBonus() {
        return extraExpBonus;
    }

    public void setExtraExpBonus(double extraExpBonus) {
        this.extraExpBonus = extraExpBonus;
    }

    @Override
    public String toString() {
        return "The rareSword's id is " + super.getId()
                + ", name is " + super.getName()
                + ", sharpness is " + super.getSharpness()
                + ", extraExpBonus is " + extraExpBonus
                + ".";
    }

    @Override
    public void usedBy(Adventurer user) {
        super.usedBy(user);
        user.setExp(user.getExp() + extraExpBonus);
        System.out.println(user.getName() +
                " got extra exp "
                + extraExpBonus
                + ".");
    }
}
