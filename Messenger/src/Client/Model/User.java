package Client.Model;

import javax.swing.*;
import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 2308362320199130613L;
    private String userName;
    private Icon icon;
    private int status;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}


