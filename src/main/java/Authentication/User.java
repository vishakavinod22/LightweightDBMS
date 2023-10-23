package Authentication;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


public class User {

    private String username;
    private String password;

    //Constructors
    public User() {
    }

    public User(Integer user_id, String username, String password) {
        this.username = username;
        this.password = password;
    }

    //Getters and Setters
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
