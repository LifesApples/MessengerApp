package Client.Controller;

import Client.Model.Client;
import Server.Model.Server;
import Client.Model.User;
import Client.View.LoginPage;
import Client.View.MainFrame;

import javax.swing.*;
import java.io.IOException;

public class Controller {
    private MainFrame mainFrame;
    private LoginPage lp;
    private Server server;
    private Client client;


    public Controller() throws IOException {
        server = new Server(2343);
        lp = new LoginPage(this);

    }

    public void signIn(String username) {
        User user = new User(username, new ImageIcon("files/knight.jpg"));
        System.out.println("icon");
        client = new Client("127.0.0.1",2343, user, this);
        System.out.println("client");
        mainFrame = new MainFrame( 600,600, this, user.getUsername());
        mainFrame.setProfileGUI(user.getUsername(), user.getIcon() );
        System.out.println("mainframe");

    }

    public void sendMessage(String message, String username) {
        System.out.println(username + " said: " + message);
    }

    public void sendGUIerror(String str) {

    }
}

