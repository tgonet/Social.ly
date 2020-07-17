package sg.MAD.socially.Class;

import java.util.Calendar;

public class NotificationFriend {
    String ImageURL;
    String Info;
    Calendar Time;

    public NotificationFriend(){}

    public NotificationFriend(String imageURL, String info, Calendar time) {
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

    public Calendar getTime() {
        return Time;
    }

    public void setTime(Calendar time) {
        Time = time;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }
}
