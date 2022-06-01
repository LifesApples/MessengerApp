package Server.Model;

import Client.Model.User;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogEntry implements Serializable{
    private String date;
    private String entry;
    private File file;
    private static final long serialVersionUID = 2308363320199130613L;

    public LogEntry(String date, String entry) {
        this.date = date;
        this.entry = entry;
    }
    public String getDate() {
       return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }


    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        String text = date + " - " + entry;
        return text;
    }
    private class logToFile{

    }



}

