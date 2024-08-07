import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyPerson implements Person {
    private int id;
    private int age;
    private String name;
    private HashMap<Integer, Person> acquaintance; // id + Person
    private HashMap<Integer, Integer> value; // id + Value
    private int socialValue;
    private ArrayList<Message> messages;
    private int bestAcquaintance;
    private int bestValue;

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
        this.messages = new ArrayList<>();
        this.bestAcquaintance = 0;
        this.bestValue = 0;
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
    public void addSocialValue(int num) {
        this.socialValue += num;
    }

    @Override
    public int getSocialValue() {
        return this.socialValue;
    }

    @Override
    public ArrayList<Message> getMessages() {
        return this.messages;
    }

    @Override
    // ???
    public ArrayList<Message> getReceivedMessages() {
        ArrayList<Message> receivedMessages = new ArrayList<>();
        for (int i = 0; i < messages.size() && i < 5; i++) {
            receivedMessages.add(messages.get(i));
        }
        return receivedMessages;
    }

    public int getBestAcquaintance() {
        return bestAcquaintance;
    }

    public int getBestValue() {
        return bestValue;
    }

    public void updateBestAcquaintance(int acquaintance, int value) {
        if (value > bestValue) {
            bestValue = value;
            bestAcquaintance = acquaintance;
        } else if (value == bestValue) {
            //当value相同时取id小的
            if (acquaintance < bestAcquaintance) {
                bestAcquaintance = acquaintance;
            }
        }
    }

    public void updateBestAcquaintance() {
        int max = 0;
        if (acquaintance.size() == 0) {
            bestAcquaintance = 0;
            bestValue = 0;
        }
        for (Map.Entry<Integer, Integer> entry : value.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                bestAcquaintance = entry.getKey();
                bestValue = max;
            } else if (entry.getValue() == max) {
                if (entry.getKey() < bestAcquaintance) {
                    bestAcquaintance = entry.getKey();
                }
            }
        }
    }

    @Override
    public int compareTo(Person o) {
        return this.name.compareTo(o.getName());
    }
}
