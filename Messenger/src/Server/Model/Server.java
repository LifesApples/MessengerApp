package Server.Model;

import Client.Model.TextMessage;
import Client.Model.User;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//Textmessage som en textmessagee-object och user som en string
//Tidsstämpel sköts här
public class Server {

    private int port;
    private ArrayList<String> log;

    public Server(int port) throws IOException {
        this.port = port;
        log = new ArrayList<>();
        new Connect(port).start();
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


        @Override
        public void run() {
            Socket socket = null;
            try(ServerSocket serverSocket = new ServerSocket(port)){
                System.out.println("MessageServer listening on port " + serverSocket.getLocalPort());
                while(!Thread.interrupted()) {
                    System.out.println("Trying to connect");
                    socket = serverSocket.accept();
                    System.out.println("Connection established");
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
            System.out.println("MessageServer down.");

        }

        //Notifies handlers that a new user logged in and that they need to update their userlists via addOnlineUser()
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            System.out.println("PCE was fired to server");
            if(evt.getPropertyName().equals("New user")){
                for(ClientHandler handler : handlers){
                    handler.addOnlineUser();
                }
            }
            else if(evt.getPropertyName().equals("User disconnected")){
                for(ClientHandler handler : handlers){
                    handler.removeUser((User) evt.getNewValue());
                }
            }
            else if(evt.getPropertyName().equals("New message")){
                //Loop through recievers

                TextMessage message = (TextMessage) evt.getNewValue();
                System.out.println("Checking recievers: " + message.getRecievers().size());
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
                            saveOfflineMessage(reciever.getUsername(), message.getMessage());

                        }
                    }



                }
            }

        }

        private void saveOfflineMessage(String username, String message) {
            if(!offlineMessages.containsKey(username)){
                System.out.println("User has no saved messages");
                ArrayList<String> messageList = new ArrayList();
                messageList.add(message);
                offlineMessages.put(username,messageList);
            }
            else {
                //get messagelist for user and add messages
                System.out.println("Offline user found. Saving message");
                ArrayList<String> messageList = (ArrayList<String>) offlineMessages.get(username);
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

            public ClientHandler(Socket socket) throws IOException {
                String message, response;
                this.socket = socket;
                ois = new ObjectInputStream(socket.getInputStream());
                oos = new ObjectOutputStream(socket.getOutputStream());
            }



            @Override
            public void run() {

                while(true) {
                    Object object = null;

                    try {
                        object = ois.readObject();
                        if(object instanceof User) {
                            user = (User) object;
                            if(user.getStatus() == 1){
                                userlist.put(user,this);
                                checkOfflineMessages(user);
                                notifyServer.firePropertyChange("New user",null,user);
                                log.add("User: " + user.getUsername() + " logged in");
                            }
                            else if (user.getStatus() == 0){
                                notifyServer.firePropertyChange("User disconnected",null,user);
                            }

                        }
                        else if(object instanceof TextMessage){
                            TextMessage message = (TextMessage) object;
                            System.out.println("TextMessage found: " + message.getMessage());
                            System.out.println("Sent by: " + message.getSender().getUsername());
                            notifyServer.firePropertyChange("New message",null,message);
                            log.add("User: " + message.getSender() + " Sent: " + message.getMessage() +
                                     " to: " + message.getSender());

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }



                }

                }
                public void logTraffick(){
                //Print traffick to file for every new activity
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
                System.out.println("Sending message");
                try {
                    oos.writeObject(message);
                    oos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void checkOfflineMessages(User user){
                System.out.println("Checking offline messages for: " + user.getUsername());
                User Ali = new User("Ali");
                ArrayList<String> alisMessages = new ArrayList<>();
                alisMessages.add("1.Detta är ett test");
                alisMessages.add("2.Även detta");
                offlineMessages.put(Ali, alisMessages);
                //offlineMessages.put(new User("Ali").getUsername(),new ArrayList<String>().add("Detta är ett test"));

                for(int i = 0; i<offlineMessages.size(); i++){
                   // System.out.println("User has offline messages");
                    for (var nextUser : offlineMessages.keySet()){
                        User offlineUser = (User) nextUser;
                        if(offlineUser.getUsername().equals(user.getUsername())){
                            System.out.println("User: " +offlineUser.getUsername() + " has offline messages");
                            System.out.println("Loading offline messages");
                            ArrayList<String> messagelist = (ArrayList<String>) offlineMessages.get(offlineUser);
                            for(int j = 0; j<messagelist.size(); j++){
                                //Use sendMessage method instead
                                System.out.println("Message: " + messagelist.get(j));
                            }


                            try {
                                oos.writeObject(offlineMessages);
                                oos.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                        else {
                            System.out.println("User: " + user.getUsername() + " has no offline messages");
                        }

                    }

                }


            }

            public void removeUser(User offlineUser) {
                userlist.remove(offlineUser);
                try {
                    oos.writeObject(offlineUser);
                    oos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
}
}


