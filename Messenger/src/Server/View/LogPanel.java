package Server.View;

import Server.Controller.Controller;

import javax.swing.*;
import java.awt.*;

public class LogPanel extends JPanel {
    private int width;
    private int height;
    private Controller controller;
    private JList resultWindow;
    private JTextField fromDate;
    private JLabel fromLabel;
    private JTextField toDate;
    private JLabel toLabel;
    private JButton search;
    private DefaultListModel log = new DefaultListModel();

    public LogPanel(int width, int height, Controller controller) {
        this.width = width;
        this.height = height;
        this.controller = controller;
        setBorder(BorderFactory.createTitledBorder("Results"));
        setBounds(13, 403, 693, 82);
        setLayout(null);



        resultWindow = new JList(log);
        resultWindow.setLocation(130, 55);
        resultWindow.setSize(430, 350);
        add(resultWindow);

        fromLabel = new JLabel();
        fromLabel.setText("From");
        fromLabel.setLocation(12, 35);
        fromLabel.setSize(100, 30);
        add(fromLabel);

        fromDate = new JTextField();
        fromDate.setLocation(10, 55);
        fromDate.setSize(100, 30);
        add(fromDate);

        toLabel = new JLabel();
        toLabel.setText("To");
        toLabel.setLocation(12, 85);
        toLabel.setSize(100, 30);
        add(toLabel);

        toDate = new JTextField();
        toDate.setLocation(10, 101);
        toDate.setSize(100, 30);
        add(toDate);

        search = new JButton();
        search.setLocation(10, 140);
        search.setText("Search");
        search.setSize(100,20);
        add(search);




    }

    public void updateLog(String logMessage) {
        log.addElement(logMessage);
    }
}
