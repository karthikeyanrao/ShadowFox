package common;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A serializable Message class that represents messages exchanged in the chat application
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Types of messages
    public static final int JOIN = 0;
    public static final int MESSAGE = 1;
    public static final int LEAVE = 2;
    public static final int USERS_LIST = 3;
    public static final int ROOMS_LIST = 4;
    public static final int JOIN_ROOM = 5;
    
    private int type;
    private String sender;
    private String content;
    private String recipient; // Can be a user or room name
    private String timestamp;
    
    // Constructor for general messages
    public Message(int type, String sender, String content, String recipient) {
        this.type = type;
        this.sender = sender;
        this.content = content;
        this.recipient = recipient;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    // Getters and setters
    public int getType() {
        return type;
    }
    
    public String getSender() {
        return sender;
    }
    
    public String getContent() {
        return content;
    }
    
    public String getRecipient() {
        return recipient;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s: %s", timestamp, sender, content);
    }
} 