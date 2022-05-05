package Client.Controller;

import Client.Model.Client;
import Client.Model.Contacts;
import Server.Model.Server;
import Client.Model.User;
import Client.View.LoginPage;
import Client.View.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Controller {
    private MainFrame mainFrame;
    private LoginPage lp;
    private Server server;
    private Client client;
    private User myUser, contactUser;
    private Contacts contacts;


    public Controller() throws IOException {
        contacts  = new Contacts();
        server = new Server(2343);
        lp = new LoginPage(this);

    }

    public void signIn() {
        client = new Client("127.0.0.1",2343, myUser, this);
        System.out.println("client");
        mainFrame = new MainFrame( 600,600, this, myUser.getUsername());
        client.setContacts(contacts.getContactlist());
        setUpContactsGUI();

    }

    public void setUpContactsGUI() {
        String[] contacts = new String[client.getContacts().size()];
        for (int i = 0; i < contacts.length; i++) {
            contacts[i] = client.getContacts().get(i).getUsername();
        }
    }
    public void setProfile() {
        mainFrame.setProfileGUI(myUser.getUsername(), myUser.getIcon() );
    }

    public void openChatWindow() {
        mainFrame.openChatwindow(myUser.getUsername(), "TESTUSER");
        setProfile();
    }
    public void sendMessage(String message, String username) {
        System.out.println(username + " said: " + message);
    }

    public void sendGUIerror(String str) {
        mainFrame.sendErrormessage(str);
    }

    public void disconnectClients () {
        client.disconnect();
    }


    public void setMyUser(String username, String path) {
        myUser = new User(username, new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(100,100, Image.SCALE_DEFAULT)));

    }
    public void setMyUser(String username) {
       myUser = new User(username);


    }



}

