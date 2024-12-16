package org.example.connection.state.handler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggingVisitor implements CommandVisitor {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void visit(CwdCommandHandler handler) {
        System.out.println(LocalDateTime.now().format(formatter) + " User " + handler.getUsername() + " executed command CWD");
    }

    @Override
    public void visit(DeleCommandHandler handler) {
        System.out.println(LocalDateTime.now().format(formatter) + " User " + handler.getUsername() + " executed command DELE");
    }

    @Override
    public void visit(ListCommandHandler handler) {
        System.out.println(LocalDateTime.now().format(formatter) + " User " + handler.getUsername() + " executed command LIST");
    }

    @Override
    public void visit(MkdCommandHandler handler) {
        System.out.println(LocalDateTime.now().format(formatter) + " User " + handler.getUsername() + " executed command MKD");
    }

    @Override
    public void visit(PwdCommandHandler handler) {
        System.out.println(LocalDateTime.now().format(formatter) + " User " + handler.getUsername() + " executed command PWD");
    }

    @Override
    public void visit(RetrCommandHandler handler) {
        System.out.println(LocalDateTime.now().format(formatter) + " User " + handler.getUsername() + " executed command RETR");
    }

    @Override
    public void visit(StorCommandHandler handler) {
        System.out.println(LocalDateTime.now().format(formatter) + " User " + handler.getUsername() + " executed command STOR");
    }
}
