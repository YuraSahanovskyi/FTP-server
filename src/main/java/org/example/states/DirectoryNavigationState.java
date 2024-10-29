package org.example.states;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirectoryNavigationState implements ConnectionState{
    private final ConnectionContext context;
    public DirectoryNavigationState(ConnectionContext context) {
        this.context = context;
    }
    private void sendDirectoryListing() throws IOException {
        File dir = new File(context.getCurrentDirectory());
        File[] files = dir.listFiles();
        if (files != null) {
            context.getOut().println("150 Here comes the directory listing.");
            BufferedWriter dataOut = new BufferedWriter(new OutputStreamWriter(context.getDataSocket().getOutputStream()));
            for (File file : files) {
                dataOut.write(file.getName() + "\r\n");
            }
            dataOut.flush();
            dataOut.close();
            context.getDataSocket().close();
            context.getOut().println("226 Directory send OK.");
        } else {
            context.getOut().println("550 Failed to list directory.");
        }
    }

    private void printWorkingDirectory() {
        context.getOut().println("257 \"" + context.getCurrentDirectory() + "\" is the current directory");
    }
    private void changeWorkingDirectory(String line) {
        String path = line.substring(4).trim();
        Path newPath = Paths.get(context.getCurrentDirectory(), path).normalize();
        if (Files.exists(newPath) && Files.isDirectory(newPath)) {
            context.setCurrentDirectory(newPath.toString());
            context.getOut().println("250 Directory successfully changed.");
        } else {
            context.getOut().println("550 Failed to change directory.");
        }
    }
    @Override
    public void handleCommand(String line) throws IOException {
        String command = line.split(" ")[0].toUpperCase();
        switch (command) {
            case "PWD":
                printWorkingDirectory();
                break;
            case "CWD":
                changeWorkingDirectory(line);
                break;
            case "LIST":
                if (context.getDataSocket() == null) {
                    context.getOut().println("425 Use PASV or PORT first.");
                } else {
                    sendDirectoryListing();
                }
                break;
        }
        context.setState(new WaitingCommandState(context));
    }
}
