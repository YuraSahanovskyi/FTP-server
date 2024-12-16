package org.example.connection.state.handler;

import org.example.authentication.Permission;
import org.example.connection.state.ConnectionContext;

import java.io.File;
import java.io.IOException;

public class DeleCommandHandler extends AbstractCommandHandler {
    public DeleCommandHandler(ConnectionContext context) {
        super(context);
    }
    private File fileToDelete;
    @Override
    protected boolean preconditions(String line) {
        fileToDelete = new File(context.getCurrentDirectory()+ "/" + line.substring(5));
        return fileToDelete.exists() && context.getUser()
                .getPermissions().stream()
                .filter(p -> context.getCurrentDirectory().startsWith(p.getPath()))
                .anyMatch(Permission::isWrite);
//        return true;
    }

    @Override
    protected void execute(String line) throws IOException {
        if (fileToDelete.isDirectory() && fileToDelete.length() > 0) {
            context.getOut().println("550 Directory is not empty");
            throw new IOException(fileToDelete.toString());
        }
        if(fileToDelete.delete()) {
            context.getOut().println("250 File/directory deleted successfully.");
        }
    }

    @Override
    protected void handleError(String line) {
        context.getOut().println("550 Failed to delete file/directory.");
    }
}
