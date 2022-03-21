import absyn.*;

public class ANode {

    int scope;
    String name;
    Absyn def;

    public ANode (String name, Absyn def, int scope) {
        this.name = name;
        this.def = def;
        this.scope = scope;
    }

    public String toString() {
        if (def instanceof NoValDeclaration) {
            NoValDeclaration e = (NoValDeclaration) def;
            return e.type.getType();
        } else if (def instanceof ArrayDeclaration) {
            ArrayDeclaration e = (ArrayDeclaration) def;
            return e.type.getType() + "[" + e.size.value + "]";
        } else if (def instanceof FunctionDeclaration) {
            FunctionDeclaration e = (FunctionDeclaration) def;
            if (e.parameters == null) return "() -> " + e.type.getType();
            return e.parameters.print_value + " -> " + e.type.getType();
        }
        return "TODO!";
    }


}