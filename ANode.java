import absyn.*;

/*
  Created by: Boris Skurikhin, Brayden Klemens
  File Name: ANode.java
*/

public class ANode {

    int scope;
    String name;
    Absyn def;
    int offset;
    boolean address;

    public ANode (String name, Absyn def, int scope) {
        this.name = name;
        this.def = def;
        this.scope = scope;
        this.address = false;
        offset = -1;
    }

    public ANode(String name, Absyn def, int scope, int offset) {
        this.name = name;
        this.def = def;
        this.scope = scope;
        this.offset = offset;
        this.address = false;
    }

    public ANode(String name, Absyn def, int scope, int offset, boolean addy) {
        this.name = name;
        this.def = def;
        this.scope = scope;
        this.offset = offset;
        this.address = addy;
    }

    public String toString() {
        if (def instanceof NoValDeclaration) {
            NoValDeclaration e = (NoValDeclaration) def;
            return e.type.getType();
        } else if (def instanceof ArrayDeclaration) {
            ArrayDeclaration e = (ArrayDeclaration) def;
            return e.type.getType() + "[" + (e.size == null ? "" : e.size.value) + "]";
        } else if (def instanceof FunctionDeclaration) {
            FunctionDeclaration e = (FunctionDeclaration) def;
            if (e.parameters == null) return "() -> " + e.type.getType();
            return "(" + String.join(", ", e.parameters.print_value) + ") -> " + e.type.getType();
        }
        return "TODO!";
    }


}