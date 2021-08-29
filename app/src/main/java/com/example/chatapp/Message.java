package com.example.chatapp;

public class Message {

    String message;
    String senderId;
    long timestamp;
    String currenttiime;


    public Message() {
    }



    public Message(String message, String senderId, long timestamp, String currenttiime) {
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.currenttiime = currenttiime;

    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCurrenttiime() {
        return currenttiime;
    }

    public void setCurrenttiime(String currenttiime) {
        this.currenttiime = currenttiime;
    }
}






