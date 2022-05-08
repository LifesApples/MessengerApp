package Client.View;

import Client.Controller.Controller;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainPanel extends JFrame implements ActionListener {

    private JPanel mainPanel;
    private String contactName, username;
    private Controller controller;
    private Chattwindow chattWindow;
    private TextWindow textWindow;
    private JLabel profilePic;
    private JButton sendButton, contactList, pictureButton;
    private ImageIcon icon;
    private JFileChooser fileChooser;
    private File file;

    public MainPanel(int width, int height, Controller controller, String contactName, String username) {

        this.controller = controller;
        this.username = username;
        this.contactName = contactName;
        setSize(width, height);

        initiateButtons();

        chattWindow = new Chattwindow(controller, contactName);

        textWindow = new TextWindow(width,height,controller,username);

        profilePic = new JLabel(new ImageIcon());
        profilePic.setBounds(450, 400, 100, 100);


        mainPanel = new JPanel(null);
        mainPanel.add(sendButton);
        mainPanel.add(pictureButton);
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



    private void initiateButtons() {
        sendButton = new JButton();
        sendButton.setSize(80,50);
        sendButton.setLocation(360,430);
        sendButton.setText("Send");
        sendButton.addActionListener(this);

        pictureButton = new JButton("Add picture");
        pictureButton.setSize(80, 50);
        pictureButton.setLocation(360, 480);
        pictureButton.addActionListener(this);
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

    public Chattwindow getChattWindow() {
        return chattWindow;
    }

    public void setChattWindow(Chattwindow chattWindow) {
        this.chattWindow = chattWindow;
    }

    public void chooseFile() {
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG, JPG and JPEG images", "jpg", "png", "jpeg");
        fileChooser.addChoosableFileFilter(filter);
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            System.out.println(file.getAbsolutePath());

        }
    }

    public void setIcon(Icon icon) {
        profilePic.setIcon(icon);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == sendButton) {
            if (file == null) {
                controller.sendMessage(textWindow.getText());
            }
            else {
                controller.sendMessage(textWindow.getText(), file.getAbsolutePath());
            }
        }
        if (e.getSource() == pictureButton) {
            chooseFile();
        }
    }
}
