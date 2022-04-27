package View;

import Controller.Controller;

import javax.swing.*;
import java.awt.*;

public class ProfilePic extends JPanel {
    private Controller controller;
    public ProfilePic(Controller controller) {
        super(null);
        this.controller = controller;
        setSize(130, 150);
        setBackground(Color.blue);
    }
}
