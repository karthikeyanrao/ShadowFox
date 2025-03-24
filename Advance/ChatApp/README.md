# Real-time Chat Application

A Java-based real-time chat application that allows multiple users to join chat rooms and exchange messages instantly.

## Features

- Multiple chat rooms support
- Real-time user list updates
- Real-time chat room list updates
- Graphical user interface
- System messages for user join/leave events

## Requirements

- Java 8 or higher
- Java Development Kit (JDK)

## How to Run

1. Compile the source files:
   ```bash
   javac -d bin src/common/*.java src/server/*.java src/client/*.java
   ```

2. Start the server:
   ```bash
   java -cp bin server.ChatServer
   ```

3. Start one or more clients:
   ```bash
   java -cp bin client.ChatClient
   ```

## Usage

1. When you start a client, you'll be prompted to enter a username
2. The client window will show:
   - A list of online users on the right
   - A list of available chat rooms
   - A chat area in the center
   - A message input field at the bottom

3. To join a chat room:
   - Double-click on a room name from the rooms list
   - If the room doesn't exist, it will be created

4. To send a message:
   - Type your message in the input field
   - Press Enter or click the Send button

5. To leave the application:
   - Simply close the client window

## Project Structure

```
ChatApp/
├── src/
│   ├── common/
│   │   └── Message.java
│   ├── server/
│   │   └── ChatServer.java
│   └── client/
│       └── ChatClient.java
└── README.md
```

## Notes

- The server runs on localhost:5000 by default
- Each client can join only one chat room at a time
- Messages are broadcast to all users in the same chat room
- System messages indicate when users join or leave rooms 