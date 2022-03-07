package absyn;

public class ArrayDeclaration extends Declaration {
    
    public VariableType type;
    public String name;
    public IntExp size;

    public ArrayDeclaration(int row, int col, VariableType type, String name, IntExp size) {
        this.row = row;
        this.col = col;
        this.type = type;
        this.name = name;
        this.size = size;
    }

    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}