package sg.MAD.socially;

public class Activity {
    private String act_picture;
    private String profile_image;
    private String Name_register;
    private String act_location;
    private String act_time;
    private String activity_name;
    private String act_desc;
    private String act_date;
    private boolean permission;

    public Activity() {
    }

    public Activity(String act_picture, String profile_image, String name_register, String act_location, String act_time, String activity_name, String act_desc, String act_date, boolean permission) {
        this.act_picture = act_picture;
        this.profile_image = profile_image;
        Name_register = name_register;
        this.act_location = act_location;
        this.act_time = act_time;
        this.activity_name = activity_name;
        this.act_desc = act_desc;
        this.act_date = act_date;
        this.permission = permission;
    }

    public String getAct_picture() {
        return act_picture;
    }

    public void setAct_picture(String act_picture) {
        this.act_picture = act_picture;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getName_register() {
        return Name_register;
    }

    public void setName_register(String name_register) {
        Name_register = name_register;
    }

    public String getAct_location() {
        return act_location;
    }

    public void setAct_location(String act_location) {
        this.act_location = act_location;
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

    public String getAct_desc() {
        return act_desc;
    }

    public void setAct_desc(String act_desc) {
        this.act_desc = act_desc;
    }

    public String getAct_date() {
        return act_date;
    }

    public void setAct_date(String act_date) {
        this.act_date = act_date;
    }

    public boolean getPermission() {
        return permission;
    }

    public void setPermission(boolean permission) {
        this.permission = permission;
    }
}
