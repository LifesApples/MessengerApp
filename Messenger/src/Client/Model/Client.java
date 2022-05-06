package Client.Model;

import Client.Controller.Controller;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

//
public class Client {
    private String ip;
    private int port;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private User user;
    private Controller controller;
    private Contacts contacts;
    private ArrayList<User> onlineUsers = new ArrayList<>();

    public Client(String ip, int port, User user, Controller controller) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.controller = controller;
        contacts = new Contacts();
        Connect();
    }

    public void Connect()
    {
        try {
            System.out.println("Connecting");
            socket = new Socket(ip, port);

            oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            oos.writeObject(user);
            ois = new ObjectInputStream(new BufferedInputStream (socket.getInputStream()));


        } catch (IOException e) {
            e.printStackTrace();
            controller.sendGUIerror("Could not connect to server");
        }

        new Listener().start();

    }

    public void sendMessage(TextMessage message) throws IOException {
        oos.writeObject(message);
    }

    public void disconnect() {
        System.out.println("Disconnect");
        disconnect();
    }

    public void addContact(User user) {
        contacts.addContact(user);
    }

    public void removeContact(User user) {
        contacts.removeContact(user);
    }
    public Contacts getContacts() {
        return contacts;
    }

    public void setContacts(Contacts contacts) {
        this.contacts = contacts;
    }

    public ArrayList<User> getOnlineUsers() {
        return onlineUsers;
    }

    public void setOnlineUsers(ArrayList<User> onlineUsers) {
        this.onlineUsers = onlineUsers;
    }

    public void addOnlineUser(User user) {
        onlineUsers.add(user);
        controller.setUpOnlineUsersGUI(onlineUsers);
    }

    public class Listener extends Thread {
        public synchronized void run() {
            while(true) {

                try {

                    Object object = ois.readObject();

                    if (object instanceof User) {
                        addOnlineUser((User) object);
                    }

                    if (object instanceof TextMessage) {
                        //controller append message in gui
                    }

                    if (object instanceof String) {
                        controller.sendGUIerror((String) object);
                        //Send String to controller (server messages)
                    }

                } catch (IOException e) {
                    e.printStackTrace();

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }




            }
        }
    }
}
