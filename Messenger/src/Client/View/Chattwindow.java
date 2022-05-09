package Client.View;

import Client.Controller.Controller;
import Client.Model.TextMessage;

import javax.swing.*;
import java.awt.*;

public class Chattwindow extends JPanel {
    private Controller controller;
    private String contactName;
    private DefaultListModel<Object> messageList = new DefaultListModel<>();

    public Chattwindow(Controller controller, String contactName) {
        super(null);
        this.contactName = contactName;
        this.controller = controller;
        setSize(435, 300);
        setLocation(10, 55);
        setBackground(Color.white);
        setBorder(BorderFactory.createTitledBorder("Chat with " + contactName));


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
