package Client.View;

import Client.Controller.Controller;

import javax.swing.*;
import java.awt.*;

public class Chattwindow extends JPanel {
    private Controller controller;

    public Chattwindow(Controller controller) {
        super(null);
        this.controller = controller;
        setSize(435, 300);
        setLocation(10, 55);
        setBackground(Color.white);
        setBorder(BorderFactory.createTitledBorder("ChatWindow"));
    }
}
