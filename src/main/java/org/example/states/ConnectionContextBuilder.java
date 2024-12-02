package org.example.states;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionContextBuilder implements Builder {
    private  PrintWriter out;
    private  BufferedReader in;
    private  Socket clientSocket;
    @Override
    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void setBufferedReader(BufferedReader bufferedReader) {
        this.in = bufferedReader;
    }

    @Override
    public void setPrintWriter(PrintWriter printWriter) {
        this.out = printWriter;
    }

    public ConnectionContext build() {
        return new ConnectionContext(clientSocket, in, out);
    }
}
