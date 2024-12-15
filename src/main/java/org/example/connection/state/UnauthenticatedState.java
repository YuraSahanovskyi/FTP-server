package org.example.connection.state;

import org.example.authentication.User;
import org.example.authentication.Authenticator;
import java.io.IOException;

class UnauthenticatedState implements ConnectionState {
    private final ConnectionContext context;
    private final Authenticator authenticator;
    public UnauthenticatedState(ConnectionContext context) {
        this.context = context;
        this.authenticator = new Authenticator();
    }
    @Override
    public void handleCommand(String line) throws IOException {
        User user = authenticator.authenticate(line, context.getIn(), context.getOut());
        if (user != null) {
            context.setUser(user);
            context.restoreHistory();
            context.setState(new WaitingCommandState(context));
        }
    }
}
