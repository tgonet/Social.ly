package sg.MAD.socially.Class;

public class Texts {
    private CharSequence message;
    private long timestamp;
    private CharSequence sender;
    public Texts(CharSequence text, CharSequence sender) {
        this.message = text;
        this.sender = sender;
        timestamp = System.currentTimeMillis();
    }
    public CharSequence getText() {
        return message;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public CharSequence getSender() {
        return sender;
    }
}