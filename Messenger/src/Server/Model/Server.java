package Server.Model;

import Client.Model.TextMessage;
import Client.Model.User;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

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
        public Connect(int port) throws IOException {
            this.port = port;
            userlist = new HashMap();
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
            else if(evt.getPropertyName().equals("New message")){
                TextMessage message = (TextMessage)evt.getNewValue();

                System.out.println("Handler size: " + handlers.size());
                System.out.println("reciever size: " + message.getRecievers().size());
                for(int i = 0; i<handlers.size(); i++){
                    for (int j = 0; j< message.getRecievers().size(); j++){
                        System.out.println("Checking handler user: " + handlers.get(i).getUser().getUsername());
                        System.out.println("Checking recipient: " + message.getRecievers().get(j).getUsername());
                        if(handlers.get(i).getUser().getUsername().equals(message.getRecievers().get(j).getUsername())){
                            System.out.println("Message: "+ message.getMessage() + " send to: " +
                                    message.getRecievers().get(j).getUsername());
                            handlers.get(i).sendMessage(message);
                        }
                        else {
                            System.out.println(handlers.get(j).getUser() + " was not among recpients ");
                        }
                    }
                }
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

            public void sendMessageToRecievers(TextMessage message, ArrayList<User> recievers){

                System.out.println("Checking recievers: " + recievers.size());
                //Loop through recievers
                for (int i = 0; i<recievers.size(); i++){
                    //Loop through online users
                    System.out.println("Userlist size: " + userlist.size());
                    System.out.println("Testing user: " + recievers.get(i).getUsername());
                    if (userlist.containsValue(recievers.get(i).getUsername())){
                        System.out.println("Sending to " + recievers.get(i).getUsername());
                        notifyServer.firePropertyChange("Sent message", null,message);
                    }
                    //User is offline if no match has been found in the userlist
                    //TO do: Add message to offline messages for specifik users
                    else {
                        System.out.println("Reciever: "+recievers.get(i).getUsername() +" is offline");
                    }
                    }

            }



            @Override
            public void run() {

                while(true) {
                    Object object = null;

                    try {
                        object = ois.readObject();
                        if(object instanceof User) {
                            user = (User) object;
                            userlist.put(user,user.getUsername());
                            notifyServer.firePropertyChange("New user",null,user);
                            log.add("User: " + user.getUsername() + " logged in");

                        }
                        else if(object instanceof TextMessage){
                            TextMessage message = (TextMessage) object;
                            System.out.println("TextMessage found: " + message.getMessage());
                            System.out.println("Sent by: " + message.getSender().getUsername());
                            notifyServer.firePropertyChange("New message",null,message);
                            for(int i = 0; i<message.getRecievers().size(); i++){
                                User singleReciever = message.getRecievers().get(i);
                               // System.out.println(singleReciever.getUsername());
                               // System.out.println("Sent to: " + singleReciever.getUsername());
                            }
                            sendMessageToRecievers(message ,message.getRecievers());
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
                try {
                    oos.writeObject(message);
                    oos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
}
}


