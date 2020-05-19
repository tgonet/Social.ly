package sg.MAD.socially;

public class User {

    String Id;
    String Username;
    String ImageURL;
    String NickName;
    String DOB;
    String Name;

    public User(String id, String username, String imageURL, String nickName, String DOB, String name) {
        Id = id;
        Username = username;
        ImageURL = imageURL;
        NickName = nickName;
        this.DOB = DOB;
        Name = name;
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

    public User(){}

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

}