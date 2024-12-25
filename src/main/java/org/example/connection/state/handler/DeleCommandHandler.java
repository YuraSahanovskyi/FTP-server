package org.example.connection.state.handler;

import org.example.authentication.Permission;
import org.example.connection.state.ConnectionContext;
import org.example.connection.state.handler.logging.CommandVisitor;

import java.io.File;
import java.io.IOException;

public class DeleCommandHandler extends AbstractCommandHandler {
    public DeleCommandHandler(ConnectionContext context) {
        super(context);
    }
    private File fileToDelete;
    @Override
    protected boolean preconditions(String line) {
        String path = line.substring(5).trim();
        fileToDelete = new File(context.getCurrentDirectory(), path).getAbsoluteFile();

        return fileToDelete.exists() && context.getUser()
                .getPermissions().stream()
                .filter(p -> fileToDelete.getParentFile().getAbsolutePath().startsWith(p.getPath()))
                .anyMatch(Permission::isWrite);
    }

    @Override
    protected void execute(String line) throws IOException {
        if (fileToDelete.isDirectory()) {
            File[] files = fileToDelete.listFiles();
            if (files != null && files.length != 0) {
                context.getOut().println("550 Directory is not empty.");
                return;
            }
        }

        if (fileToDelete.delete()) {
            context.getOut().println("250 File/directory deleted successfully.");
        } else {
            context.getOut().println("550 Failed to delete file/directory.");
        }
    }

    @Override
    protected void handleError(String line) {
        context.getOut().println("550 Failed to delete file/directory.");
    }

    @Override
    public void accept(CommandVisitor visitor) {
        visitor.visit(this);
    }
}
