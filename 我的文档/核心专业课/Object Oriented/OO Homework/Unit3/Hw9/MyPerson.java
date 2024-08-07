import com.oocourse.spec1.main.Person;

import java.util.HashMap;
import java.util.Map;

public class MyPerson implements Person {
    private int id;
    private int age;
    private String name;
    private HashMap<Integer, Person> acquaintance; // id + Person
    private HashMap<Integer, Integer> value; // id + Value

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getAge() {
        return this.age;
    }

    public HashMap<Integer, Person> getAcquaintance() {
        return acquaintance;
    }

    public HashMap<Integer, Integer> getValue() {
        return value;
    }

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.acquaintance = new HashMap<>();
        this.value = new HashMap<>();
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof MyPerson) {
            return this.id == ((MyPerson) obj).getId();
        } else {
            return false;
        }
    }

    @Override
    public boolean isLinked(Person person) {
        if (person.equals(this)) {
            return true;
        }
        for (Map.Entry<Integer, Person> entry : acquaintance.entrySet()) {
            if (entry.getValue().equals(person)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int queryValue(Person person) {
        for (Map.Entry<Integer, Person> entry : acquaintance.entrySet()) {
            if (entry.getValue().equals(person)) {
                return value.get(entry.getKey());
            }
        }
        return 0;
    }

    @Override
    public int compareTo(Person o) {
        return this.name.compareTo(o.getName());
    }
}
