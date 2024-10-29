package org.example.states;

import java.io.IOException;

public interface ConnectionState {
    void handleCommand(String line) throws IOException;
}
