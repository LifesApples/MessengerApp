package Client.Model;

import java.io.*;
import java.net.Socket;

//
public class Client {
    private String ip;
    private int port;
    private Socket socket;
    private DataOutputStream dos;
    private User user;

    public Client(String ip, int port, User user) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        Connect();
    }

    public void Connect()
    {
        try {
            socket = new Socket(ip, port);
            dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            dos.writeUTF(user.getUsername());
            dos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
