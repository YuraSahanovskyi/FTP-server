package org.example.connection.state;

import java.io.IOException;

interface ConnectionState {
    void handleCommand(String line) throws IOException;
}
