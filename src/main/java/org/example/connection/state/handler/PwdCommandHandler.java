package org.example.connection.state.handler;

import org.example.connection.state.ConnectionContext;
import org.example.connection.state.handler.logging.CommandVisitor;

public class PwdCommandHandler extends AbstractCommandHandler {

    public PwdCommandHandler(ConnectionContext context) {
        super(context);
    }

    @Override
    protected void execute(String line) {
        context.getOut().println("257 \"" + context.getCurrentDirectory() + "\" is the current directory");
    }

    @Override
    public void accept(CommandVisitor visitor) {
        visitor.visit(this);
    }
}

