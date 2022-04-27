package Model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.util.ArrayList;

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
            private DataOutputStream dos;
            private DataInputStream dis;

            public ClientHandler(Socket socket) {
                String message,response;
                this.socket = socket;
                try {
                    dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                    dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                    message = dis.readUTF();
                    System.out.println("New client is: " + message);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void run() {
                
            }
        }
    }
}
