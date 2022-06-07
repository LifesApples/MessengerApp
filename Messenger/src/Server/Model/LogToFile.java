package Server.Model;

import Client.Model.User;

import java.io.*;

public class LogToFile {
    private File file;
    private LogEntry logEntry;

    /**
     * Class used for storing log in a file. The entries are created using the LogEntry class.
     * @param path
     */
    public LogToFile(String path) {
        file = new File(path);
    }


    /**
     * Used for reading the file from the harddrive
     * @return
     */
    public LogEntry readEntry() {
        LogEntry entry;
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            entry = (LogEntry) ois.readObject();

            return entry;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Used for writing the file to the harddrive
     * @param logEntry
     */
        public void writeEntry(LogEntry logEntry) {

        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
                oos.writeObject(logEntry);
                oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
