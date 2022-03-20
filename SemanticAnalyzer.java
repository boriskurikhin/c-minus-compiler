import absyn.*;
import java.util.*;

public class SemanticAnalyzer implements AbsynVisitor {

    public StringBuilder out;
    
    final static int SPACES = 4;

    int global_scope; /* global scope for the analyzer */
    Map<String, ArrayList<ANode>> symbol_table = new HashMap<>();

    public SemanticAnalyzer (StringBuilder out) {
        this.out = out;
        global_scope = 0;

        /*
            TODO: add input() and output() to the symbol_table. Not sure what the name is .. probably function something
        */

        out.append("The Semantic analyzer tree is:\n");
        out.append("Entering the global scope:\n");
    }

    private void insert_node(ANode node) {
        symbol_table
            .computeIfAbsent(node.name, k -> new ArrayList<ANode>())
            .add(node);
    }

    private ANode find_node(String name){
        if(symbol_table.containsKey(name)){
            List<ANode> list = symbol_table.get(name);
            int list_size = list.size();
            return list.get(list_size - 1);
        }

        return null;
    }

    private void delete_nodes_at_scope(int del_scope) {
        Set<String> keys_to_remove = new HashSet<String>();
        symbol_table.forEach((name, refs) -> {
            ANode last = refs.get(refs.size() - 1);
            if (last.scope == del_scope) {
                refs.remove(refs.size() - 1); //TODO: refs.remove(last); ???
                if (refs.size() == 0)
                keys_to_remove.add(name);
            }
        });
        symbol_table
            .entrySet()
            .removeIf(entry -> keys_to_remove.contains(entry.getKey()));
    }

    private void indent(int s) {
        for (int i = 0; i < s * SPACES; i++)
            out.append(" ");
    }
    
    public void print_scope(final int p_scope) {
        symbol_table.forEach((name, refs) -> {
            ANode last = refs.get(refs.size() - 1);
            if (last.scope == global_scope) {
                indent(p_scope + 1);
                out.append(last.name + ": " + last.print_value + "\n");
            }
        });
    }

    /* END OF HELPER FUNCTIONS */

    public void visit (DeclarationList exp, int level){
        while (exp != null) {
            if (exp.head != null)
                exp.head.accept(this, level);
            exp = exp.tail;
        }
    }

    /* int x; */
    public void visit (NoValDeclaration exp, int level) {
        exp.type.accept(this, level);
        ANode check = find_node(exp.name);
        if (check != null && check.scope == global_scope) {
            /* var declared in the same scope */
            System.err.println("Error: re-defined variable " + exp.name + " at the same scope, on line: " + 
                (exp.row + 1) + ", column: " + exp.col);
            return;
        }
        insert_node(new ANode(exp.name, exp.type.getType(), global_scope));
    }
    
    public void visit( ExpList exp, int level ){}

    public void visit( AssignExp exp, int level ){}
  
    public void visit( IfExp exp, int level ){}
  
    public void visit( IntExp exp, int level ){}
  
    public void visit( OpExp exp, int level ){}
  
    public void visit( ReadExp exp, int level ){}
  
    public void visit( RepeatExp exp, int level ){}
  
    public void visit( VariableExp exp, int level ){}
  
    public void visit( ArrVariableExp exp, int level ){}
  
    public void visit( WriteExp exp, int level ){}
  
    public void visit (ArrayDeclaration exp, int level){}
  
    public void visit (VariableType exp, int level){}
    
    public void visit (CompoundStatement exp, int level){}
  
    public void visit (FunctionDeclaration exp, int level){}
  
    public void visit (VarDeclarationList exp, int level){}
  
    public void visit(CallExpression exp, int level){}
  
    public void visit(ReturnExp exp, int level){}
    
}