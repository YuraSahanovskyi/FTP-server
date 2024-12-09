package org.example.connection.state;

import org.example.connection.state.handler.*;

import java.util.HashMap;
import java.util.Map;

public class FileOperationsState implements ConnectionState {
    private final ConnectionContext context;
    private final Map<String, AbstractCommandHandler> commandHandlers;

    public FileOperationsState(ConnectionContext context) {
        this.context = context;
        this.commandHandlers = new HashMap<>();
        commandHandlers.put("STOR", new StorCommandHandler(context));
        commandHandlers.put("RETR", new RetrCommandHandler(context));
        commandHandlers.put("PWD", new PwdCommandHandler(context));
        commandHandlers.put("CWD", new CwdCommandHandler(context));
        commandHandlers.put("LIST", new ListCommandHandler(context));
    }

    @Override
    public void handleCommand(String line) {
        String command = line.split(" ")[0].toUpperCase();
        AbstractCommandHandler handler = commandHandlers.get(command);
        if (handler != null) {
            handler.handle(line);
        } else {
            context.getOut().println("502 Command not implemented");
        }
        context.setState(new WaitingCommandState(context));
    }
}
