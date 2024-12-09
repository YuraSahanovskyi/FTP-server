package org.example.connection.state;

import java.io.IOException;

public interface ConnectionState {
    void handleCommand(String line) throws IOException;
}
