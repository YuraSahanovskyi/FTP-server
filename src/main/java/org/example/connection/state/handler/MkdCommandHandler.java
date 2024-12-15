package org.example.connection.state.handler;

import org.example.connection.state.ConnectionContext;
import java.io.File;
import java.io.IOException;

public class MkdCommandHandler extends AbstractCommandHandler {
    public MkdCommandHandler(ConnectionContext context) {
        super(context);
    }
    private File newDir;
    @Override
    protected boolean preconditions(String line) {
        newDir = new File(context.getCurrentDirectory()+ "/" + line.substring(4));
        return !newDir.exists();
    }

    @Override
    protected void execute(String line) throws IOException {
        if (newDir.mkdir()) {
            context.getOut().println("257 \"" + line.substring(4) + "\" directory created.");
        } else {
            throw new IOException("Failed to create directory " + newDir.getAbsolutePath());
        }
    }

    @Override
    protected void handleError(String line) {
        context.getOut().println("550 Failed to create directory.");
    }
}
