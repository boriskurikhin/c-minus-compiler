package absyn;

public class ReturnExp extends Exp {

    public Exp expression;

    public ReturnExp(int row, int col, Exp expression) {
        this.row = row;
        this.col = col;
        this.expression = expression;
    }

    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }

    public int accept (AbsynVisitorM3 visitor, int offset, boolean isAddress) {
        return visitor.visit(this, offset, isAddress);
    }
}