package Client.Model;

import javax.swing.*;
import java.io.Serializable;

/**
 * User class implements Serializable
 * @author Patrik Brandell
 */

public class User implements Serializable {

    private static final long serialVersionUID = 2308362320199130613L;
    private String userName;
    private Icon icon;
    private int status;


    /**
     * Create new user Object with only username
     * @param username - String username
     */
    public User(String username) {
        this.userName = username;

    }

    /**
     * Create new user Object with username and icon
     * @param username - String username
     * @param icon - Icon
     */
    public User(String username, Icon icon) {
        this.userName = username;
        this.icon = icon;
    }

    /**
     *
     * @return String username
     */
    public String getUsername() {
        return userName;
    }

    /**
     *
     * @return - profilepicture Icon
     */

    public Icon getIcon() {
        return icon;
    }


    /**
     *
     * @return int connected status
     */
    public int getStatus() {
        return status;
    }

    /**
     *
     * @param status - Set int status of connection
     */
    public void setStatus(int status) {
        this.status = status;
    }
}


