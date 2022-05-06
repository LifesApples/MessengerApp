package Client.View;

import Client.Controller.Controller;

import javax.swing.*;
import java.awt.*;

public class Chattwindow extends JPanel {
    private Controller controller;
    private String contactName;

    public Chattwindow(Controller controller, String contactName) {
        super(null);
        this.contactName = contactName;
        this.controller = controller;
        setSize(435, 300);
        setLocation(10, 55);
        setBackground(Color.white);
        setBorder(BorderFactory.createTitledBorder("Chat with " + contactName));
    }
}
