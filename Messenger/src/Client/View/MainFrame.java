package Client.View;

import Client.Controller.Controller;

import javax.swing.*;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    private Controller controller;
    private MainPanel mainPanel;
    private ContactPanel contactPanel;
    private int width;
    private int height;

    public MainFrame(int width, int height, Controller controller){
        this.width = width;
        this.height = height;
        this.controller = controller;
        setResizable(true);
        setSize(width, height);
        contactPanel = new ContactPanel(width, height, controller);



        setContentPane(contactPanel);
        setVisible(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }


    public void setProfileGUI(String name, Icon icon) {
        mainPanel.setIcon(icon);
        mainPanel.setUsername(name);
    }

    public void setContactProfileGUI(String name, Icon icon) {
        mainPanel.setContactIcon(icon);

    }



    public void openChatwindow(String username, ArrayList recievers) {
        mainPanel = new MainPanel(width, height, controller, recievers, username);
    }

    public void contactPanelBorder (String username) {
        contactPanel.setBorder(username);
    }
    public void setContacts(String[] contacts) {
        contactPanel.updateMyContacts(contacts);
    }

    public void setOnlineUsers(String[] onlineUsers) {
        contactPanel.updateOnlineUsers(onlineUsers);
    }

    public void appendTextMessageGUI(Object obj) {
        if (mainPanel == null) {
            mainPanel = new MainPanel(width, height, controller, obj);
            mainPanel.setIcon(controller.getMyUser().getIcon());
        }

        mainPanel.addMessage(obj);
    }

    public MainPanel getMainPanel() {
        return mainPanel;
    }
}
