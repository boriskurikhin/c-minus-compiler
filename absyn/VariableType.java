package absyn;

public class VariableType extends Absyn {
    public int type;
    public final static int VOID = 0;
    public final static int INT  = 1;
    public final static int ERROR = 2;


    public VariableType(int row, int col, int type) {
        this.row = row;
        this.col = col;
        this.type = type;
    }

    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }

    public String getType() {
        switch (type) {
            case 0: return "VOID";
            case 1: return "INT";
            default: return "ERROR";
        }
    }
}