package Client.View;

import Client.Controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel extends JPanel implements ActionListener {
    private String username;
    private Controller controller;
    private Chattwindow chattWindow;
    private TextWindow textWindow;
    private JLabel profilePic;
    private JButton sendButton;
    private ImageIcon icon;

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


        profilePic = new JLabel(new ImageIcon());
        profilePic.setBounds(450, 400, 200, 200);
        add(profilePic);


        setBackground(Color.PINK);


    }

    private void setUp(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public TextWindow getTextWindow() {
        return textWindow;
    }

    public void setTextWindow(TextWindow textWindow) {
        this.textWindow = textWindow;
    }



    public void setIcon(Icon icon) {
        profilePic.setIcon(icon);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == sendButton){
            controller.sendMessage(textWindow.getText(), username);
        }
    }
}
