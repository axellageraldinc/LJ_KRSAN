package pap.ta.lj_krsan.Model;

/**
 * Created by axellageraldinc on 17/11/17.
 */

public class User {
    private String id, email, username, password;

    public User() {
    }

    public User(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public User(String id, String email,  String username) {
        this.id = id;
        this.email = email;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
