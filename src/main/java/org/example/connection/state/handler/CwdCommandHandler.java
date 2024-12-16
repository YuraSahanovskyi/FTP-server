package org.example.connection.state.handler;

import org.example.authentication.Permission;
import org.example.connection.state.ConnectionContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CwdCommandHandler extends AbstractCommandHandler {

    private Path newPath;

    public CwdCommandHandler(ConnectionContext context) {
        super(context);
    }

    @Override
    protected boolean preconditions(String line) {
        String path = line.substring(4).trim();
        return canChangeDirectory(path);
    }

    private boolean canChangeDirectory(String path) {
        newPath = Paths.get(context.getCurrentDirectory(), path).normalize();

        if (!Files.exists(newPath) || !Files.isDirectory(newPath)) {
            return false;
        }

        return context.getUser().getPermissions().stream()
                .filter(p -> newPath.startsWith(p.getPath()))
                .anyMatch(Permission::isRead);
    }

    @Override
    protected void execute(String line) {
        context.setCurrentDirectory(newPath.toString());
        context.saveHistory();
        context.getOut().println("250 Directory successfully changed.");
    }

    @Override
    protected void handleError(String line) {
        context.getOut().println("550 Failed to change directory.");
    }
}
