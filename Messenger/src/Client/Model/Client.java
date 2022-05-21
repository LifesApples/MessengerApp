package Client.Model;

import Client.Controller.Controller;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * Client Class controls connections to server
 * @author Patrik Brandell
 */
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
    private Boolean flag;

    /**
     *
     * @param ip - IP to server
     * @param port - Port to server
     * @param user - Logged in user
     * @param controller - Controller
     */

    public Client(String ip, int port, User user, Controller controller) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.controller = controller;
        contacts = new Contacts();
        flag = true;
        Connect();

    }

    /**
     * Connect to server
     */

    public void Connect()
    {
        try {
            System.out.println("Connecting");
            socket = new Socket(ip, port);
            user.setStatus(1);
            oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            oos.writeObject(user);

            oos.flush();
            ois = new ObjectInputStream(new BufferedInputStream (socket.getInputStream()));


        } catch (IOException e) {}
        new Listener().start();

    }

    /**
     *
     * @param message - Messageobject to send to server
     * @throws IOException
     */

    public void sendMessage(TextMessage message) throws IOException {
        oos.writeObject(message);
        oos.flush();
    }

    /**
     * Disconnect from server
     * set userstatus to 0
     */
    public void disconnect() {
        while (user.getStatus() == 1) {
            user.setStatus(0);
            try {
                oos.writeObject(user);
                oos.flush();
                socket.close();
                flag = false;
            } catch (IOException e) {
            }
        }
    }

    /**
     *
     * @param user - User object to add to contacts
     */
    public void addContact(User user) {
        contacts.addContact(user);
    }

    /**
     *
     * @return contacts class
     */
    public Contacts getContacts() {
        return contacts;
    }

    /**
     *
     * @return Arraylist of User objects
     */
    public ArrayList<User> getOnlineUsers() {
        return onlineUsers;
    }


    /**
     *
     * @param user - Add User object to onlinelist, recieved from server
     */

    public void addOnlineUser(User user) {
        System.out.println(this.user.getUsername());
        System.out.println(user.getUsername());
        if(!onlineUsers.contains(user)) {
            if (!this.user.getUsername().equals(user.getUsername())) {
                onlineUsers.add(user);
            }
        }

        controller.setUpOnlineUsersGUI(onlineUsers);
    }

    /**
     *
     * @param user - Remove user object from onlinelist if recieved as disconnected from server
     */
    public void removeOfflineUser(User user) {
        for (User u : onlineUsers) {
            if (u == user) {
                onlineUsers.remove(u);
            }
        }
    }


    /**
     * Inner Class Listener extends Thread - Listener from server
     */
    public class Listener extends Thread {
        public synchronized void run() {
            while(flag) {

                try {

                    Object object = ois.readObject();

                    if (object instanceof User) {
                        User user = (User) object;
                        System.out.println("CLient recieved: " + ((User) object).getUsername());

                        if(user.getStatus()== 1){
                            addOnlineUser(user);
                        }
                        else if(user.getStatus()== 0){
                            removeOfflineUser(user);
                        }

                    }

                    if (object instanceof TextMessage) {
                       controller.appendTextMessageGUI(object);
                        System.out.println("Text message recieved!");
                    }


                } catch (IOException e) {


                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }




            }
        }
    }
}
