package Client.Model;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * TextMessage Class implements Serializable
 * @author Patrik Brandell
 */
public class TextMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    private String message;
    private Icon icon;
    private User sender;
    private ArrayList<User> recievers = new ArrayList<>();
    private String timeSent;
    private String timeRecieved;


    /**
     * New textmessage with 3 params
     * @param message - String textmessage
     * @param icon - Icon picture
     * @param sender - User object as sender
     */
    public TextMessage (String message, Icon icon, User sender) {
        this.message = message;
        this.icon = icon;
        this.sender = sender;


    }

    /**
     * New textmessage with 2 params if only text
     * @param message - String textmessage
     * @param sender - User object as sender
     */
    public TextMessage (String message, User sender) {
        this.message = message;
        this.sender = sender;

    }

    /**
     * new textmessage with 2 params if only icon
     * @param icon - Icon picture
     * @param sender - User object as sender
     */
    public TextMessage (Icon icon, User sender) {
        this.icon = icon;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    /**
     *
     * @return get current icon
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     *
     * @return get current sender
     */
    public User getSender() {
        return sender;
    }


    /**
     *
     * @return - ArrayList with User objects
     */
    public ArrayList<User> getRecievers() {
        return recievers;
    }

    /**
     * clear list of duplicate names
     */
    public void removeDuplicateRecievers() {
        for (int i = 0; i < recievers.size()-1; i++) {
            for (int j = i+1; j < recievers.size(); j++) {
                if (recievers.get(i).getUsername().equals(recievers.get(j).getUsername())) {
                    removeReciever(j);
                }
            }
        }
    }

    /**
     *
     * @param user - Add single User object to recieverlist
     */
    public void addReciever(User user) {
        recievers.add(user);
    }

    /**
     * Remove single reciever
     * @param index in array
     */
    public void removeReciever (int index) {
        recievers.remove(index);
    }

    /**
     *
     * @return String time sent
     */
    public String getTimeSent() {
        return timeSent;
    }

    /**
     *
     * @param timeSent - set time sent
     */
    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }

    /**
     *
     * @return - String of time recieved
     */
    public String getTimeRecieved() {
        return timeRecieved;
    }

    /**
     *
     * @param timeRecieved - set Timerecieved as String
     */
    public void setTimeRecieved(String timeRecieved) {
        this.timeRecieved = timeRecieved;
    }

    @Override
    public String toString() {
        String text = timeSent +  '\''  +
                " Sender: " + sender + " Message: " + message +
                " Reciever: " + getRecievers().toString();

        return text;
    }
}
