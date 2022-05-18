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

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            System.out.println("PCE was fired to server");
            if(evt.getPropertyName().equals("New user")){

                for(ClientHandler handler : handlers){
                    System.out.println("Sending notice to handlers");
                    handler.addOnlineUser();
                }
            }

        }

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

            public void sendMessageToRecievers(ArrayList<User> recievers){

                System.out.println("Checking recieves: " + recievers.size());
                //Loop through recievers
                for (int i = 0; i<recievers.size(); i++){
                    //Loop through online users
                    for (var entry : userlist.keySet()){
                        if (recievers.get(i) == entry){
                            User user = (User) entry;
                            System.out.println("Sending to " + user.getUsername());
                        }
                        //User is offline if no match has been found in the userlist
                        //TO do: Add message to offline messages for specifik users
                        else {
                            //System.out.println("Reciever(s) offline");
                        }
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
                            
                            for(int i = 0; i<message.getRecievers().size(); i++){
                                User singleReciever = message.getRecievers().get(i);
                                System.out.println(singleReciever.getUsername());
                                System.out.println("Sent to: " + message.getRecievers());
                            }
                            sendMessageToRecievers(message.getRecievers());
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
                    Next User: aa

                try {
                    System.out.println("New user: " + ((User) user1).getUsername()+" is in list sending to stream." );
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


        }
}
}


