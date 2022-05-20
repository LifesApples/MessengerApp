package Server.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogEntry {
    private String date;
    private String entry;

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

    @Override
    public String toString() {
        String text = date + " - " + entry;
        return text;
    }


}
