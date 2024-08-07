import java.util.ArrayList;

public class Json {
    private String downloadTime;
    private String createdAt;
    private String id;
    private String fullText;
    private String userId;
    private int retweetCount;
    private int favoriteCount;
    private int replyCount;
    private boolean possiblySensitiveEditable;
    private String lang;
    private ArrayList<Emoji> emojis;

    public Json() {
    }

    public String getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(String downloadTime) {
        this.downloadTime = downloadTime;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public boolean isPossiblySensitiveEditable() {
        return possiblySensitiveEditable;
    }

    public void setPossiblySensitiveEditable(boolean possiblySensitiveEditable) {
        this.possiblySensitiveEditable = possiblySensitiveEditable;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public ArrayList<Emoji> getEmojis() {
        return emojis;
    }

    public void setEmojis(ArrayList<Emoji> emojis) {
        this.emojis = emojis;
    }
}
