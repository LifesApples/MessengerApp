package Client.Model;

import javax.swing.*;

public class TextMessage {

    private String message;
    private Icon icon;

    public TextMessage (String message, Icon icon) {
        this.message = message;
        this.icon = icon;
    }
    public TextMessage (String message) {
        this.message = message;
    }

    public TextMessage (Icon icon) {
        this.icon = icon;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }
}
