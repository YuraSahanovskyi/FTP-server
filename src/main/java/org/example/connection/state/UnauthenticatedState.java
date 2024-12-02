package org.example.connection.state;

import org.example.authentication.AuthenticatedUser;
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
        AuthenticatedUser authenticatedUser = authenticator.authenticate(line, context.getIn(), context.getOut());
        if (authenticatedUser != null) {
            context.setUser(authenticatedUser);
            context.restoreHistory();
            context.setState(new WaitingCommandState(context));
        }
    }
}
