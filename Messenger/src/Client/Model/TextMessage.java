package Client.Model;

import javax.swing.*;
import java.io.Serializable;

public class TextMessage implements Serializable {

    private static final long serialVersionUID = 1L;
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
