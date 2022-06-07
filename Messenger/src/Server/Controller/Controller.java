package Server.Controller;


import Server.Model.Server;
import Server.View.MainFrame;

import javax.swing.*;
import java.io.IOException;

public class Controller {
    private Server server;
    private MainFrame mainFrame;


    public Controller() throws IOException {
        mainFrame = new MainFrame(1200,600, this);
        server = new Server(2343, this);


    }

    public void updateLog(String logmessage) {
        mainFrame.updateLog(logmessage);
    }

    public DefaultListModel getSearchRes(String from, String to) {
        return server.searchDates(from,to);
    }
}
