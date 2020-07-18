package sg.MAD.socially.Class;

public class NotificationFriend {
    String ImageURL;
    String Info;
    String Time;

    public NotificationFriend(){}

    public NotificationFriend(String imageURL, String info, String time) {
        ImageURL = imageURL;
        Info = info;
        Time = time;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }
}
