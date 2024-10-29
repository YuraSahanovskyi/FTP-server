package org.example.states;

import java.io.*;


public class FileTransferState implements ConnectionState {
    private final ConnectionContext context;
    public FileTransferState(ConnectionContext context) {
        this.context = context;
    }
    @Override
    public void handleCommand(String line) throws IOException {
        String command = line.split(" ")[0].toUpperCase();
        switch (command) {
            case "STOR":
                if (context.getDataSocket() == null) {
                    context.getOut().println("425 Use PASV or PORT first.");
                } else {
                    receiveFile(line.substring(5).trim());
                }
                break;
            case "RETR":
                if (context.getDataSocket() == null) {
                    context.getOut().println("425 Use PASV or PORT first.");
                } else {
                    sendFile(line.substring(5).trim());
                }
                break;
        }

    }
    private void receiveFile(String filename) {
        try {
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
        } catch (IOException e) {
            context.getOut().println("550 Failed to receive file.");
        }
        context.setState(new WaitingCommandState(context));
    }

    private void sendFile(String filename) {
        try {
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
        } catch (IOException e) {
            context.getOut().println("550 Failed to send file.");
        }
        context.setState(new WaitingCommandState(context));
    }
}
