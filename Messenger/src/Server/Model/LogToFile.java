package Server.Model;

import Client.Model.User;

import java.io.*;

public class LogToFile {
    private File file;
    private LogEntry logEntry;
    public LogToFile(String path) {
        file = new File(path);
    }


    public LogEntry readEntry() {
        LogEntry entry;
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            entry = (LogEntry) ois.readObject();

            System.out.println("Reading: " + entry);
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
        public void writeEntry(LogEntry logEntry) {
            System.out.println("Saving entry: " + logEntry);

        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
                oos.writeObject(logEntry);
                oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
