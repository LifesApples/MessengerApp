package Client.View;

import Client.Controller.Controller;
import Client.Model.TextMessage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Chattwindow extends JPanel {
    private Controller controller;
    private String contactName;
    private DefaultListModel<Object> messageList = new DefaultListModel<>();
    private ArrayList<String> recievers = new ArrayList<>();

    public Chattwindow(Controller controller, ArrayList<String> recievers) {
        super(null);
        this.recievers = recievers;
        this.controller = controller;
        setSize(435, 300);
        setLocation(10, 55);
        setBackground(Color.white);
        setBorder(BorderFactory.createTitledBorder("Chat with " + recievers.size()));


    }


    public DefaultListModel<Object> getMessageList() {
        return messageList;
    }

    public void setMessageList(DefaultListModel<Object> message) {
        this.messageList = messageList;
    }

    public void addMessage(Object obj) {
        if (obj instanceof Icon) {
           messageList.addElement(obj);
        }
        if (obj instanceof String) {
            messageList.addElement(obj);
        }
    }
}
