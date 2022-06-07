package Server.Controller;


import Server.Model.Server;
import Server.View.MainFrame;

import javax.swing.*;
import java.io.IOException;

/**
 *  Controller class used for instantiating server and updating logs. Also communicates with view classes.
 *  @author Azam Suleiman
 */
public class Controller {
    private Server server;
    private MainFrame mainFrame;


    public Controller() throws IOException {
        mainFrame = new MainFrame(1200,600, this);
        server = new Server(2343, this);

    }

    /**
     * Sends the latest message to be added to the log
     * @param logmessage
     */
    public void updateLog(String logmessage) {
        mainFrame.updateLog(logmessage);
    }

    /**
     * Sends searchresults to the GUI
     * @param from
     * @param to
     * @return
     */
    public DefaultListModel getSearchRes(String from, String to) {
        return server.searchDates(from,to);
    }
}
