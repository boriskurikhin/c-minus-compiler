package absyn;

public class CompoundStatement extends Exp {
    
    public VarDeclarationList declarations;
    public ExpList expressions;

    public CompoundStatement(int row, int col, VarDeclarationList decs, ExpList exp) {
        this.row = row;
        this.col = col;
        declarations = decs;
        expressions = exp;
    }

    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }

    public int accept (AbsynVisitorM3 visitor, int offset, boolean isAddress) {
        return visitor.visit(this, offset, isAddress);
    }

}