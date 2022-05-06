package Client.View;

import Client.Controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel extends JFrame implements ActionListener {

    private JPanel mainPanel;
    private String contactName, username;
    private Controller controller;
    private Chattwindow chattWindow;
    private TextWindow textWindow;
    private JLabel profilePic;
    private JButton sendButton, contactList;
    private ImageIcon icon;

    public MainPanel(int width, int height, Controller controller, String contactName, String username) {

        this.controller = controller;
        this.username = username;
        this.contactName = contactName;
        setSize(width, height);

        sendButton = new JButton();
        sendButton.setSize(80,50);
        sendButton.setLocation(360,430);
        sendButton.setText("Send");
        sendButton.addActionListener(this);

        chattWindow = new Chattwindow(controller, contactName);


        textWindow = new TextWindow(width,height,controller,username);


        profilePic = new JLabel(new ImageIcon());
        profilePic.setBounds(450, 400, 100, 100);





        mainPanel = new JPanel(null);
        mainPanel.add(sendButton);
        mainPanel.add(chattWindow);
        mainPanel.add(textWindow);
        mainPanel.add(profilePic);
        mainPanel.setBounds(0, 0, width, height);

        setVisible(true);
        setContentPane(mainPanel);
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
