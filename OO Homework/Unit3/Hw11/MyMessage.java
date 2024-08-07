import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;

public class MyMessage implements Message {
    private int id;
    private int socialValue;
    private int type;   //0：发给个人；1：群发
    private MyPerson person1;
    private MyPerson person2;
    private MyGroup group;

    public MyMessage(int id, int socialValue, Person messagePerson1, Person messagePerson2) {
        this.id = id;
        this.type = 0;
        this.socialValue = socialValue;
        this.person1 = (MyPerson) messagePerson1;
        this.person2 = (MyPerson) messagePerson2;
    }

    public MyMessage(int id, int socialValue, Person messagePerson1, Group messageGroup) {
        this.id = id;
        this.type = 1;
        this.socialValue = socialValue;
        this.person1 = (MyPerson) messagePerson1;
        this.group = (MyGroup) messageGroup;
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public int getSocialValue() {
        return this.socialValue;
    }

    @Override
    public Person getPerson1() {
        return this.person1;
    }

    @Override
    public Person getPerson2() {
        return this.person2;
    }

    @Override
    public Group getGroup() {
        return this.group;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Message) {
            return this.id == ((Message) obj).getId();
        }
        return false;
    }
}
