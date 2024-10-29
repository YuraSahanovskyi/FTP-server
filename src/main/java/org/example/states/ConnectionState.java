package org.example.states;

import java.io.IOException;

interface ConnectionState {
    void handleCommand(String line) throws IOException;
}
