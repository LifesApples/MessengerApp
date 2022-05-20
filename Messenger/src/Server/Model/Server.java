package Server.Model;

import Client.Model.TextMessage;
import Client.Model.User;
import Server.Controller.Controller;

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
    private ArrayList<LogEntry> searchResults;
    private Controller controller;

    public Server(int port, Controller controller) throws IOException {
        this.port = port;
        this.controller = controller;
        log = new ArrayList<>();
        searchResults = new ArrayList<>();
        new Connect(port).start();
    }

    public void searchDates(String from, String to){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        Date date = new Date();
        try {
            Date after = formatter.parse(from);
            Date before = formatter.parse(to);

            for(int i = 0; i<log.size();i++){
                LogEntry logentry = log.get(i);
                //Om detta datumet är efter/före detta datumet men före detta & detta datumet är efter...s
                if(after.before(logentry.getDate()) < after && logentry.getDate() > before)
                    searchResults
            }

        } catch (ParseException e) {
            System.out.println("Could not format date");
            e.printStackTrace();
        }
        date.toString();


    }


    private class Connect extends Thread implements PropertyChangeListener{
        private int port;
        private ServerSocket serverSocket;
        private ArrayList<ClientHandler> handlers;
        private HashMap userlist;
        private static HashMap offlineMessages;
        public Connect(int port) throws IOException {
            this.port = port;
            userlist = new HashMap();
            offlineMessages = new HashMap();
            handlers = new ArrayList<>();

        }

        public void updateLog(String logMessage){
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd hh:mm");
            LogEntry entry = new LogEntry(sdf.format(date),logMessage);

            controller.updateLog(entry.toString());
        }


        @Override
        public void run() {
            Socket socket = null;

            try(ServerSocket serverSocket = new ServerSocket(port)){

                updateLog("Server is listening on port: " + port);
                while(!Thread.interrupted()) {

                    socket = serverSocket.accept();
                    updateLog("Connection established!");
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
                for(ClientHandler handler : handlers){
                    handler.removeUser((User) evt.getNewValue());
                }
                updateLog(((User) evt.getNewValue()).getUsername() + " logged out");
            }
            else if(evt.getPropertyName().equals("New message")){
                TextMessage message = (TextMessage) evt.getNewValue();
                updateLog((message.toString()));
                ArrayList<User> recievers =((TextMessage) evt.getNewValue()).getRecievers();
                for(User reciever : recievers){
                    for(var entry : userlist.keySet()){

                        String username = ((User) entry).getUsername();
                        System.out.println("Checking if: " + username + " is in recieverlist");
                        if(username.equals(reciever.getUsername())){
                            System.out.println("Username: " + username + " was found in recieverlist");
                            ClientHandler recipent = (ClientHandler) userlist.get(entry);
                            recipent.sendMessage(message);
                        }
                        else{
                            System.out.println("User not online");
                            saveOfflineMessage(reciever.getUsername(), message);

                        }
                    }



                }
            }

        }

        private void saveOfflineMessage(String username, TextMessage message) {
            if(!offlineMessages.containsKey(username)){
                updateLog("User : " + username + "has no saved messages");
                ArrayList<TextMessage> messageList = new ArrayList();
                messageList.add(message);
                offlineMessages.put(username,messageList);
            }
            else {
                //get messagelist for user and add messages
                updateLog("User: " + username + " is offline. Saving messages ");
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

            public ClientHandler(Socket socket) throws IOException {
                String message, response;
                this.socket = socket;
                ois = new ObjectInputStream(socket.getInputStream());
                oos = new ObjectOutputStream(socket.getOutputStream());
                flag = true;
            }



            @Override
            public void run() {

                while(flag) {
                    Object object = null;

                    try {
                        object = ois.readObject();
                        if(object instanceof User) {
                            user = (User) object;
                            if(user.getStatus() == 1){
                                userlist.put(user,this);
                                checkOfflineMessages(user);
                                notifyServer.firePropertyChange("New user",null,user);
                            }
                            else if (user.getStatus() == 0){
                                notifyServer.firePropertyChange("User disconnected",null,user);
                            }

                        }
                        else if(object instanceof TextMessage){
                            TextMessage message = (TextMessage) object;
                            notifyServer.firePropertyChange("New message",null,message);

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
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
                    oos.writeObject(message);
                    oos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void checkOfflineMessages(User user){
                updateLog("Checking offline messages for: " + user.getUsername());
                User Ali = new User("Ali");
                ArrayList<TextMessage> alisMessages = new ArrayList<>();
                alisMessages.add(new TextMessage("1.Detta är ett test",Ali));
                alisMessages.add(new TextMessage("2.Även detta",Ali));
                offlineMessages.put(Ali, alisMessages);
                //offlineMessages.put(new User("Ali").getUsername(),new ArrayList<String>().add("Detta är ett test"));

                for(int i = 0; i<offlineMessages.size(); i++){
                    for (var nextUser : offlineMessages.keySet()){
                        User offlineUser = (User) nextUser;
                        if(offlineUser.getUsername().equals(user.getUsername())){
                            updateLog("User: " +offlineUser.getUsername() + " has offline messages");
                            updateLog("Loading offline messages");
                            ArrayList<TextMessage> messagelist = (ArrayList<TextMessage>) offlineMessages.get(offlineUser);
                            for(int j = 0; j<messagelist.size(); j++){
                                //Use sendMessage method instead
                                sendMessage(messagelist.get(j));
                                updateLog("Message: " + messagelist.get(j));
                            }


                            try {
                                oos.writeObject(offlineMessages);
                                oos.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                        else {
                            updateLog("User: " + user.getUsername() + " has no offline messages");
                        }

                    }

                }


            }

            public void removeUser(User offlineUser) {
                userlist.remove(offlineUser);
                try {
                    oos.writeObject(offlineUser);
                    oos.flush();
                    socket.close();
                    flag = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }



        }

}
}


