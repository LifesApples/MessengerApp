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
    private JPanel mainPanel, buttonPanel, listPanel;
    private JList onlineUsers, myContacts;
    private JScrollPane o, m;
    private int height;
    private int width;
    private JButton btnAddContact, btnRemoveContact;
    private String userMarked;


    public ContactPanel(int width, int height, Controller controller) {
        super(null);
        this.height = height;
        this.width = width;
        this.controller = controller;
        initiateLists();
        initiateButtons();
        initiatePanels();
        updateOnlineUsers(new String[100]);


    }


    public void initiateButtons() {
        btnAddContact = new JButton("Add Contact");
        btnAddContact.addActionListener(this);
        btnRemoveContact = new JButton("Remove Contact");
        btnRemoveContact.addActionListener(this);

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

        listPanel = new JPanel(new GridLayout(3, 1));
        listPanel.setName("Contacts");
        listPanel.add(m);
        listPanel.add(o);
        listPanel.setBounds(0, 0, width - 40, height - 40);

        buttonPanel = new JPanel(new GridLayout(1,2));
        buttonPanel.add(btnAddContact);
        buttonPanel.add(btnRemoveContact);
        buttonPanel.setBounds(20, height-150, 300, 50);

        mainPanel = new JPanel(null);
        mainPanel.setBounds(0,0, width, height);
        mainPanel.add(listPanel);
        mainPanel.add(buttonPanel);

        super.add(mainPanel);



    }

    public void setContactOnline(int index) {
        myContacts.setSelectedIndex(index);
        myContacts.setSelectionForeground(Color.GREEN);
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
        if (e.getSource() == btnAddContact) {
            controller.addContact(onlineUsers.getSelectedValue().toString());
        }
        if (e.getSource() == btnRemoveContact) {
            controller.removeContact(myContacts.getSelectedValue().toString());
        }

    }

    public void addListener() {
        onlineUsers.addMouseListener(new MouseListener() {
            int index;
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                      index = onlineUsers.getMaxSelectionIndex();
                    if (index > -1) {
                        controller.openChatWindow(onlineUsers.getSelectedValue().toString());
                        System.out.println(onlineUsers.getSelectedValue().toString());
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
                        controller.openChatWindow(myContacts.getSelectedValue().toString());
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
