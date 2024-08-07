import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyNetwork implements Network {
    private ArrayList<MyPerson> people = new ArrayList<>();
    private UnionFind finder = new UnionFind();
    private int countAncestor = 0;
    private int countTri = 0;

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
            finder.addNode(person.getId());
            countAncestor++;
        } else {
            throw new MyEqualPersonIdException(person.getId());
        }
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        MyPerson person1 = (MyPerson) getPerson(id1);
        MyPerson person2 = (MyPerson) getPerson(id2);
        if (contains(id1) && contains(id2) && !person1.isLinked(person2)) {
            int less = person1.getAcquaintance().size() < person2.getAcquaintance().size()
                    ? id1 : id2;
            int more = less == id1 ? id2 : id1;
            MyPerson person = (MyPerson) getPerson(less);
            for (Map.Entry<Integer, Person> entry : person.getAcquaintance().entrySet()) {
                if (entry.getValue().isLinked(getPerson(more))) {
                    countTri++;
                }
            }
            if (finder.find(id1) != finder.find(id2)) {
                finder.merge(id1, id2);
                countAncestor--;
            }
            person1.getAcquaintance().put(id2, person2);
            person2.getAcquaintance().put(id1, person1);
            person1.getValue().put(id2, value);
            person2.getValue().put(id1, value);
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (contains(id1) && !contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (contains(id1) && contains(id2) && person1.isLinked(person2)) {
            throw new MyEqualRelationException(id1, id2);
        }
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
            if (id1 == id2) {
                return true;
            }
            return finder.find(id1) == finder.find(id2);
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
    public boolean queryTripleSumOKTest(HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                                        HashMap<Integer, HashMap<Integer, Integer>> afterData,
                                        int result) {
        for (Integer id : beforeData.keySet()) {
            if (!afterData.containsKey(id)) {
                return false;
            }
            if (!isSameAcquaintance(beforeData.get(id), afterData.get(id))) {
                return false;
            }
            MyPerson myPerson = new MyPerson(id, "", 10);
            try {
                addPerson(myPerson);
            } catch (EqualPersonIdException e) {
                throw new RuntimeException(e);
            }
        }
        for (Integer id : beforeData.keySet()) {
            for (Integer acquaintance : beforeData.get(id).keySet()) {
                try {
                    MyPerson person1 = (MyPerson) getPerson(id);
                    MyPerson person2 = (MyPerson) getPerson(acquaintance);
                    if (!person1.isLinked(person2)) {
                        addRelation(id, acquaintance, 0);
                    }
                } catch (PersonIdNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (EqualRelationException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return result == queryTripleSum();
    }

    public boolean isSameAcquaintance(HashMap<Integer, Integer> map1,
                                      HashMap<Integer, Integer> map2) {
        for (Integer id : map1.keySet()) {
            if (!map2.containsKey(id)) {
                return false;
            }
            if (map1.get(id) != map2.get(id)) {
                return false;
            }
        }
        return true;
    }
}
