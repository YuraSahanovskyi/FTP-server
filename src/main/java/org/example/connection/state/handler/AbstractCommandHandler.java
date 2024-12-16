package org.example.connection.state.handler;

import org.example.connection.state.ConnectionContext;

import java.io.IOException;

public abstract class AbstractCommandHandler implements CommandHandler {
    protected final ConnectionContext context;

    protected AbstractCommandHandler(ConnectionContext context) {
        this.context = context;
    }


    public final void handle(String line) {
        if (!preconditions(line)) {
            handleError(line);
            return;
        }
        try {
            execute(line);
        } catch (Exception e) {
            handleError(line);
        }
    }

    protected boolean preconditions(String line) {
        return true;
    }
    protected abstract void execute(String line) throws IOException;
    protected void handleError(String line) {}

    @Override
    public String getUsername() {
        return context.getUser().getUsername();
    }
}
