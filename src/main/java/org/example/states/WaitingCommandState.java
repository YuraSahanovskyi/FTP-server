package org.example.states;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WaitingCommandState implements ConnectionState {
    protected ConnectionContext context;

    public WaitingCommandState(ConnectionContext context) {
        this.context = context;
    }


    protected void handlePASV() throws IOException {
        if (context.getDataServerSocket() != null) {
            context.getDataServerSocket().close();
        }
        context.setDataServerSocket(new ServerSocket(0));
        int port = context.getDataServerSocket().getLocalPort();
        String ip = context.getClientSocket().getLocalAddress().getHostAddress().replace('.', ',');
        int p1 = port / 256;
        int p2 = port % 256;
        context.getOut().println("227 Entering Passive Mode (" + ip + "," + p1 + "," + p2 + ")");
        context.setDataSocket(context.getDataServerSocket().accept());
    }

    protected void handlePORT(String line) throws IOException {
        if (context.getDataSocket() != null) {
            context.getDataSocket().close();
        }
        String[] parts = line.substring(5).split(",");
        String ip = parts[0] + "." + parts[1] + "." + parts[2] + "." + parts[3];
        int port = Integer.parseInt(parts[4]) * 256 + Integer.parseInt(parts[5]);
        context.setDataSocket(new Socket(ip, port));
        context.getOut().println("200 Command okay.");
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

    @Override
    public void handleCommand(String line) throws IOException {
        String command = line.split(" ")[0].toUpperCase();
        switch (command) {
            case "PWD":
                context.getOut().println("257 \"" + context.getCurrentDirectory() + "\" is the current directory");
                break;
            case "CWD":
                String path = line.substring(4).trim();
                Path newPath = Paths.get(context.getCurrentDirectory(), path).normalize();
                if (Files.exists(newPath) && Files.isDirectory(newPath)) {
                    context.setCurrentDirectory(newPath.toString());
                    context.getOut().println("250 Directory successfully changed.");
                } else {
                    context.getOut().println("550 Failed to change directory.");
                }
                break;
            case "LIST":
                if (context.getDataSocket() == null) {
                    context.getOut().println("425 Use PASV or PORT first.");
                } else {
                    sendDirectoryListing();
                }
                break;
            case "PASV":
                handlePASV();
                break;
            case "PORT":
                handlePORT(line);
                break;
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
            case "QUIT":
                context.getOut().println("221 Service closing control connection");
                break;
            default:
                context.getOut().println("502 Command not implemented");
                break;

        }
    }
}