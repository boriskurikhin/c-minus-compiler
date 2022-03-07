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
    
}