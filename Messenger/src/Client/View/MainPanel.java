package Client.View;

import Client.Controller.Controller;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static javax.print.attribute.standard.Chromaticity.COLOR;

public class MainPanel extends JFrame implements ActionListener {

    private JPanel mainPanel, chatWindow;
    private String contactName, username, str;
    private Controller controller;
    private Chattwindow chattWindow;
    private TextWindow textWindow;
    private JLabel profilePic;
    private JButton sendButton, contactList, pictureButton;
    private JFileChooser fileChooser;
    private File file;
    private JList messageList;
    private DefaultListModel dlm = new DefaultListModel();
    private Icon icon;


    public MainPanel(int width, int height, Controller controller, String contactName, String username) {

        this.controller = controller;
        this.username = username;
        this.contactName = contactName;
        setSize(width, height);
        initiateList();
        initiateButtons();
        initiateChatWindow();
       // chattWindow = new Chattwindow(controller, contactName);

        textWindow = new TextWindow(width, height, controller, username);

        profilePic = new JLabel(new ImageIcon());
        profilePic.setBounds(450, 400, 100, 100);


        mainPanel = new JPanel(null);
        mainPanel.add(sendButton);
        mainPanel.add(pictureButton);
        mainPanel.add(chatWindow);
        mainPanel.add(textWindow);
        mainPanel.add(profilePic);

        mainPanel.setBounds(0, 0, width, height);

        setVisible(true);
        setContentPane(mainPanel);
        setBackground(Color.PINK);


    }

    private void setUp() {

    }


    private void initiateChatWindow() {
        chatWindow = new JPanel(new GridLayout(1, 1));
        chatWindow.setSize(435, 300);
        chatWindow.setLocation(10, 55);
        chatWindow.setBackground(Color.BLACK);
        chatWindow.setBorder(BorderFactory.createTitledBorder("Chat with " + contactName));
        chatWindow.add(messageList);

    }

    private void initiateButtons() {
        sendButton = new JButton();
        sendButton.setSize(80, 50);
        sendButton.setLocation(360, 430);
        sendButton.setText("Send");
        sendButton.addActionListener(this);

        pictureButton = new JButton("Add picture");
        pictureButton.setSize(80, 50);
        pictureButton.setLocation(360, 480);
        pictureButton.addActionListener(this);
    }

    private void initiateList() {
        messageList = new JList(dlm);
        messageList.setBackground(Color.GREEN);

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
        if (e.getSource() == sendButton) {
            if (file == null) {
                controller.sendMessage(textWindow.getText());
                textWindow.setText(null);
            } else {
                controller.sendMessage(textWindow.getText(), file.getAbsolutePath());
                textWindow.setText(null);
                file = null;
            }
        }
        if (e.getSource() == pictureButton) {
            chooseFile();
        }
    }



    public void addMessage(Object obj) {
        if (obj instanceof String) {
            dlm.addElement(obj);
        }
        if (obj instanceof Icon) {
            dlm.addElement(obj);
        }


    }
}
