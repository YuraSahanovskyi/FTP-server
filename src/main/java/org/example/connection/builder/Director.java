package org.example.connection.builder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Director {
    public void buildConnectionContext(Builder builder, Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        builder.setClientSocket(clientSocket);
        builder.setBufferedReader(in);
        builder.setPrintWriter(out);
    }
}
