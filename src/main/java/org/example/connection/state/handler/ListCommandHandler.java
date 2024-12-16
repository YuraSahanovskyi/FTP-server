package org.example.connection.state.handler;

import org.example.authentication.Permission;
import org.example.connection.state.ConnectionContext;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class ListCommandHandler extends AbstractCommandHandler {

    private File[] files;
    public ListCommandHandler(ConnectionContext context) {
        super(context);
    }

    @Override
    protected boolean preconditions(String line) {
        if (context.getDataSocket() == null) {
            context.getOut().println("425 Use PASV or PORT first.");
            return false;
        }
        File dir = new File(context.getCurrentDirectory());
        files = dir.listFiles();
        return files != null && context.getUser().getPermissions().stream()
                .filter(p -> context.getCurrentDirectory().startsWith(p.getPath()))
                .anyMatch(Permission::isRead);
    }

    @Override
    protected void execute(String line) throws IOException {
            context.getOut().println("150 Here comes the directory listing.");
            BufferedWriter dataOut = new BufferedWriter(new OutputStreamWriter(context.getDataSocket().getOutputStream()));
            for (File file : files) {
                dataOut.write(file.getName() + "\r\n");
            }
            dataOut.flush();
            dataOut.close();
            context.getDataSocket().close();
            context.getOut().println("226 Directory send OK.");
    }
    @Override
    protected void handleError(String line) {
        context.getOut().println("550 Failed to list directory.");
    }

    @Override
    public void accept(CommandVisitor visitor) {
        visitor.visit(this);
    }
}
