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


    }

    public void signIn() {
        client = new Client("127.0.0.1",2343, myUser, this);
        System.out.println("client");
        mainFrame = new MainFrame( 600,600, this, myUser.getUsername());
        System.out.println("MF started");
        setUpContactsGUI();

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

    public void openChatWindow(String contact) {
        mainFrame.openChatwindow(myUser.getUsername(), contact);
        setProfile();
    }
    public void sendMessage(String message) {
        TextMessage m = new TextMessage(message, myUser);
        try {
            client.sendMessage(m);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainFrame.appendTextMessageGUI(m.getSender().getUsername()+ ":");
        mainFrame.appendTextMessageGUI(m.getMessage());


    }
    public void sendMessage(String message, String path) {
        TextMessage m = new TextMessage(message, new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(30,30, Image.SCALE_DEFAULT)), myUser);

        mainFrame.appendTextMessageGUI(m.getSender().getUsername() + ":");
        mainFrame.appendTextMessageGUI(m.getIcon());
        mainFrame.appendTextMessageGUI(m.getMessage());


    }


    public void sendGUIerror(String str) {
       // mainFrame.sendErrormessage(str);
    }



    public void disconnectClients () {
        client.disconnect();
    }

    public void setContactUser () {}

    public void setMyUser(String username, String path) {
        myUser = new User(username, new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(100,100, Image.SCALE_DEFAULT)));


    }
    public void setMyUser(String username) {
       myUser = new User(username);

    }

    public void addContact(String username) {
        client.addOnlineUser(new User("test1"));
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

    public void appendTextMessageGUI(Object obj) {
        if (obj instanceof TextMessage) {
            mainFrame.appendTextMessageGUI(((TextMessage) obj).getIcon());
            mainFrame.appendTextMessageGUI(((TextMessage) obj).getMessage());
            mainFrame.appendTextMessageGUI(((TextMessage) obj).getSender().getUsername());
            mainFrame.appendTextMessageGUI(((TextMessage) obj).getTimeSent());
        }

    }


}

