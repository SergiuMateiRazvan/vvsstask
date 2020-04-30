package serenity.model;

public class User {
    private String username;
    private String password;
    private String userNickName;
    private String wrongPassword;

    public User(String username, String password, String userNickName, String wrongPassword) {
        this.username = username;
        this.password = password;
        this.userNickName = userNickName;
        this.wrongPassword = wrongPassword;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getWrongPassword() {
        return wrongPassword;
    }

    public void setWrongPassword(String wrongPassword) {
        this.wrongPassword = wrongPassword;
    }
}
