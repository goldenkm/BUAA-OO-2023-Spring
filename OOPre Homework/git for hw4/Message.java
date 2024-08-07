public class Message {
    private String message;
    private String date;
    private String sender;
    private String receiver;

    public Message(String message, String date, String sender, String receiver) {
        this.message = message;
        this.date = date;
        this.sender = sender;
        this.receiver = receiver;
    }

    public Message(String message, String date, String sender) {
        this.message = message;
        this.date = date;
        this.sender = sender;
    }

    public Message() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
