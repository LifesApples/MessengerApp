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

//Textmessage som en textmessagee-object och user som en string
//Tidsstämpel sköts här
public class Server {

    private int port;
    private ArrayList<LogEntry> log;
    private DefaultListModel<String> searchResults;
    private Controller controller;
    private LogToFile logToFile;


    public Server(int port, Controller controller) throws IOException {
        this.port = port;
        this.controller = controller;
        log = new ArrayList<>();
        logToFile = new LogToFile("files/logentries.dat");
        searchResults = new DefaultListModel<>();
        new Connect(port).start();
    }

    public synchronized void updateLog(String logMessage){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd hh:mm");
        LogEntry entry = new LogEntry(sdf.format(date),logMessage);
        log.add(entry);
        logToFile.writeEntry(entry);
        controller.updateLog(logToFile.readEntry());
    }




    public DefaultListModel<String> searchDates(String from, String to){

        System.out.println("Checking dates in server");
        SimpleDateFormat formatter = new SimpleDateFormat("yy.MM.dd hh:mm", Locale.ENGLISH);
        try {
            Date date1 = formatter.parse(from);
            Date date2 = formatter.parse(to);

            for(int i = 0; i<log.size();i++) {
                LogEntry logDate = log.get(i);
                Date loggedDate = new SimpleDateFormat("yy.MM.dd hh:mm").parse(logDate.getDate());
                System.out.println("checking log for dates");
                if (loggedDate.after(date1) && loggedDate.before(date2)) {
                    System.out.println("Found result");
                    searchResults.addElement(logDate.getDate());
                }
            }
            System.out.println("Res size: " + searchResults.size());


        } catch (ParseException e) {
            System.out.println("Could not format date");
            e.printStackTrace();
        }
        for (int i = 0; i<searchResults.size(); i++){
            System.out.println("Looping through results");
            System.out.println(searchResults.get(i));

        }
        return searchResults;


    }


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
                    System.out.println();
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

        //Notifies handlers that a new user logged in and that they need to update their userlists via addOnlineUser()
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(evt.getPropertyName().equals("New user")){
                for(ClientHandler handler : handlers){
                    handler.addOnlineUser();
                }
                updateLog(((User) evt.getNewValue()).getUsername() + " logged in");
            }
            else if(evt.getPropertyName().equals("User disconnected")){
                System.out.println("Notifying all the others");
                for(int i = 0; i<handlers.size(); i++) {
                    if(handlers.get(i).getUser().getUsername().equals(((User) evt.getNewValue()).getUsername())){
                        try {
                            System.out.println("Found user/client: " + ((User) evt.getNewValue()).getUsername());
                            System.out.println("Closing socket for: " + handlers.get(i).getUser().getUsername());
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
                System.out.println("r size: " + recievers.size());
                for(User reciever : recievers){
                    System.out.println("Next to be sent to: " + reciever.getUsername());
                    for(var entry : userlist.keySet()){
                        String username = ((User) entry).getUsername();
                        System.out.println("Checking if: " + reciever.getUsername() + " is Online");
                        if(reciever.getUsername().equals(username)){
                            recieverIsOffline = false;
                            System.out.println("Username: " + username + " was found in recieverlist");
                            ClientHandler recipent = (ClientHandler) userlist.get(entry);
                            recipent.sendMessage(message);
                        }

                    }
                    if(recieverIsOffline){
                        saveOfflineMessage(reciever.getUsername(), message);
                    }
/*
                    for(var entry : userlist.keySet()){
                        String username = ((User) entry).getUsername();
                        if(username.equals(sender.getUsername())){
                            ClientHandler handler = (ClientHandler) userlist.get(entry);
                            handler.getUser();
                        }

                    }

 */
                }
            }

        }

        private void saveOfflineMessage(String username, TextMessage message) {
            if(!offlineMessages.containsKey(username)){
                updateLog("User : " + username + " has no previous saved messages. Saving offline message in a list");
                ArrayList<TextMessage> messageList = new ArrayList();
                messageList.add(message);
                offlineMessages.put(username,messageList);
            }
            else {
                //get messagelist for user and add messages
                updateLog("User: " + username + " is offline. adding to offline message list ");
                ArrayList<TextMessage> messageList = (ArrayList<TextMessage>) offlineMessages.get(username);
                messageList.add(message);
            }
        }

        //Each new client gets a Clienthandler instance
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



            @Override
            public void run() {

                while(flag) {
                    Object object = null;

                    try {

                        if(socket.isClosed()){
                            System.out.println("Socket is closed. Closing ois.");
                            ois.close();
                            System.out.println("OIS closed. changing flag to false.");
                            flag = false;
                        }
                        else {
                            //System.out.println("Socket not closed looping");
                            object = ois.readObject();
                            oos.flush();
                            System.out.println("Object is class of: " + object.getClass());

                        }

                        if(object instanceof User) {
                            user = ((User) object);
                            System.out.println("loop " +counter);
                            counter++;

                            System.out.println("user: " + ((User) object).getUsername() + " status is: " + ((User) object).getStatus());
                            if(user.getStatus() == 1){
                                userlist.put(user,this);
                                notifyServer.firePropertyChange("New user",null,user);
                                checkOfflineMessages(user.getUsername());
                            }
                            else if (user.getStatus() == 0){
                                System.out.println("Found status 0");
                                notifyServer.firePropertyChange("User disconnected",null,user);
                            }

                        }
                        else if(object instanceof TextMessage){
                            System.out.println("text message found");
                            TextMessage message = (TextMessage) object;
                            Date date = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd hh:mm");
                            message.setTimeRecieved(sdf.format(date));
                            notifyServer.firePropertyChange("New message",null,message);

                        }


                    } catch (IOException e) {
                       // updateLog(e.getMessage());
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                       // updateLog(e.getMessage());
                        e.printStackTrace();
                    }



                }

                }



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
            public void  registerListener(PropertyChangeListener listener)
            {
                notifyServer.addPropertyChangeListener(listener);
            }

            public User getUser() {
                return user;
            }

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

            public void checkOfflineMessages(String username){
                updateLog("Checking offline messages for: " + user.getUsername());

                if(offlineMessages.containsKey(username)){
                    updateLog("Loading saved messages for: " + username);
                    ArrayList<TextMessage> messagelist = (ArrayList<TextMessage>) offlineMessages.get(username);
                    for(int j = 0; j<messagelist.size(); j++){
                        sendMessage(messagelist.get(j));
                        //updateLog("OfflineMessage: " + messagelist.get(j));
                    }
                    offlineMessages.remove(username);
                }
                else {
                    updateLog("No saved messages");
                }

                /*
                else{
                    for(int i = 0; i<offlineMessages.size(); i++){
                        for (var nextUser : offlineMessages.keySet()){
                            System.out.println("list offline messages is: " + offlineMessages.size());
                            User offlineUser = (User) nextUser;
                            if(offlineUser.getUsername().equals(user.getUsername())){
                                updateLog("User: " +offlineUser.getUsername() + " has offline messages");
                                updateLog("Loading offline messages");
                                ArrayList<TextMessage> messagelist = (ArrayList<TextMessage>) offlineMessages.get(offlineUser);
                                for(int j = 0; j<messagelist.size(); j++){
                                    sendMessage(messagelist.get(j));
                                    updateLog("Message: " + messagelist.get(j));
                                }


                                try {
                                    oos.writiteObject(offlineMessages);
                                    oos.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            else {
                                updateLog("User: " + offlineUser + " has no offline messages");
                            }

                        }


                    }
                }
                */



            }

            public void removeUser(User offlineUser) {
                System.out.println("Handlers telling their clients/users");
                userlist.remove(offlineUser);
                try {
                    oos.writeObject(offlineUser);
                    oos.flush();
                    //flag = false;
                } catch (IOException e) {
                    updateLog(e.getMessage());
                    e.printStackTrace();
                }

            }



        }

}
}


