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
}