package org.example.states;

import org.example.AuthenticatedUser;
import org.example.Authenticator;
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
