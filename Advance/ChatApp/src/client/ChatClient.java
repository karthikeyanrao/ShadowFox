package client;

import common.Message;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatClient extends JFrame {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5000;
    
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String username;
    private String currentRoom;
    
    private JTextArea chatArea;
    private JTextField messageField;
    private JList<String> usersList;
    private JList<String> roomsList;
    private DefaultListModel<String> usersModel;
    private DefaultListModel<String> roomsModel;
    
    public ChatClient() {
        setTitle("Chat Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Initialize components
        initializeComponents();
        
        // Connect to server
        connectToServer();
    }
    
    private void initializeComponents() {
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Chat area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        
        // Message input
        JPanel messagePanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        JButton sendButton = new JButton("Send");
        messagePanel.add(messageField, BorderLayout.CENTER);
        messagePanel.add(sendButton, BorderLayout.EAST);
        
        // Users and rooms panel
        JPanel sidePanel = new JPanel(new GridLayout(2, 1));
        usersModel = new DefaultListModel<>();
        roomsModel = new DefaultListModel<>();
        usersList = new JList<>(usersModel);
        roomsList = new JList<>(roomsModel);
        
        JPanel usersPanel = new JPanel(new BorderLayout());
        usersPanel.add(new JLabel("Users"), BorderLayout.NORTH);
        usersPanel.add(new JScrollPane(usersList));
        
        JPanel roomsPanel = new JPanel(new BorderLayout());
        roomsPanel.add(new JLabel("Chat Rooms"), BorderLayout.NORTH);
        roomsPanel.add(new JScrollPane(roomsList));
        
        sidePanel.add(usersPanel);
        sidePanel.add(roomsPanel);
        
        // Add components to main panel
        mainPanel.add(chatScrollPane, BorderLayout.CENTER);
        mainPanel.add(messagePanel, BorderLayout.SOUTH);
        mainPanel.add(sidePanel, BorderLayout.EAST);
        
        // Add event listeners
        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());
        roomsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedRoom = roomsList.getSelectedValue();
                    if (selectedRoom != null) {
                        joinRoom(selectedRoom);
                    }
                }
            }
        });
        
        add(mainPanel);
    }
    
    private void connectToServer() {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            
            // Get username
            username = JOptionPane.showInputDialog(this, "Enter your username:");
            if (username == null || username.trim().isEmpty()) {
                username = "User" + System.currentTimeMillis();
            }
            
            // Send join message
            output.writeObject(new Message(Message.JOIN, username, "", ""));
            
            // Start message listener thread
            new Thread(this::listenForMessages).start();
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not connect to server: " + e.getMessage());
            System.exit(1);
        }
    }
    
    private void listenForMessages() {
        try {
            while (true) {
                Message message = (Message) input.readObject();
                handleMessage(message);
            }
        } catch (EOFException e) {
            // Server disconnected
            handleServerDisconnect();
        } catch (Exception e) {
            System.err.println("Error receiving message: " + e.getMessage());
            e.printStackTrace();
            handleServerDisconnect();
        }
    }
    
    private void handleMessage(Message message) {
        SwingUtilities.invokeLater(() -> {
            switch (message.getType()) {
                case Message.MESSAGE:
                    chatArea.append(message.toString() + "\n");
                    break;
                case Message.USERS_LIST:
                    updateUsersList(message.getContent());
                    break;
                case Message.ROOMS_LIST:
                    updateRoomsList(message.getContent());
                    break;
            }
        });
    }
    
    private void updateUsersList(String content) {
        usersModel.clear();
        if (!content.isEmpty()) {
            String[] users = content.split(",");
            for (String user : users) {
                if (!user.equals(username)) {
                    usersModel.addElement(user);
                }
            }
        }
    }
    
    private void updateRoomsList(String content) {
        roomsModel.clear();
        if (!content.isEmpty()) {
            String[] rooms = content.split(",");
            for (String room : rooms) {
                if (!room.equals(currentRoom)) {
                    roomsModel.addElement(room);
                }
            }
        }
    }
    
    private void sendMessage() {
        String content = messageField.getText().trim();
        if (!content.isEmpty()) {
            try {
                output.writeObject(new Message(Message.MESSAGE, username, content, currentRoom));
                messageField.setText("");
            } catch (IOException e) {
                System.err.println("Error sending message: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private void joinRoom(String roomName) {
        try {
            output.writeObject(new Message(Message.JOIN_ROOM, username, "", roomName));
            currentRoom = roomName;
            chatArea.append("Joined room: " + roomName + "\n");
        } catch (IOException e) {
            System.err.println("Error joining room: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleServerDisconnect() {
        SwingUtilities.invokeLater(() -> {
            chatArea.append("Disconnected from server\n");
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        });
    }
    
    @Override
    public void dispose() {
        try {
            if (socket != null && !socket.isClosed()) {
                output.writeObject(new Message(Message.LEAVE, username, "", ""));
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
        super.dispose();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChatClient().setVisible(true);
        });
    }
} 