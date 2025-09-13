package DAOstuff.user;

public class User {

    private int userId;
    private String username;
    private String password;
    private String type;

    public User(int userId, String username, String password, String type) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getType() {
        return type;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setType(String type) {
        this.type = type;
    }
}
