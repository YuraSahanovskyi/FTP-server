package org.example.connection.builder;

import org.example.connection.stats.ConnectionInfo;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public interface Builder {
    void setClientSocket(Socket clientSocket);
    void setBufferedReader(BufferedReader bufferedReader);
    void setPrintWriter(PrintWriter printWriter);
    void setGlobalSpeedLimit(int globalSpeedLimit);
    void setDataPort(int dataPort);
    void setConnectionInfo(ConnectionInfo connectionInfo);
}
