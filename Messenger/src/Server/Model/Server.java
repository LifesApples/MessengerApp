package Server.Model;

import Client.Model.TextMessage;
import Client.Model.User;
import Server.Controller.Controller;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Server class that can handle multithreading. The class has the inner class Connect which in
 * turn has an inner ClientHandler. class used for instantiating server and updating logs.
 * The class also communicates with the LogEntry & LogToFile classes to save and store logs.
 * @author Azam Suleiman
 */
public class Server {

    private int port;
    private ArrayList<LogEntry> log;
    private DefaultListModel<String> searchResults;
    private Controller controller;
    private LogToFile logToFile;


    /**
     * Contructor creates a the logfile during the instantiation av this object
     * The contructor also starts the thread in the Connect-class.
     * @param port
     * @param controller
     * @throws IOException
     */
    public Server(int port, Controller controller) throws IOException {
        this.port = port;
        this.controller = controller;
        log = new ArrayList<>();
        logToFile = new LogToFile("files/logentries.dat");
        searchResults = new DefaultListModel<>();
        new Connect(port).start();
    }

    /**
     * Used for updating the GUI log and writing it to the file.
     * @param logMessage
     */
    public synchronized void updateLog(String logMessage){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd hh:mm");
        LogEntry entry = new LogEntry(sdf.format(date),logMessage);
        log.add(entry);
        logToFile.writeEntry(entry);
        controller.updateLog(String.valueOf(logToFile.readEntry()));
    }


    /**
     * Used for search between two point in time to retrieve the requested logs. Loops through all
     * the messages in the log to achieve this.
     * Method uses simpledataformat to format results to yy.MM.dd hh:mm.
     * @param from
     * @param to
     * @return
     */
    public DefaultListModel<String> searchDates(String from, String to){

        SimpleDateFormat formatter = new SimpleDateFormat("yy.MM.dd hh:mm", Locale.ENGLISH);
        try {
            Date date1 = formatter.parse(from);
            Date date2 = formatter.parse(to);
            for(int i = 0; i<log.size();i++) {
                LogEntry loggedEntry = log.get(i);
                Date loggedDate = new SimpleDateFormat("yy.MM.dd hh:mm").parse(loggedEntry.getDate());
                if (loggedDate.after(date1) && loggedDate.before(date2)) {
                    searchResults.addElement(loggedEntry.getDate() + " - " +  loggedEntry.getEntry());
                }
            }


        } catch (ParseException e) {
            System.out.println("Could not format date");
            e.printStackTrace();
        }

        return searchResults;


    }


    /**
     * Class used for listening for new connections. A clienthandler thread is created for every new connectino.
     * The class used Property Change Support to act on updates from clients and users.
     * The class also stores messages in a static hashmap
     */
    private class Connect extends Thread implements PropertyChangeListener{
        private int port;
        private ArrayList<ClientHandler> handlers;
        private HashMap userlist;
        private static HashMap offlineMessages;
        public Connect(int port) throws IOException {
            this.port = port;
            userlist = new HashMap();
            offlineMessages = new HashMap();
            handlers = new ArrayList<>();

        }




        @Override
        public void run() {
            Socket socket = null;

            try(ServerSocket serverSocket = new ServerSocket(port)){

                updateLog("Server is listening on port: " + port);
                while(!Thread.interrupted()) {

                    updateLog("Connection established!");
                    socket = serverSocket.accept();
                    ClientHandler handler = new ClientHandler(socket);
                    handler.start();
                    handlers.add(handler);
                    handler.registerListener(this);

                }

            }catch(IOException e){
                e.printStackTrace();

                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException ignore) {}
                }

            }
            updateLog("MessageServer down.");

        }

        /** Notifies handlers depending on if a user has logged in, logged our or sent a message.
         * The user or message-object are sent as values of the event.
         * @param evt
         */

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(evt.getPropertyName().equals("New user")){
                for(ClientHandler handler : handlers){
                    handler.addOnlineUser();
                }
                updateLog(((User) evt.getNewValue()).getUsername() + " logged in");
            }
            else if(evt.getPropertyName().equals("User disconnected")){
                for(int i = 0; i<handlers.size(); i++) {
                    if(handlers.get(i).getUser().getUsername().equals(((User) evt.getNewValue()).getUsername())){
                        try {
                            handlers.get(i).socket.close(); //Close socket for that handler
                            handlers.remove(handlers.get(i)); // remove the handler from the handler list


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                }
                updateLog(((User) evt.getNewValue()).getUsername() + " logged out");
                for(int i = 0; i<handlers.size(); i++){
                    handlers.get(i).removeUser((User) evt.getNewValue());
                }

            }

            else if(evt.getPropertyName().equals("New message")){
                TextMessage message = (TextMessage) evt.getNewValue();
                User sender = message.getSender();
                updateLog("Time recieved: " + message.getTimeRecieved() + " Sender:  " + sender + " Message: " + message );
                ArrayList<User> recievers =((TextMessage) evt.getNewValue()).getRecievers();
                Boolean recieverIsOffline = true;
                for(User reciever : recievers){
                    for(var entry : userlist.keySet()){
                        String username = ((User) entry).getUsername();
                        if(reciever.getUsername().equals(username)){
                            recieverIsOffline = false;
                            ClientHandler recipent = (ClientHandler) userlist.get(entry);
                            recipent.sendMessage(message);
                        }

                    }
                    if(recieverIsOffline){
                        saveOfflineMessage(reciever.getUsername(), message);
                    }

                }
            }

        }

        /** Used for saving messages if a user is not online. Messages are stored in an arraylist.
         * Each user has one arraylist.
         * @param username
         * @param message
         */
        private void saveOfflineMessage(String username, TextMessage message) {
            if(!offlineMessages.containsKey(username)){
                updateLog("User : " + username + " has no previous saved messages. Saving offline message in a list");
                ArrayList<TextMessage> messageList = new ArrayList();
                messageList.add(message);
                offlineMessages.put(username,messageList);
            }
            else {
                //Get messagelist for user and add messages
                updateLog("User: " + username + " is offline. adding to offline message list ");
                ArrayList<TextMessage> messageList = (ArrayList<TextMessage>) offlineMessages.get(username);
                messageList.add(message);
            }
        }


        /**
         * Each new client gets a Clienthandler instance. The clienthandler notifies the Connection class of which actions
         * have been taken, for instance when a client logs in.
         * The class runs on a thread and uses object-streams to send and recieve objects like user and textmessage-objects.
         */
        private class ClientHandler extends Thread{
            private Socket socket;
            private ObjectOutputStream oos;
            private ObjectInputStream ois;
            private User user = null;
            private PropertyChangeSupport notifyServer = new PropertyChangeSupport(this);;
            private Boolean flag;
            private int counter;

            public ClientHandler(Socket socket) throws IOException {
                String message, response;
                this.socket = socket;
                ois = new ObjectInputStream(socket.getInputStream());
                oos = new ObjectOutputStream(socket.getOutputStream());
                flag = true;
                counter =0;
            }


            /**
             * Used for recieving objects from clients. Once object is recieved a notification is sent to the
             * Connection class.
             */
            @Override
            public void run() {

                while(flag) {
                    Object object = null;

                    try {

                        if(socket.isClosed()){
                            ois.close();
                            flag = false;
                        }
                        else {
                            object = ois.readObject();
                            oos.flush();

                        }

                        if(object instanceof User) {
                            user = ((User) object);

                            if(user.getStatus() == 1){
                                userlist.put(user,this);
                                notifyServer.firePropertyChange("New user",null,user);
                                checkOfflineMessages(user.getUsername());
                            }
                            else if (user.getStatus() == 0){
                                notifyServer.firePropertyChange("User disconnected",null,user);
                            }

                        }
                        else if(object instanceof TextMessage){
                            TextMessage message = (TextMessage) object;
                            Date date = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd hh:mm");
                            message.setTimeRecieved(sdf.format(date));
                            notifyServer.firePropertyChange("New message",null,message);

                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }



                }

                }


            /**
             * Used for notifying client that a user has logged in.
             */
            public void addOnlineUser(){
                for (var user1 : userlist.keySet()){

                try {
                    oos.writeObject(((User) user1));
                    oos.flush();
                } catch (IOException e) {
                        updateLog(e.getMessage());
                        e.printStackTrace();
                    }
                   }

            }
            public void registerListener(PropertyChangeListener listener)
            {
                notifyServer.addPropertyChangeListener(listener);
            }

            public User getUser() {
                return user;
            }

            /**
             * Used for sending messages to other clients. A date and time is added before sending the message.
             * @param message
             */
            public void sendMessage(TextMessage message){
                try {
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd hh:mm");
                    message.setTimeSent(sdf.format(date));
                    oos.writeObject(message);
                    oos.flush();
                } catch (IOException e) {
                    updateLog(e.getMessage());
                    e.printStackTrace();
                }
            }

            /**
             * Checks if a user has any stored messages and sends them to the user.
             * @param username
             */
            public void checkOfflineMessages(String username){
                updateLog("Checking offline messages for: " + user.getUsername());

                if(offlineMessages.containsKey(username)){
                    updateLog("Loading saved messages for: " + username);
                    ArrayList<TextMessage> messagelist = (ArrayList<TextMessage>) offlineMessages.get(username);
                    for(int j = 0; j<messagelist.size(); j++){
                        sendMessage(messagelist.get(j));
                    }
                    offlineMessages.remove(username);
                }
                else {
                    updateLog("No saved messages");
                }





            }

            /**
             * Passes the message that a user logged out to other clients. This done by setting the user status to
             * 0 by the client.
             * @param offlineUser
             */
            public void removeUser(User offlineUser) {
                userlist.remove(offlineUser);
                try {
                    oos.writeObject(offlineUser);
                    oos.flush();

                } catch (IOException e) {
                    updateLog(e.getMessage());
                    e.printStackTrace();
                }

            }



        }

}
}


