package absyn;

public class CallExpression extends Exp {
    public String name;
    public ExpList args;

    public CallExpression(int row, int col, String name, ExpList args) {
        this.row = row;
        this.col = col;
        this.name = name;
        this.args = args;
    }

    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }

    public int accept (AbsynVisitorM3 visitor, int offset, boolean isAddress) {
        return visitor.visit(this, offset, isAddress);
    }
}