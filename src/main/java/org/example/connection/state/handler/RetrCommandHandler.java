package org.example.connection.state.handler;

import org.example.authentication.Permission;
import org.example.connection.state.ConnectionContext;
import org.example.connection.state.handler.limiter.ThrottledOutputStream;
import org.example.connection.state.handler.logging.CommandVisitor;

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

        long startTime = System.nanoTime();

        context.getOut().println("150 Opening data connection for " + filename);

        BufferedOutputStream dataOut = new BufferedOutputStream(context.getDataSocket().getOutputStream());
        FileInputStream fileIn = new FileInputStream(file);

        ThrottledOutputStream throttledDataOut = new ThrottledOutputStream(dataOut, context.getUser().getSpeedLimit(), context.getGlobalSpeedLimit());

        byte[] buffer = new byte[4096];
        int bytesRead;
        long totalBytesSent = 0;

        while ((bytesRead = fileIn.read(buffer)) != -1) {
            throttledDataOut.write(buffer, 0, bytesRead);
            totalBytesSent += bytesRead;
        }

        throttledDataOut.flush();
        throttledDataOut.close();
        fileIn.close();
        context.getDataSocket().close();

        context.getConnectionInfo().addBytesSent(totalBytesSent);

        long endTime = System.nanoTime();
        long durationInNanoSeconds = endTime - startTime;
        double durationInSeconds = durationInNanoSeconds / 1_000_000_000.0;
        double speedInBytesPerSecond = totalBytesSent / durationInSeconds;

        context.getOut().println(String.format("226 Transfer complete. Time: %.2f seconds, Speed: %.2f bytes/sec.", durationInSeconds, speedInBytesPerSecond));
    }

    @Override
    protected void handleError(String line) {
        context.getOut().println("550 Failed to send file.");
    }

    @Override
    public void accept(CommandVisitor visitor) {
        visitor.visit(this);
    }
}
