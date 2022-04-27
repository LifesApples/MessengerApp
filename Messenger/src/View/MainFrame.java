package View;

import Controller.Controller;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private Controller controller;
    private MainPanel mainPanel;
    private LoginPage lp;

    public MainFrame(int width, int height, Controller controller, String username){
        this.controller = controller;
        setResizable(true);
        setSize(width, height);

        mainPanel = new MainPanel(width, height, controller, username);
        setContentPane(mainPanel);
        setVisible(true);

        setBackground(Color.BLACK);
    }
}
