import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;

public class MyRedEnvelopeMessage extends MyMessage implements RedEnvelopeMessage {
    private int money = 0;

    public MyRedEnvelopeMessage(int id, int luckyMoney,
                                Person messagePerson1, Person messagePerson2) {
        super(id, 5 * luckyMoney, messagePerson1, messagePerson2);
        this.money = luckyMoney;
    }

    public MyRedEnvelopeMessage(int id, int luckyMoney, Person messagePerson1, Group messageGroup) {
        super(id, 5 * luckyMoney, messagePerson1, messageGroup);
        this.money = luckyMoney;
    }

    @Override
    public int getMoney() {
        return this.money;
    }
}
