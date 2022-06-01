package Server.View;

import Server.Controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogPanel extends JPanel implements ActionListener {
    private int width;
    private int height;
    private Controller controller;
    private JList resultWindow;
    private JTextField fromDate;
    private JLabel fromLabel;
    private JTextField toDate;
    private JLabel toLabel;
    private JButton search;
    private JScrollPane scrollPane;
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
        resultWindow.setSize(900, 350);
        add(resultWindow);
        //scrollPane = new JScrollPane(resultWindow);
        //add(scrollPane);

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
        search.addActionListener(this);
        add(search);
    }

    public void setLog(DefaultListModel log) {
       // this.log = log;
       // resultWindow.setModel(log);
       // add(resultWindow);
    }

    public synchronized void updateLog(String logMessage) {
        //System.out.println("Logpanel: " + logMessage );
        log.addElement(logMessage);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == search){
            String from = fromDate.getText();
            String to = toDate.getText();
            DefaultListModel results = controller.getSearchRes(from,to);
            results.addElement("Hej detta kommer från logpanel/sökresultat");
            setLog(results);

        }
    }
}
