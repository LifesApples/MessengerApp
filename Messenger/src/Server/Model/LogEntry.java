package Server.Model;

import Client.Model.User;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Class used for creating entries for the log. The entries are created with a String object as the message and dates.
 */
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


    public String getEntry() {
        return entry;
    }


    @Override
    public String toString() {
        String text = date + " - " + entry;
        return text;
    }
    private class logToFile{

    }



}

