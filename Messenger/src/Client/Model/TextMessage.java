package Client.Model;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;

public class TextMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    private String message;
    private Icon icon;
    private User sender;
    private ArrayList<User> recievers = new ArrayList<>();
    private String timeSent;
    private String timeRecieved;



    public TextMessage (String message, Icon icon) {
        this.message = message;
        this.icon = icon;


    }
    public TextMessage (String message) {
        this.message = message;

    }

    public TextMessage (Icon icon) {
        this.icon = icon;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public ArrayList<User> getRecievers() {
        return recievers;
    }

    public void setRecievers(ArrayList<User> recievers) {
        this.recievers = recievers;
    }

    public void addReciever(User user) {
        recievers.add(user);
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }

    public String getTimeRecieved() {
        return timeRecieved;
    }

    public void setTimeRecieved(String timeRecieved) {
        this.timeRecieved = timeRecieved;
    }

}
