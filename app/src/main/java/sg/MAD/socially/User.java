package sg.MAD.socially;

public class User {

    String Id;
    String ImageURL;
    String NickName;
    String DOB;
    String Name;
    String ShortDesc;
    String Email;
    String Friends;


    public User(String id, String username, String imageURL, String nickName, String DOB,
                String name, String shortDesc, String email, String friends) {
        Id = id;
        ImageURL = imageURL;
        NickName = nickName;
        this.DOB = DOB;
        Name = name;
        ShortDesc = shortDesc;
    }
    public String getShortDesc() {
        return ShortDesc;
    }

    public void setShortDesc(String shortDesc) {
        ShortDesc = shortDesc;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getShortDesc() {
        return ShortDesc;
    }

    public void setShortDesc(String shortDesc) {
        ShortDesc = shortDesc;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFriends() {
        return Friends;
    }

    public void setFriends(String friends) {
        Friends = friends;
    }
}