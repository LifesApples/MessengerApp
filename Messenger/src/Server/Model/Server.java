package Server.Model;

import Client.Model.TextMessage;
import Client.Model.User;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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


    private class Connect extends Thread {
        private int port;
        private ServerSocket serverSocket;
        //private ArrayList<ClientHandler> handlers;
        private HashMap userlist;
        public Connect(int port) throws IOException {
            this.port = port;
            userlist = new HashMap();
            //handlers = new ArrayList<>();

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
                    new ClientHandler(socket);
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

        private class ClientHandler extends Thread{
            private Socket socket;
            private ObjectOutputStream oos;
            private ObjectInputStream ois;
            private User u1;
            private User u2;
            private User u3;

            public ClientHandler(Socket socket) throws IOException {
                String message, response;
                this.socket = socket;
                ois = new ObjectInputStream(socket.getInputStream());
                u1 = new User("Sam");
                u2 = new User("Azam");
                u3 = new User("Per");
                start();
            }

            public void sendMessageToRecievers(ArrayList<User> recievers){
                recievers.add(u1);
                recievers.add(u2);

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
                            User user = (User) object;
                            System.out.println("User tried logging in");
                            userlist.put(user,user.getUsername());
                            log.add("User" + user.getUsername() + " logged in");
                            //userlist.put(u1,user.getUsername());
                            //userlist.put(u2,user.getUsername());
                            //userlist.put(u3,user.getUsername());
                            //System.out.println("Active users: " + userlist.values());
                        }
                        else if(object instanceof TextMessage){
                            TextMessage message = (TextMessage) object;
                            //System.out.println("TextMessage found: " + message.getMessage());
                            //System.out.println("Sent by: " + message.getSender());
                            //System.out.println("Sent to: " + message.getRecievers());
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
            }
        }
}


