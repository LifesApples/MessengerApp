package Client.Model;

import Client.Controller.Controller;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ListIterator;

//
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

    public Client(String ip, int port, User myUser, Controller controller) {
        this.ip = ip;
        this.port = port;
        this.myUser = myUser;
        this.controller = controller;
        contacts = new Contacts();
        flag = true;
        Connect();

    }

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


        } catch (IOException e) {
            e.printStackTrace();
            controller.sendGUIerror("Could not connect to server");
        }

        new Listener().start();

    }

    public void sendMessage(TextMessage message) throws IOException {
        System.out.println("Reciever size: " + message.getRecievers());
        oos.writeObject(message);
        oos.flush();
    }

    public void disconnect() {
        System.out.println("Disconnecting");

        myUser.setStatus(0);
        try {
            System.out.println("Sending: " + myUser.getUsername() + " status: " + myUser.getStatus());
            oos.writeObject(myUser);
            oos.flush();
            socket.close();
            flag = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    public void addContact(User user) {
        contacts.addContact(user);
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



    public synchronized void addOnlineUser(User user) {
        System.out.println("running addOnline");
        if(!onlineUsers.contains(user)) {
            if (!this.myUser.getUsername().equals(user.getUsername())) {
                onlineUsers.add(user);
                System.out.println("Adding: " + this.myUser.getUsername() + " to online list");
            }
        }

        controller.setUpOnlineUsersGUI(onlineUsers);
    }

    public synchronized void removeOfflineUser(User user) {
        ListIterator<User> list = onlineUsers.listIterator();
        while(list.hasNext()){
            User nextUser = list.next();
            System.out.println("Loops. next is: " + nextUser.getUsername() + " comparing with: " + user.getUsername());
            if (nextUser.getUsername().equals(user.getUsername())) {
                System.out.println("Removing: " + nextUser);
                list.remove();

            }
        }
        controller.setUpOnlineUsersGUI(onlineUsers);

    }

    public void checkContactOnline(User user) {
        for (User u : contacts.getContactlist()) {
            if (u.getUsername().equals(user.getUsername())) {
                //controller set online
            }
        }
    }
    public class Listener extends Thread {
        public synchronized void run() {
            while(flag) {

                try {

                    Object object = ois.readObject();
                    System.out.println(this.getName() + " somehing came through");

                    if (object instanceof User) {
                        //Om denna använderen är null skapa min användare

                        if(myUser.getUsername().equals(((User) object).getUsername())){
                            System.out.println(this.getName() + "This is about my user. Do something");
                            setMyUser(((User) object));

                            if(myUser.getStatus() == 0){
                                System.out.println(this.getName() + " logged out. Remove them");
                                removeOfflineUser(myUser);
                            }
                        }


                        else {
                            User user = ((User) object);
                            System.out.println(this.getName() + "This is about someone else : " + user.getUsername());
                            if(user.getStatus()== 1){
                                System.out.println(this.getName() + "Adding: " + ((User) object).getUsername() + " to onlinelist");
                                addOnlineUser(user);
                            }
                            else if(user.getStatus()== 0){
                                System.out.println(this.getName() + "Removing: " + ((User) object).getUsername() + " from onlinelist");
                                removeOfflineUser(user);
                            }
                        }



                    }

                    if (object instanceof TextMessage) {
                       controller.appendTextMessageGUI(object);
                        System.out.println("Text message recieved!");
                    }

                    if (object instanceof String) {
                        controller.sendGUIerror((String) object);
                        //Send String to controller (server messages)
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
