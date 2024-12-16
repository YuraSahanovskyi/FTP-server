package org.example.connection.state.handler;

import org.example.authentication.Permission;
import org.example.connection.state.ConnectionContext;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class StorCommandHandler extends AbstractCommandHandler {
    public StorCommandHandler(ConnectionContext context) {
        super(context);
    }

    @Override
    protected boolean preconditions(String line) {
        if (context.getDataSocket() == null) {
            context.getOut().println("425 Use PASV or PORT first.");
            return false;
        }
        return context.getUser().getPermissions().stream()
                .filter(p -> context.getCurrentDirectory().startsWith(p.getPath()))
                .anyMatch(Permission::isWrite);
    }

    @Override
    protected void execute(String line) throws IOException {
        String filename = line.substring(5).trim();
        context.getOut().println("150 Opening data connection for " + filename);
        BufferedInputStream dataIn = new BufferedInputStream(context.getDataSocket().getInputStream());
        FileOutputStream fileOut = new FileOutputStream(new File(context.getCurrentDirectory(), filename));

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = dataIn.read(buffer)) != -1) {
            fileOut.write(buffer, 0, bytesRead);
        }

        fileOut.close();
        context.getDataSocket().close();
        context.getOut().println("226 Transfer complete.");
    }

    @Override
    protected void handleError(String line) {
        context.getOut().println("550 Failed to receive file.");
    }

    @Override
    public void accept(CommandVisitor visitor) {
        visitor.visit(this);
    }
}
