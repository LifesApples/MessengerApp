package Client.View;

import Client.Controller.Controller;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ContactPanel extends JPanel implements ActionListener {
    private Controller controller;
    private JPanel mainPanel;
    private JList onlineUsers, myContacts;
    private JScrollPane o, m;
    private int height;
    private int width;


    public ContactPanel(int width, int height, Controller controller) {
        super(null);
        this.height = height;
        this.width = width;
        this.controller = controller;
        initiateLists();
        initiateButtons();
        initiatePanels();
        updateMyContacts(new String[]{"test1", "test2"});

    }


    public void initiateButtons() {

    }

    public void initiateLists() {
        onlineUsers = new JList();
        onlineUsers.setBorder(BorderFactory.createTitledBorder("Online Users"));
        o = new JScrollPane(onlineUsers);
        o.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);


        myContacts = new JList();
        myContacts.setBorder(BorderFactory.createTitledBorder("My contacts"));
        m = new JScrollPane(myContacts);
        m.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);


        addListener();

    }

    public void initiatePanels() {
        mainPanel = new JPanel(new GridLayout(3, 1));
        mainPanel.setName("Contacts");
        mainPanel.add(m);
        mainPanel.add(o);
        mainPanel.setBounds(0, 0, width - 40, height - 40);

        super.add(mainPanel);

    }

    public int getOnlineUsersIndex() {
        return onlineUsers.getSelectedIndex();
    }

    public int getMyContactsIndex() {
        return myContacts.getSelectedIndex();
    }

    public void updateOnlineUsers(String[] list) {
        onlineUsers.setListData(list);
    }

    public void updateMyContacts(String[] list) {
        myContacts.setListData(list);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void addListener() {
        onlineUsers.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = onlineUsers.getMaxSelectionIndex();
                    if (index > -1) {
                        controller.openChatWindow();
                    }
                }

            }


            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }


        });
        myContacts.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = myContacts.getMaxSelectionIndex();
                    if (index > -1) {
                        controller.openChatWindow();
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }


        });

    }
}
