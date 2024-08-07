import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

public class MyEmojiMessage extends MyMessage implements EmojiMessage {

    private int emojiId;

    public MyEmojiMessage(int id, int emojiNumber, Person messagePerson1, Person messagePerson2) {
        super(id, emojiNumber, messagePerson1, messagePerson2);
        this.emojiId = emojiNumber;
    }

    public MyEmojiMessage(int id, int emojiNumber, Person messagePerson1, Group messageGroup) {
        super(id, emojiNumber, messagePerson1, messageGroup);
        this.emojiId = emojiNumber;
    }

    @Override
    public int getEmojiId() {
        return emojiId;
    }
}
