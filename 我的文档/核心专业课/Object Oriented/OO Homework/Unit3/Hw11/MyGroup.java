import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

import java.util.HashMap;
import java.util.Map;

public class MyGroup implements Group {
    private int id;
    private HashMap<Integer, MyPerson> people;
    private int ageSum;
    private int agePowSum;

    public MyGroup(int id) {
        this.id = id;
        this.people = new HashMap<>();
        ageSum = 0;
        agePowSum = 0;
    }

    public int getId() {
        return this.id;
    }

    public HashMap<Integer, MyPerson> getPeople() {
        return this.people;
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Group) {
            return ((Group) obj).getId() == this.id;
        }
        return false;
    }

    @Override
    public void addPerson(Person person) {
        if (people.size() <= 1111) {
            people.put(person.getId(), (MyPerson) person);
            ageSum += person.getAge();
            agePowSum += person.getAge() * person.getAge();
        }
    }

    @Override
    public boolean hasPerson(Person person) {
        return people.containsKey(person.getId());
    }

    @Override
    public int getValueSum() {
        int sum = 0;
        for (Map.Entry<Integer, MyPerson> entry1 : people.entrySet()) {
            MyPerson person1 = entry1.getValue();
            for (Map.Entry<Integer, MyPerson> entry2 : people.entrySet()) {
                MyPerson person2 = entry2.getValue();
                if (person1.isLinked(person2)) {
                    sum += person1.queryValue(person2);
                }
            }
        }
        return sum;
    }

    @Override
    //求平均数
    public int getAgeMean() {
        if (people.size() == 0) {
            return 0;
        }
        return ageSum / people.size();
    }

    @Override
    //求方差
    public int getAgeVar() {
        if (people.size() == 0) {
            return 0;
        }
        int average = getAgeMean();
        int n = getSize();
        int tmp = agePowSum - 2 * average * ageSum + n * average * average;
        return tmp / n;
    }

    @Override
    public void delPerson(Person person) {
        if (hasPerson(person)) {
            people.remove(person.getId());
            ageSum -= person.getAge();
            agePowSum -= person.getAge() * person.getAge();
        }
    }

    @Override
    public int getSize() {
        return people.size();
    }
}
