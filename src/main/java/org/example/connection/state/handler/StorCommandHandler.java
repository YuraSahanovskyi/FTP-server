package org.example.connection.state.handler;

import org.example.authentication.Permission;
import org.example.connection.state.ConnectionContext;
import org.example.connection.state.handler.limiter.ThrottledInputStream;

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

        long startTime = System.nanoTime();

        BufferedInputStream dataIn = new BufferedInputStream(context.getDataSocket().getInputStream());
        FileOutputStream fileOut = new FileOutputStream(new File(context.getCurrentDirectory(), filename));
        ThrottledInputStream throttledDataIn = new ThrottledInputStream(dataIn, context.getUser().getSpeedLimit(), context.getGlobalSpeedLimit());

        byte[] buffer = new byte[4096];
        int bytesRead;
        long totalBytesRead = 0;

        while ((bytesRead = throttledDataIn.read(buffer)) != -1) {
            fileOut.write(buffer, 0, bytesRead);
            totalBytesRead += bytesRead;
        }

        fileOut.close();
        throttledDataIn.close();
        context.getDataSocket().close();

        long endTime = System.nanoTime();
        long durationInNanoSeconds = endTime - startTime;
        double durationInSeconds = durationInNanoSeconds / 1_000_000_000.0;
        double speedInBytesPerSecond = totalBytesRead / durationInSeconds;
        context.getOut().println(String.format("226 Transfer complete. Time: %.2f seconds, Speed: %.2f bytes/sec.", durationInSeconds, speedInBytesPerSecond));
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
