package absyn;

public class FunctionDeclaration extends Declaration {
    public VariableType type;
    public String name;
    public VarDeclarationList parameters;
    public CompoundStatement body;

    public FunctionDeclaration(
        int row,
        int col,
        VariableType type,
        String name,
        VarDeclarationList parameters,
        CompoundStatement body) {
            this.row = row;
            this.col = col;
            this.type = type;
            this.name = name;
            this.parameters = parameters;
            this.body = body;
        }

    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level );
    }
}