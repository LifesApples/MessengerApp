package View;

import Controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class LoginPage extends JFrame {
    private Controller controller;
    private MainLoginPanel mainp;
    private BufferedImage image;

    //Jframe
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
        private JTextField password;
        private JButton loginButton;

        public MainLoginPanel(int width, int height) {
            super(null);
            setSize(width, height);

            username = new JTextField();
            username.setSize(150,30);
            username.setLocation(400,350);
            add(username);



            password = new JTextField();
            password.setSize(150,30);
            password.setLocation(400,395);
            add(password);

            loginButton = new JButton();
            loginButton.setSize(70,40);
            loginButton.setLocation(425,500);
            loginButton.setText("Sign in");
            add(loginButton);
            loginButton.addActionListener(this);

            setBackground(Color.GREEN);
            setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(e + " was pressed");
            if(e.getSource() == loginButton){
                System.out.println("Login was pressed");
                controller.signIn(username.getText());
            }
        }
    }



}
