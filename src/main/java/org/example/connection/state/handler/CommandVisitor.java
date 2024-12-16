package org.example.connection.state.handler;

public interface CommandVisitor {
    void visit(CwdCommandHandler handler);
    void visit(DeleCommandHandler handler);
    void visit(ListCommandHandler handler);
    void visit(MkdCommandHandler handler);
    void visit(PwdCommandHandler handler);
    void visit(RetrCommandHandler handler);
    void visit(StorCommandHandler handler);
}
