package absyn;

public class ArrayDeclaration extends VarDeclaration {
    
    public VariableType type;
    public String name;
    public IntExp size;
    public boolean passAsAddress;

    public ArrayDeclaration(int row, int col, VariableType type, String name, IntExp size) {
        this.row = row;
        this.col = col;
        this.type = type;
        this.name = name;
        this.size = size;
        passAsAddress = false;
    }

    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }

    public int accept (AbsynVisitorM3 visitor, int offset, boolean isAddress) {
        return visitor.visit(this, offset, isAddress);
    }
}