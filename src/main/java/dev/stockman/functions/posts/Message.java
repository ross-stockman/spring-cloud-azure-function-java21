package dev.stockman.functions.posts;

import jakarta.validation.constraints.NotNull;

public class Message{
    @NotNull
    private String message;

    public Message(){}

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                '}';
    }
}
