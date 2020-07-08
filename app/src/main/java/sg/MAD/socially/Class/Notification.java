package sg.MAD.socially.Class;

import java.util.Calendar;

public class Notification {
    String Info;
    Calendar Time;

    public Notification(){}

    public Notification(String info, Calendar time) {
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
}
