package Server.View;


import Server.Controller.Controller;

import javax.swing.*;

public class MainFrame extends JFrame {
    private Controller controller;
    private LogPanel logPanel;
    private int width;
    private int height;


    public MainFrame(int width, int height, Controller controller){
        this.width = width;
        this.height = height;
        this.controller = controller;
        setResizable(true);
        setSize(width, height);
        logPanel = new LogPanel(width, height, controller);
        setContentPane(logPanel);
        setVisible(true);
        //   setBackground(Color.BLACK);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void updateLog(String logMessage) {
        logPanel.updateLog(logMessage);
    }

    public void setSearchresults(DefaultListModel log){
        logPanel.setLog(log);
    }


}
