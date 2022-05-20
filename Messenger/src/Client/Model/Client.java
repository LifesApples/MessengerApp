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
            user.setStatus(1);
            oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            oos.writeObject(user);

            oos.flush();
            ois = new ObjectInputStream(new BufferedInputStream (socket.getInputStream()));


        } catch (IOException e) {
            e.printStackTrace();
            controller.sendGUIerror("Could not connect to server");
        }

        new Listener().start();

    }

    public void sendMessage(TextMessage message) throws IOException {
        oos.writeObject(message);
        oos.flush();
    }

    public void disconnect() {
        while (user.getStatus() == 1) {
            user.setStatus(0);
            try {
                oos.writeObject(user);
                oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            disconnect();
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



    public void addOnlineUser(User user) {
        if(!onlineUsers.contains(user)){
            onlineUsers.add(user);
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
            while(true) {

                try {

                    Object object = ois.readObject();

                    if (object instanceof User) {
                        User user = (User) object;
                        try {
                            System.out.println("CLient recieved: " + ((User) object).getUsername());
                            Thread.sleep(2500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(user.getStatus()== 1){
                            addOnlineUser(user);
                        }
                        else if(user.getStatus()== 0){
                            //remove user
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
                    e.printStackTrace();

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }




            }
        }
    }
}
