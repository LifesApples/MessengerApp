package Client.View;

import Client.Controller.Controller;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private Controller controller;
    private MainPanel mainPanel;
    private ContactPanel contactPanel;
    private LoginPage lp;
    private int width;
    private int height;

    public MainFrame(int width, int height, Controller controller, String username){
        this.width = width;
        this.height = height;
        this.controller = controller;
        setResizable(true);
        setSize(width, height);
        contactPanel = new ContactPanel(width, height, controller);



        setContentPane(contactPanel);
        setVisible(true);

     //   setBackground(Color.BLACK);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void sendErrormessage (String str) {
        JOptionPane.showMessageDialog(null, str);
    }

    public void setProfileGUI(String name, Icon icon) {
        mainPanel.setIcon(icon);
        mainPanel.setUsername(name);
    }

    public void openChatwindow(String username, String contactName) {
        mainPanel = new MainPanel(width, height, controller, contactName, username);
    }
}
