package Client.View;

import Client.Controller.Controller;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class MainPanel extends JFrame implements ActionListener {

    private JPanel mainPanel, chatWindow;
    private String contactName, username;
    private ArrayList<String> recievers = new ArrayList<>();
    private Controller controller;
    private TextWindow textWindow;
    private JLabel profilePic, contactProfilePic;
    private JButton sendButton, pictureButton;
    private JFileChooser fileChooser;
    private File file;
    private JList messageList;
    private DefaultListModel dlm = new DefaultListModel();
    private JScrollPane scroll;



    public MainPanel(int width, int height, Controller controller, ArrayList recievers, String username) {

        this.controller = controller;
        this.username = username;
        this.recievers = recievers;
        setSize(width, height);
        initiateList();
        initiateButtons();
        initiateChatWindow();


        textWindow = new TextWindow(width, height, controller, username);

        profilePic = new JLabel(new ImageIcon());
        profilePic.setBounds(450, 400, 100, 100);

        contactProfilePic = new JLabel(new ImageIcon());
        contactProfilePic.setBounds(450, 100, 100, 100);


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

    public MainPanel(int width, int height, Controller controller, Object object) {

        this.controller = controller;
        setSize(width, height);
        initiateList();
        initiateButtons();
        initiateChatWindow();
        recievers.add((String) object);

        textWindow = new TextWindow(width, height, controller, (String)object);

        profilePic = new JLabel(new ImageIcon());
        profilePic.setBounds(450, 400, 100, 100);

        contactProfilePic = new JLabel(new ImageIcon());
        contactProfilePic.setBounds(450, 100, 100, 100);


        mainPanel = new JPanel(null);
        mainPanel.add(sendButton);
        mainPanel.add(pictureButton);
        mainPanel.add(chatWindow);
        mainPanel.add(textWindow);
        mainPanel.add(profilePic);
        mainPanel.add(contactProfilePic);

        mainPanel.setBounds(0, 0, width, height);

        setVisible(true);
        setContentPane(mainPanel);
        setBackground(Color.PINK);

    }


    private void initiateChatWindow() {
        chatWindow = new JPanel(new GridLayout(1, 1));
        chatWindow.setSize(435, 300);
        chatWindow.setLocation(10, 55);
        chatWindow.setBackground(Color.BLACK);
        chatWindow.setBorder(BorderFactory.createTitledBorder("Chat with " ));
        chatWindow.add(scroll);

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
        messageList.setBackground(Color.WHITE);
        scroll = new JScrollPane(messageList);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void removeReciever(int index) {
        recievers.remove(index);

    }

    public void setRecievers(String reciever) {
        this.recievers.add(reciever);
    }

    public ArrayList<String> getRecievers() {
        return recievers;
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

        }
    }

    public void setIcon(Icon icon) {
        profilePic.setIcon(icon);
    }

    public void setContactIcon(Icon icon) {
        contactProfilePic.setIcon(icon);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
                if (file == null) {
                    controller.sendMessage(textWindow.getText(), recievers);
                    textWindow.setText(null);
                } else {
                    controller.sendMessage(textWindow.getText(), file.getAbsolutePath(), recievers);
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
