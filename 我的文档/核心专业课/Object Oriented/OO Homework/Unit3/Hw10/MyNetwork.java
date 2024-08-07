import com.oocourse.spec2.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec2.exceptions.EqualGroupIdException;
import com.oocourse.spec2.exceptions.EqualMessageIdException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import com.oocourse.spec2.exceptions.MessageIdNotFoundException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class MyNetwork implements Network {
    private ArrayList<MyPerson> people = new ArrayList<>();
    private int countAncestor = 0;
    private int countTri = 0;
    private HashMap<Integer, MyGroup> groups = new HashMap<>();
    private HashMap<Integer, MyMessage> messages = new HashMap<>();

    public MyNetwork() {
    }

    @Override
    public boolean contains(int id) {
        for (MyPerson myPerson : people) {
            if (myPerson.getId() == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Person getPerson(int id) {
        if (contains(id)) {
            for (MyPerson person : people) {
                if (person.getId() == id) {
                    return person;
                }
            }
        }
        return null;
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (!contains(person.getId())) {
            people.add((MyPerson) person);
            countAncestor++;
        } else {
            throw new MyEqualPersonIdException(person.getId());
        }
    }

    public void updateTriSum(MyPerson person1, MyPerson person2, int op) {
        int id1 = person1.getId();
        int id2 = person2.getId();
        int less = person1.getAcquaintance().size() < person2.getAcquaintance().size()
                ? id1 : id2;
        int more = less == id1 ? id2 : id1;
        MyPerson me = (MyPerson) getPerson(less);
        for (Map.Entry<Integer, Person> entry : me.getAcquaintance().entrySet()) {
            Person other = getPerson(more);
            if (entry.getValue().equals(other)) {
                continue;
            }
            if (entry.getValue().isLinked(other)) {
                countTri += op;
            }
        }
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        MyPerson person1 = (MyPerson) getPerson(id1);
        MyPerson person2 = (MyPerson) getPerson(id2);
        if (contains(id1) && contains(id2) && !person1.isLinked(person2)) {
            if (!bfs(id1, id2)) {
                countAncestor--;
            }
            updateTriSum(person1, person2, 1);
            person1.getAcquaintance().put(id2, person2);
            person2.getAcquaintance().put(id1, person1);
            person1.getValue().put(id2, value);
            person2.getValue().put(id1, value);
            person1.updateBestAcquaintance(id2, value);
            person2.updateBestAcquaintance(id1, value);
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (contains(id1) && !contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (contains(id1) && contains(id2) && person1.isLinked(person2)) {
            throw new MyEqualRelationException(id1, id2);
        }
    }

    @Override
    //如果person1.queryValue(person2) + value > 0, 保存二者关系，并添加value
    //否则删除二者关系
    public void modifyRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualPersonIdException, RelationNotFoundException {
        MyPerson person1 = (MyPerson) getPerson(id1);
        MyPerson person2 = (MyPerson) getPerson(id2);
        if (contains(id1) && contains(id2) && id1 != id2
                && person1.isLinked(person2)) {
            HashMap<Integer, Integer> value1 = person1.getValue();
            HashMap<Integer, Integer> value2 = person2.getValue();
            if (person1.queryValue(person2) + value > 0) {
                //增加value
                value1.put(id2, value1.get(id2) + value);
                value2.put(id1, value2.get(id1) + value);
            } else {
                //维护三人组数量
                updateTriSum(person1, person2, -1);
                //删除二者关系
                person1.getAcquaintance().remove(id2);
                value1.remove(id2);
                person2.getAcquaintance().remove(id1);
                value2.remove(id1);
                if (id1 == 7 && id2 == 8) {
                    //System.out.println(bfs(id1, id2) + "!!!");
                }
                if (!bfs(id1, id2)) {
                    countAncestor++;
                }
            }
            person1.updateBestAcquaintance();
            person2.updateBestAcquaintance();
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (contains(id1) && !contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (id1 == id2) {
            throw new MyEqualPersonIdException(id1);
        } else if (contains(id1) && contains(id2) && id1 != id2
                && !person1.isLinked(person2)) {
            throw new MyRelationNotFoundException(id1, id2);
        }
    }

    public boolean bfs(int id1, int id2) {
        //bfs
        if (id1 == id2) {
            return true;
        }
        MyPerson person1 = (MyPerson) getPerson(id1);
        ArrayList<Integer> visited = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.addAll(person1.getAcquaintance().keySet());
        // 标记起点
        visited.add(id1);
        while (!queue.isEmpty()) {
            int id = queue.poll();
            if (!visited.contains(id)) {
                if (id == id2) {
                    return true;
                }
                visited.add(id);
                MyPerson tmp = (MyPerson) getPerson(id);
                queue.addAll(tmp.getAcquaintance().keySet());
            }
        }
        return false;
    }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        MyPerson person1 = (MyPerson) getPerson(id1);
        MyPerson person2 = (MyPerson) getPerson(id2);
        if (contains(id1) && contains(id2) && person1.isLinked(person2)) {
            return person1.queryValue(person2);
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (contains(id1) && !contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (contains(id1) && contains(id2) && !person1.isLinked(person2)) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        return 0;
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        // 判断两个人是否可达
        if (contains(id1) && contains(id2)) {
            return bfs(id1, id2);
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else {
            throw new MyPersonIdNotFoundException(id2);
        }
    }

    @Override
    public int queryBlockSum() {
        //统计能构建出多少个连通子图
        return countAncestor;
    }

    @Override
    public int queryTripleSum() {
        return countTri;
    }

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        if (!groups.containsKey(group.getId())) {
            groups.put(group.getId(), (MyGroup) group);
        } else {
            throw new MyEqualGroupIdException(group.getId());
        }
    }

    @Override
    public Group getGroup(int id) {
        if (groups.containsKey(id)) {
            return groups.get(id);
        }
        return null;
    }

    @Override
    public void addToGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (contains(id1) && groups.containsKey(id2)) {
            MyGroup group = (MyGroup) getGroup(id2);
            MyPerson person = (MyPerson) getPerson(id1);
            if (!group.hasPerson(person)) {
                group.addPerson(person);
            } else {
                throw new MyEqualPersonIdException(id1);
            }
        } else if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (groups.containsKey(id)) {
            return getGroup(id).getValueSum();
        } else {
            throw new MyGroupIdNotFoundException(id);
        }
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (groups.containsKey(id)) {
            return getGroup(id).getAgeVar();
        } else {
            throw new MyGroupIdNotFoundException(id);
        }
    }

    @Override
    public void delFromGroup(int id1, int id2) throws
            GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (contains(id1) && groups.containsKey(id2)) {
            MyGroup group = (MyGroup) getGroup(id2);
            MyPerson person = (MyPerson) getPerson(id1);
            if (group.hasPerson(person)) {
                group.delPerson(person);
            } else {
                throw new MyEqualPersonIdException(id1);
            }
        } else if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
    }

    @Override
    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }

    @Override
    public void addMessage(Message message) throws EqualMessageIdException, EqualPersonIdException {
        if (!containsMessage(message.getId())) {
            if (message.getType() == 0
                    && message.getPerson1().equals(message.getPerson2())) {
                throw new MyEqualPersonIdException(message.getPerson1().getId());
            }
            messages.put(message.getId(), (MyMessage) message);
        } else {
            throw new MyEqualMessageIdException(message.getId());
        }
    }

    @Override
    public Message getMessage(int id) {
        return messages.get(id);
    }

    @Override
    public void sendMessage(int id) throws
            RelationNotFoundException, MessageIdNotFoundException, PersonIdNotFoundException {
        if (containsMessage(id)) {
            MyMessage message = (MyMessage) getMessage(id);
            MyPerson person1 = (MyPerson) message.getPerson1();
            MyPerson person2 = (MyPerson) message.getPerson2();
            if (message.getType() == 0) {
                if (!person1.isLinked(person2)) {
                    throw new MyRelationNotFoundException(person1.getId(), person2.getId());
                }
                if (!person1.equals(person2)) {
                    person1.addSocialValue(message.getSocialValue());
                    person2.addSocialValue(message.getSocialValue());
                    messages.remove(message.getId());
                    person2.getMessages().add(0, message);
                }
            } else {
                if (!message.getGroup().hasPerson(person1)) {
                    throw new MyPersonIdNotFoundException(person1.getId());
                }
                MyGroup group = (MyGroup) message.getGroup();
                for (Map.Entry<Integer, MyPerson> entry : group.getPeople().entrySet()) {
                    entry.getValue().addSocialValue(message.getSocialValue());
                }
                messages.remove(message.getId());
            }
        } else {
            throw new MyMessageIdNotFoundException(id);
        }
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (contains(id)) {
            return getPerson(id).getSocialValue();
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (contains(id)) {
            return getPerson(id).getReceivedMessages();
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    @Override
    public int queryBestAcquaintance(int id) throws
            PersonIdNotFoundException, AcquaintanceNotFoundException {
        if (contains(id) && ((MyPerson) getPerson(id)).getAcquaintance().size() > 0) {
            MyPerson person = (MyPerson) getPerson(id);
            return person.getBestAcquaintance();
        } else if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            throw new MyAcquaintanceNotFoundException(id);
        }
    }

    @Override
    public int queryCoupleSum() {
        int sum = 0;
        for (int i = 0; i < people.size(); i++) {
            int id1 = people.get(i).getId();
            if (people.get(i).getAcquaintance().size() == 0) {
                continue;
            }
            //没有好朋友
            if (people.get(i).getBestValue() == 0) {
                continue;
            }
            try {
                if (queryBestAcquaintance(people.get(i).getBestAcquaintance()) == id1) {
                    //System.out.println("id1: " + id1);
                    //System.out.println("id2: " + people.get(i).getBestAcquaintance());
                    sum++;
                }
            } catch (PersonIdNotFoundException e) {
                throw new RuntimeException(e);
            } catch (AcquaintanceNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return sum / 2;
    }

    @Override
    public int modifyRelationOKTest(int id1, int id2, int value,
                                    HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                                    HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        return OkTest.okTest(id1, id2, value, beforeData, afterData);
    }
}
