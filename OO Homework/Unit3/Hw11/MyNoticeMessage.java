import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.Person;

public class MyNoticeMessage extends MyMessage implements NoticeMessage {
    private String string;

    public MyNoticeMessage(int id, String noticeString,
                           Person messagePerson1, Person messagePerson2) {
        super(id, noticeString.length(), messagePerson1, messagePerson2);
        this.string = noticeString;
    }

    public MyNoticeMessage(int id, String noticeString, Person messagePerson1, Group messageGroup) {
        super(id, noticeString.length(), messagePerson1, messageGroup);
        this.string = noticeString;
    }

    @Override
    public String getString() {
        return string;
    }
}
