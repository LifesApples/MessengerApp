package View;

import Controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel extends JPanel implements ActionListener {
    private String username;
    private Controller controller;
    private Chattwindow chattWindow;
    private TextWindow textWindow;
    private ProfilePic profilePic;
    private JButton sendButton;

    public MainPanel(int width, int height, Controller controller, String username) {
        super(null);
        this.controller = controller;
        this.username = username;
        setSize(100, height/8);

        sendButton = new JButton();
        sendButton.setSize(80,50);
        sendButton.setLocation(360,430);
        sendButton.setText("Send");
        add(sendButton);
        sendButton.addActionListener(this);

        chattWindow = new Chattwindow(controller);
        add(chattWindow);

        textWindow = new TextWindow(width,height,controller,username);
        add(textWindow);

        profilePic = new ProfilePic(controller);
        profilePic.setLocation(450,400);
        add(profilePic);

        profilePic = new ProfilePic(controller);
        profilePic.setLocation(450,56);
        add(profilePic);

        setBackground(Color.PINK);


    }

    private void setUp(){

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == sendButton){
            controller.sendMessage(textWindow.getText(), username);
        }
    }
}
