package Client.Model;

import javax.swing.*;
import java.io.Serializable;

public class User implements Serializable {

    private String userName;
    private Icon icon;

    public User(String username) {
        this.userName = username;

    }

    public User(String username, Icon icon) {
        this.userName = username;
        this.icon = icon;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }
}


