package sg.MAD.socially.Notifications;

public class Data {
    private String sender;
    private String body;
    private String title;
    private String receiver;
    private String type;

    public Data(String sender, String body, String title, String receiver, String type) {
        this.sender = sender;
        this.body = body;
        this.title = title;
        this.receiver = receiver;
        this.type = type;
    }

    public Data() {

    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

