package Client.Model;

import Client.Controller.Controller;

import java.io.*;
import java.util.ArrayList;

public class Contacts {

    private ArrayList<User> contactlist;
    private File contactfile;
    private final String filename = "contacts";

    public Contacts() {
        contactlist = new ArrayList<>();
        contactfile = new File("files/contacts.dat");

        if (contactfile.exists()) {
            try {
                readContactfile();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                contactfile.createNewFile();
                System.out.println(contactfile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void readContactfile()  throws IOException, ClassNotFoundException{
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(contactfile)))) {
                int n = ois.readInt();
                for (int i = 0; i < n; i++) {
                    contactlist.add((User) ois.readObject());
            }
        }
        catch (EOFException e) {

        }

    }

    public void writeContactfile() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(contactfile)))) {
            oos.writeInt(contactlist.size());
            for (User u : contactlist) {
                oos.writeObject(u);
                oos.flush();
            }
        }
    }

    public ArrayList<User> getContactlist() {
        return contactlist;
    }

    public void setContactlist(ArrayList<User> contactlist) {
        this.contactlist = contactlist;
    }
}

