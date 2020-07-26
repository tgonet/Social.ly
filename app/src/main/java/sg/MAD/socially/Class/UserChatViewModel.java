package sg.MAD.socially.Class;

public class UserChatViewModel implements Comparable<UserChatViewModel>  {
    public User user;
    public long timestamp;
    public String message;

    public UserChatViewModel(User user, long timestamp, String message) {
        this.user = user;
        this.timestamp = timestamp;
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public int compareTo(UserChatViewModel o) {
        return Long.compare(o.getTimestamp(),getTimestamp());
    }
}
