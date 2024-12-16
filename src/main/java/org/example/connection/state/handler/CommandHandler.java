package org.example.connection.state.handler;

public interface CommandHandler {
    void accept(CommandVisitor visitor);
    String getUsername();
}
