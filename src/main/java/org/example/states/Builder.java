package org.example.states;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public interface Builder {
    void setClientSocket(Socket clientSocket);
    void setBufferedReader(BufferedReader bufferedReader);
    void setPrintWriter(PrintWriter printWriter);
}
