public class Emoji implements Comparable<Emoji> {
    private String name;
    private String emojiId;
    private int count;

    public Emoji() {
    }

    public Emoji(String name, String emojiId, int count) {
        this.name = name;
        this.emojiId = emojiId;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmojiId() {
        return emojiId;
    }

    public void setEmojiId(String emojiId) {
        this.emojiId = emojiId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int compareTo(Emoji other) {
        if (this.count == other.count) {
            return this.name.compareTo(other.name);
        } else {
            return other.count - this.count;
        }
    }
}
