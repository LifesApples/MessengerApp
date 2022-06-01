package Client.Model;

import Client.Controller.Controller;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.ListIterator;

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
    private User myUser;
    private Controller controller;
    private Contacts contacts;
    private ArrayList<User> onlineUsers = new ArrayList<>();
    private Boolean flag;

    /**
     *
     * @param ip - IP to server
     * @param port - Port to server
     * @param myUser - Logged in user
     * @param controller - Controller
     */

    public Client(String ip, int port, User myUser, Controller controller) {
        this.ip = ip;
        this.port = port;
        this.myUser = myUser;
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
            myUser.setStatus(1);
            oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            oos.writeObject(myUser);

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

        myUser.setStatus(0);
        try {
            oos.writeObject(myUser);
            oos.flush();
            socket.close();
            flag = false;
            } catch (IOException e) {
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

    public synchronized void addOnlineUser(User user) {
        if(!onlineUsers.contains(user)) {
            if (!this.myUser.getUsername().equals(user.getUsername())) {
                onlineUsers.add(user);
            }
        }

        controller.setUpOnlineUsersGUI(onlineUsers);
    }
  
   /**
     *
     * @param user - Remove user object from onlinelist if recieved as disconnected from server
     */
  
    public synchronized void removeOfflineUser(User user) {
        ListIterator<User> list = onlineUsers.listIterator();
        while(list.hasNext()){
            User nextUser = list.next();
            if (nextUser.getUsername().equals(user.getUsername())) {
                list.remove();

            }
        }
        controller.setUpOnlineUsersGUI(onlineUsers);

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
                        //If user object is my user
                        if(myUser.getUsername().equals(((User) object).getUsername())){
                            setMyUser(((User) object));

                            if(myUser.getStatus() == 0){
                                removeOfflineUser(myUser);
                            }
                        }

                        //If user object is someoneelse
                        else {
                            User user = ((User) object);
                            if(user.getStatus()== 1){
                                addOnlineUser(user);
                            }
                            else if(user.getStatus()== 0){
                                removeOfflineUser(user);
                            }
                        }



                    }

                    if (object instanceof TextMessage) {
                       controller.appendTextMessageGUI(object);
                        System.out.println("Text message recieved!");
                    }


                } catch (IOException e) {

                    System.out.println("Thread found error: " + this.getName());
                    e.printStackTrace();

                } catch (ClassNotFoundException e) {
                    System.out.println("Thread found error: " + this.getName());
                    e.printStackTrace();
                }




            }
        }

    }

    public User getMyUser() {
        return myUser;
    }

    public void setMyUser(User user) {
        this.myUser = user;
    }
}
