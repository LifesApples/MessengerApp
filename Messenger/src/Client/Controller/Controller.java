package Client.Controller;

import Client.Model.Client;
import Client.Model.Contacts;
import Client.Model.TextMessage;
import Server.Model.Server;
import Client.Model.User;
import Client.View.LoginPage;
import Client.View.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Controller {
    private MainFrame mainFrame;
    private LoginPage lp;
    private Server server;
    private Client client;
    private User myUser, contactUser;


    public Controller() throws IOException {

        //server = new Server(2343);
        lp = new LoginPage(this);
        mainFrame = new MainFrame( 600,600, this);

    }

    public void signIn() {
        client = new Client("127.0.0.1",2343, myUser, this);
        System.out.println("client");

        System.out.println("MF started");
        setUpContactsGUI();
        mainFrame.contactPanelBorder(myUser.getUsername());


    }


    public void setUpContactsGUI() {

        String[] contacts = new String[client.getContacts().getContactlist().size()];
        for (int i = 0; i < contacts.length; i++) {
            contacts[i] =  client.getContacts().getContactlist().get(i).getUsername();;

        }
        mainFrame.setContacts(contacts);
    }

    public void setUpOnlineUsersGUI (ArrayList<User> users) {

        String[] onlineUsers = new String[users.size()];
        for (int i = 0; i < onlineUsers.length; i++) {
            onlineUsers[i] = users.get(i).getUsername();
            System.out.println("Next User: " + onlineUsers[i]);
        }
        mainFrame.setOnlineUsers(onlineUsers);
    }


    public void setProfile() {
        mainFrame.setProfileGUI(myUser.getUsername(), myUser.getIcon() );
    }

    public void setContactProfile(User user) {
        if (user.getIcon() != null) {
            mainFrame.setContactProfileGUI(user.getUsername(), user.getIcon());
        }
    }

    public void openChatWindow(ArrayList<String> recievers) {
        mainFrame.openChatwindow(myUser.getUsername(), recievers);
        setProfile();
        if (recievers.size() == 1) {
            for (User u : client.getOnlineUsers()) {
                if (recievers.get(0).equals(u.getUsername())) {
                    setContactProfile(u);
                }
            }
        }

    }

    /**
     * Send only text as message to recievers
     * @param message String message to send
     * @param recievers ArrayList<String> of recievers
     */
    public void sendMessage(String message, ArrayList<String> recievers) {

        TextMessage m = new TextMessage(message, myUser);
        for (User u : client.getOnlineUsers())
            for (String r : recievers) {
                if (u.getUsername().equals(r) && !r.equals(myUser.getUsername())) {
                    System.out.println("ONLINE USER ADDED " + u.getUsername());
                    m.addReciever(u);
                }
            }

        for (User u : client.getContacts().getContactlist()) {
            for (String r : recievers) {
                if (u.getUsername().equals(r) && !r.equals(myUser.getUsername())) {
                    System.out.println("contact added " + u.getUsername());
                    m.addReciever(u);
                }
            }
        }
        m.removeDuplicateRecievers();

        mainFrame.appendTextMessageGUI(m.getSender().getUsername()+ ":");
        mainFrame.appendTextMessageGUI(m.getMessage());
        try {
            client.sendMessage(m);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Send text and picture as a message to recievers
     * @param message - textmessage as String
     * @param path - Path to picture
     * @param recievers - ArrayList<String> of recievers
     */
    public void sendMessage(String message, String path, ArrayList<String> recievers) {
        TextMessage m = new TextMessage(message, new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(30,30, Image.SCALE_DEFAULT)), myUser);
        for (User u : client.getOnlineUsers()) {
            for (String r : recievers) {
                System.out.println(r);
                if (u.getUsername().equals(r) && !r.equals(myUser.getUsername())) {
                        m.addReciever(u);
                }
            }
        }
        for (User u : client.getContacts().getContactlist()) {
            for (String r : recievers) {
                if (u.getUsername().equals(r) && !r.equals(myUser.getUsername())) {
                        m.addReciever(u);
                }
            }
        }
        m.removeDuplicateRecievers();
        mainFrame.appendTextMessageGUI(m.getSender().getUsername() + ":");
        mainFrame.appendTextMessageGUI(m.getIcon());
        mainFrame.appendTextMessageGUI(m.getMessage());

        try {
            client.sendMessage(m);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void disconnectClient() {
        client.disconnect();
    }


    public void setMyUser(String username, String path) {
        myUser = new User(username, new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(100,100, Image.SCALE_DEFAULT)));


    }
    public void setMyUser(String username) {
       myUser = new User(username);

    }

    public void addContact(String username) {
        for (User u : client.getOnlineUsers()) {
            if (u.getUsername().equals(username)) {
                client.addContact(u);

            }
        }
        setUpContactsGUI();
    }
    public void removeContact(String username) {
        for (int i = 0; i < client.getContacts().getContactlist().size(); i++) {
            if (client.getContacts().getContactlist().get(i).getUsername().equals(username)) {
                client.getContacts().removeContact(client.getContacts().getContactlist().get(i));

            }
        }

        setUpContactsGUI();
    }

    /**
     * Method to append TextMessages in Gui. If group chat sort away yourself and remove duplicate recievers
     * @param obj message object
     */

    public void appendTextMessageGUI(Object obj) {
        if (obj instanceof TextMessage) {

            mainFrame.appendTextMessageGUI(((TextMessage) obj).getSender().getUsername());
            mainFrame.appendTextMessageGUI(((TextMessage) obj).getIcon());
            mainFrame.appendTextMessageGUI(((TextMessage) obj).getMessage());
            setContactProfile(((TextMessage) obj).getSender());
            //mainFrame.appendTextMessageGUI(((TextMessage) obj).getTimeSent());
            if (((TextMessage) obj).getRecievers().size() > 1) {
                for (User u :((TextMessage) obj).getRecievers()) {
                    for (int i = 0; i < mainFrame.getMainPanel().getRecievers().size(); i++) {
                        if (mainFrame.getMainPanel().getRecievers().get(i).equals(u.getUsername())) {
                            mainFrame.getMainPanel().removeReciever(i);
                        }

                    }
                    if (!u.getUsername().equals(getMyUser().getUsername())) {
                        mainFrame.getMainPanel().setRecievers(u.getUsername());
                    }
                }

            }
        }

    }

    public User getMyUser() {
        return myUser;
    }
}

