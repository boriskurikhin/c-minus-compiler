package absyn;

public class VarDeclarationList extends Absyn {

    public VarDeclaration head;
    public VarDeclarationList tail;

    public VarDeclarationList(VarDeclaration head, VarDeclarationList tail) {
        this.head = head;
        this.tail = tail;
    }

    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }

    public int accept (AbsynVisitorM3 visitor, int offset, boolean isAddress) {
        return visitor.visit(this, offset, isAddress);
    }
    
}