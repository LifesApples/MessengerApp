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

//Textmessage som en textmessagee-object och user som en string
//Tidsstämpel sköts här
public class Server {

    private int port;

    public Server(int port) throws IOException {
        this.port = port;
        new Connect(port).start();
    }


    private class Connect extends Thread implements PropertyChangeListener {
        private int port;
        private ServerSocket serverSocket;
        private ArrayList<ClientHandler> handlers;
        private ClientHandler handler;

        public Connect(int port) throws IOException {
            this.port = port;
            handlers = new ArrayList<>();
            serverSocket = new ServerSocket(port);

        }

        @Override
        public void run() {
            System.out.println("Server is listening on port: " + port + " Threads: " + Thread.activeCount());
            while (true){
                try(Socket socket = serverSocket.accept()) {
                    if(socket != null){
                        System.out.println("Connection established!");
                        handler = new ClientHandler(socket);
                        handler.start();
                        handlers.add(handler);
                    }
                    else {
                        Thread.yield();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

        }

        private class ClientHandler extends Thread{
            private Socket socket;
            private ObjectOutputStream oos;
            private ObjectInputStream ois;
            private User user;

            //User
            public ClientHandler(Socket socket) {
                String message,response;
                this.socket = socket;
                try {

                    oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                    ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                    Object object = ois.readObject();
                    if(object instanceof User){
                        System.out.println("User object found");
                    }
                    else if(object instanceof TextMessage){
                        System.out.println("User sent text");
                    }

                  //System.out.println("Client: " + user.getUsername() + " connected." +
                    //        " Picture detected: " + user.getProfilePic);
                } catch (EOFException e){

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void run() {
                
            }
        }
    }
}
