package org.example.connection.state.handler.logging;

public interface CommandHandler {
    void accept(CommandVisitor visitor);
    String getUsername();
}
