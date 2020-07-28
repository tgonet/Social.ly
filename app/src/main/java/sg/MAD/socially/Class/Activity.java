package sg.MAD.socially.Class;


import java.io.Serializable;

public class Activity implements Serializable {

    private String Interest;
    private String name_register;
    private String act_date;
    private String act_desc;
    private String act_location;
    private String act_picture;
    private String act_time;
    private String activity_name;
    private String activityid;
    private String host_id;
    private String profile_image;
    private boolean permission;

    public Activity() {
    }

    public Activity(String Interest, String name_register, String act_date, String act_desc, String act_location, String act_picture, String act_time, String activity_name, String activityid, String host_id, String profile_image, boolean permission) {
        this.Interest = Interest;
        this.name_register = name_register;
        this.act_date = act_date;
        this.act_desc = act_desc;
        this.act_location = act_location;
        this.act_picture = act_picture;
        this.act_time = act_time;
        this.activity_name = activity_name;
        this.activityid = activityid;
        this.host_id = host_id;
        this.profile_image = profile_image;
        this.permission = permission;
    }


    public String getInterest() {
        return Interest;
    }

    public void setInterest(String interest) {
        Interest = interest;
    }

    public String getName_register() {
        return name_register;
    }

    public void setName_register(String name_register) {
        this.name_register = name_register;
    }

    public String getAct_date() {
        return act_date;
    }

    public void setAct_date(String act_date) {
        this.act_date = act_date;
    }

    public String getAct_desc() {
        return act_desc;
    }

    public void setAct_desc(String act_desc) {
        this.act_desc = act_desc;
    }

    public String getAct_location() {
        return act_location;
    }

    public void setAct_location(String act_location) {
        this.act_location = act_location;
    }

    public String getAct_picture() {
        return act_picture;
    }

    public void setAct_picture(String act_picture) {
        this.act_picture = act_picture;
    }

    public String getAct_time() {
        return act_time;
    }

    public void setAct_time(String act_time) {
        this.act_time = act_time;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public String getActivityid() {
        return activityid;
    }

    public void setActivityid(String activityid) {
        this.activityid = activityid;
    }

    public String getHost_id() {
        return host_id;
    }

    public void setHost_id(String host_id) {
        this.host_id = host_id;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public boolean isPermission() {
        return permission;
    }

    public void setPermission(boolean permission) {
        this.permission = permission;
    }
}