package Client.Model;


import java.io.*;
import java.util.ArrayList;

/**
 * Class Contacts handles contact list och file
 * @author Patrik Brandell
 */

public class Contacts {

    private final String filename = "contacts";
    private ArrayList<User> contactlist;
    private File contactfile;


    /**
     * Contacts constructor checks for contactfile, create if it doesn't exist
     */
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

    /**
     * Reads contactfile from disk
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void readContactfile() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(contactfile)))) {
            int n = ois.readInt();
            for (int i = 0; i < n; i++) {
                contactlist.add((User) ois.readObject());
            }
        } catch (EOFException e) {

        }

    }

    /**
     * Writes to contactfile
     * @throws IOException
     */

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

    /**
     *
     * @param user - Add User object to contactlist if it doesn't exist
     */
    public void addContact(User user) {
        Boolean exist = false;
        for (User u : contactlist) {
            if (u.getUsername().equals(user.getUsername())) {
                exist = true;
            }
        }
        if (!exist)
            contactlist.add(user);
        try {
            writeContactfile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param user - Remove User object from contactlist
     */
    public void removeContact(User user) {
        User userToRemove = null;
        for (User u : contactlist) {
            if (u.getUsername().equals(user.getUsername())) {
                userToRemove = u;
            }
        }
        if (userToRemove != null) {
            try {
                contactlist.remove(userToRemove);
                writeContactfile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}



