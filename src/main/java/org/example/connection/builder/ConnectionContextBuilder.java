package org.example.connection.builder;

import org.example.connection.state.ConnectionContext;
import org.example.connection.stats.ConnectionInfo;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionContextBuilder implements Builder {
    private  PrintWriter out;
    private  BufferedReader in;
    private  Socket clientSocket;
    private int globalSpeedLimit;
    private int dataPort;
    private ConnectionInfo connectionInfo;
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

    @Override
    public void setGlobalSpeedLimit(int globalSpeedLimit) {
        this.globalSpeedLimit = globalSpeedLimit;
    }

    @Override
    public void setDataPort(int dataPort) {
        this.dataPort = dataPort;
    }

    @Override
    public void setConnectionInfo(ConnectionInfo connectionInfo) {
        this.connectionInfo = connectionInfo;
    }

    public ConnectionContext build() {
        return new ConnectionContext(clientSocket, in, out, globalSpeedLimit, dataPort, connectionInfo);
    }
}
