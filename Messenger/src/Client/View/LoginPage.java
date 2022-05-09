package Client.View;

import Client.Controller.Controller;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class LoginPage extends JFrame {
    private Controller controller;
    private MainLoginPanel mainp;



    public LoginPage(Controller controller) {
        this.controller = controller;
        setResizable(true);
        setSize(1000, 1000);
        mainp = new MainLoginPanel(500, 500);
        setContentPane(mainp);
        setVisible(true);

    }
    //Jpanel/mainpanel
    private class MainLoginPanel extends JPanel implements ActionListener {
        private JTextField username;
        private JButton btnLogin, btnPicture;
        private JFileChooser profilePicture;
        private File file;


        public MainLoginPanel(int width, int height) {
            super(null);
            setSize(width, height);

            username = new JTextField();
            username.setSize(150,30);
            username.setLocation(400,350);
            initiateButtons();

            add(username);
            add(btnLogin);
            add(btnPicture);
            setBackground(Color.GREEN);
            setVisible(true);
        }

        public void initiateButtons() {
            btnPicture = new JButton("Profile picture");
            btnPicture.setSize(120, 40);
            btnPicture.setLocation(425, 570);
            btnPicture.addActionListener(this);

            btnLogin = new JButton();
            btnLogin.setSize(120,40);
            btnLogin.setLocation(425,500);
            btnLogin.setText("Sign in");
            btnLogin.addActionListener(this);
        }
        public void chooseFile() {
            profilePicture = new JFileChooser();
            profilePicture.setFileSelectionMode(JFileChooser.FILES_ONLY);
            profilePicture.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG, JPG and JPEG images", "jpg", "png", "jpeg");
            profilePicture.addChoosableFileFilter(filter);
            int returnValue = profilePicture.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
               file = profilePicture.getSelectedFile();
               System.out.println(file.getAbsolutePath());

            }

        }


        public JTextField getUsername() {
            return username;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(e + " was pressed");
            if(e.getSource() == btnLogin){
                System.out.println("Login was pressed");
                if (file == null) {
                    controller.setMyUser(getUsername().getText());
                    controller.signIn();
                    dispose();
                }
                else {
                    controller.setMyUser(getUsername().getText(), file.getAbsolutePath());
                    controller.signIn();
                    dispose();
                }
            }
            if (e.getSource() == btnPicture) {
                chooseFile();
            }
        }
    }



}
