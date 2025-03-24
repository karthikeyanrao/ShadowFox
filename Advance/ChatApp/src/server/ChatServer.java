package server;

import common.Message;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {
    private static final int PORT = 5000;
    private ServerSocket serverSocket;
    private final Map<String, ClientHandler> clients = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> chatRooms = new ConcurrentHashMap<>();
    
    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Chat Server started on port " + PORT);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.err.println("Error in server: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private ObjectInputStream input;
        private ObjectOutputStream output;
        private String username;
        private String currentRoom;
        
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            try {
                output = new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                System.err.println("Error creating client handler: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        @Override
        public void run() {
            try {
                while (true) {
                    Message message = (Message) input.readObject();
                    handleMessage(message);
                }
            } catch (EOFException e) {
                // Client disconnected
                handleClientDisconnect();
            } catch (Exception e) {
                System.err.println("Error handling client message: " + e.getMessage());
                e.printStackTrace();
                handleClientDisconnect();
            }
        }
        
        private void handleMessage(Message message) throws IOException {
            switch (message.getType()) {
                case Message.JOIN:
                    handleJoin(message);
                    break;
                case Message.MESSAGE:
                    handleChatMessage(message);
                    break;
                case Message.LEAVE:
                    handleLeave(message);
                    break;
                case Message.JOIN_ROOM:
                    handleJoinRoom(message);
                    break;
            }
        }
        
        private void handleJoin(Message message) throws IOException {
            username = message.getSender();
            clients.put(username, this);
            broadcastUserList();
            broadcastRoomsList();
        }
        
        private void handleChatMessage(Message message) throws IOException {
            if (currentRoom != null) {
                broadcastToRoom(currentRoom, message);
            }
        }
        
        private void handleLeave(Message message) throws IOException {
            handleClientDisconnect();
        }
        
        private void handleJoinRoom(Message message) throws IOException {
            String roomName = message.getRecipient();
            if (currentRoom != null) {
                leaveRoom(currentRoom);
            }
            joinRoom(roomName);
        }
        
        private void handleClientDisconnect() {
            if (username != null) {
                if (currentRoom != null) {
                    try {
                        leaveRoom(currentRoom);
                    } catch (IOException e) {
                        System.err.println("Error leaving room: " + e.getMessage());
                    }
                }
                clients.remove(username);
                try {
                    broadcastUserList();
                } catch (IOException e) {
                    System.err.println("Error broadcasting user list: " + e.getMessage());
                }
            }
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
        
        private void joinRoom(String roomName) throws IOException {
            chatRooms.computeIfAbsent(roomName, k -> ConcurrentHashMap.newKeySet()).add(username);
            currentRoom = roomName;
            broadcastToRoom(roomName, new Message(Message.MESSAGE, "System", 
                username + " joined the room", roomName));
        }
        
        private void leaveRoom(String roomName) throws IOException {
            Set<String> room = chatRooms.get(roomName);
            if (room != null) {
                room.remove(username);
                if (room.isEmpty()) {
                    chatRooms.remove(roomName);
                } else {
                    broadcastToRoom(roomName, new Message(Message.MESSAGE, "System", 
                        username + " left the room", roomName));
                }
            }
            currentRoom = null;
        }
        
        private void broadcastToRoom(String roomName, Message message) throws IOException {
            Set<String> room = chatRooms.get(roomName);
            if (room != null) {
                for (String username : room) {
                    ClientHandler client = clients.get(username);
                    if (client != null) {
                        client.output.writeObject(message);
                    }
                }
            }
        }
        
        private void broadcastUserList() throws IOException {
            List<String> userList = new ArrayList<>(clients.keySet());
            Message message = new Message(Message.USERS_LIST, "Server", 
                String.join(",", userList), "");
            for (ClientHandler client : clients.values()) {
                client.output.writeObject(message);
            }
        }
        
        private void broadcastRoomsList() throws IOException {
            List<String> roomsList = new ArrayList<>(chatRooms.keySet());
            Message message = new Message(Message.ROOMS_LIST, "Server", 
                String.join(",", roomsList), "");
            for (ClientHandler client : clients.values()) {
                client.output.writeObject(message);
            }
        }
    }
    
    public static void main(String[] args) {
        new ChatServer().start();
    }
} 