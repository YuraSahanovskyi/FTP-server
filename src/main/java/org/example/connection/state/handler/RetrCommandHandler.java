package org.example.connection.state.handler;

import org.example.authentication.Permission;
import org.example.connection.state.ConnectionContext;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class RetrCommandHandler extends AbstractCommandHandler {
    public RetrCommandHandler(ConnectionContext context) {
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
                .anyMatch(Permission::isRead);
    }

    @Override
    protected void execute(String line) throws IOException {
        String filename = line.substring(5).trim();  // отримуємо ім'я файлу з команди
        File file = new File(context.getCurrentDirectory(), filename);
            if (!file.exists() || file.isDirectory()) {
                context.getOut().println("550 File not found.");
                return;
            }

            context.getOut().println("150 Opening data connection for " + filename);
            BufferedOutputStream dataOut = new BufferedOutputStream(context.getDataSocket().getOutputStream());
            FileInputStream fileIn = new FileInputStream(file);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileIn.read(buffer)) != -1) {
                dataOut.write(buffer, 0, bytesRead);
            }

            dataOut.flush();
            dataOut.close();
            fileIn.close();
            context.getDataSocket().close();
            context.getOut().println("226 Transfer complete.");
    }
    @Override
    protected void handleError(String line) {
        context.getOut().println("550 Failed to send file.");
    }
}

