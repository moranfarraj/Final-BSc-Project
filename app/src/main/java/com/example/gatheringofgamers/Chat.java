package com.example.gatheringofgamers;

public class Chat {
    private String username;
    private String lastMessage;
    private String profileImageUrl; // URL of the profile image, if applicable

    // You may also want to include other properties, such as a timestamp

    public Chat(String username, String lastMessage, String profileImageUrl) {
        this.username = username;
        this.lastMessage = lastMessage;
        this.profileImageUrl = profileImageUrl;
    }

    // Getters and setters for each property

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
