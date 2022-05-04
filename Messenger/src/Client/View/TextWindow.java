package Client.View;

import Client.Controller.Controller;

import javax.swing.*;
import java.awt.*;

public class TextWindow extends JTextArea {
    private String username;
    private Controller controller;


    public TextWindow(int width, int height, Controller controller, String username) {
        this.username = username;
        this.controller = controller;
        setSize(435, 150);
        setLocation(10,400);
        setBorder(BorderFactory.createTitledBorder("Text Window"));

        setBackground(Color.white);
    }


}
