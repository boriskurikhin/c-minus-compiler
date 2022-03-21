import absyn.*;
import java.util.*;
import java.util.regex.*;

public class SemanticAnalyzer implements AbsynVisitor {

    public StringBuilder out;
    
    final static int SPACES = 4;

    int global_scope; /* global scope for the analyzer */
    Map<String, ArrayList<ANode>> symbol_table = new HashMap<>();

    FunctionDeclaration function_tracker;
    Set<Integer> return_tracker;
    

    public SemanticAnalyzer (StringBuilder out) {
        this.out = out;
        global_scope = 0;

        function_tracker = null;
        return_tracker = new HashSet<Integer>();

        /*
            TODO: add input() and output() to the symbol_table. Not sure what the name is .. probably function something
            TODO: handle dead code (stuff thats after a return statement for that scope)
            TODO: checking array index out of bounds (not that important)
            TODO: accessing elements in array that don't have value stored (might assume everything is 0 or random junk)
            TODO: add function name when entering function
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
                out.append(last.name + ": " + last.toString() + "\n");
            }
        });
    }

    private boolean check_non_array_exists(String name) {
        return symbol_table
            .getOrDefault(name, new ArrayList<ANode>())
            .stream()
            .anyMatch(ref -> (ref.def instanceof NoValDeclaration));
    }

    /* TODO: we could potentially check array index out of bounds here */
    private boolean check_array_exists(String name) {
        return symbol_table
            .getOrDefault(name, new ArrayList<ANode>())
            .stream()
            .anyMatch(ref -> (ref.def instanceof ArrayDeclaration));
    }

    private ANode find_function (String name) {
        return symbol_table
            .getOrDefault(name, new ArrayList<ANode>())
            .stream()
            .filter(ref -> (ref.def instanceof FunctionDeclaration))
            .findFirst()
            .orElse(null);
    }

    /* END OF HELPER FUNCTIONS */

    /* all variables/functions declared at the top level */
    public void visit (DeclarationList exp, int level){
        while (exp != null) {
            if (exp.head != null)
                exp.head.accept(this, level);
            exp = exp.tail;
        }
    }

    public void visit (VariableType exp, int level) {}

    public void visit( IntExp exp, int level ){}

    /* int x; */
    public void visit (NoValDeclaration exp, int level) {
        exp.type.accept(this, level);
        ANode check = find_node(exp.name);

        if (check != null && check.scope == global_scope) {
            /* var declared in the same scope */
            System.err.println("Error: re-defined variable \"" + exp.name + "\" at the same scope, on line: " + 
                (exp.row + 1) + ", column: " + exp.col);
            return;
        }

        insert_node(new ANode(exp.name, exp, global_scope));
    }

    /* int x[10] */
    public void visit (ArrayDeclaration exp, int level){
        exp.type.accept(this, level);

        if (exp.size != null)
            exp.size.accept(this, level);

        ANode check = find_node(exp.name);

        if (check != null && check.scope == global_scope) {
            /* var declared in the same scope */
            System.err.println("Error: re-defined array variable \"" + exp.name + "\" at the same scope, on line: " + 
                (exp.row + 1) + ", column: " + exp.col);
            return;
        }

        insert_node(new ANode(exp.name, exp, global_scope));
    }


    /* function parameters */
    public void visit (VarDeclarationList exp, int level) {
        VarDeclarationList head = exp;
        List<String> print_value = new ArrayList<String>();

        while (exp != null) {
            if (exp.head != null) {
                exp.head.accept(this, level);
                if (exp.head instanceof NoValDeclaration)
                    print_value.add("INT");
                else if (exp.head instanceof ArrayDeclaration)
                    print_value.add("INT[]");
            }
            exp = exp.tail;
        }

        head.print_value = print_value;
    }

    /* int func() {} */
    public void visit (FunctionDeclaration exp, int level) {
        exp.type.accept(this, level);
        
        global_scope++;

        indent(level + 1);
        out.append("Entering the function scope for function " + exp.name + ":\n");

        if (exp.parameters != null)
            exp.parameters.accept(this, level + 1);

        ANode check = find_node(exp.name);

        if (check != null && check.scope == 0) {
            System.err.println("Error: re-defined function " + exp.name + " at the top scope, on line: " + (exp.row + 1) + ", column: " + exp.col);
        } else {
            function_tracker = exp;
            return_tracker = new HashSet<Integer>();
            insert_node(new ANode(exp.name, exp, global_scope - 1));
        }
            
        if (exp.body != null)
            exp.body.accept(this, level + 1);

        if (exp.type.getType().equals("INT") && !return_tracker.contains(level + 1))
            System.err.println("Error: INT function may not return a value, on line: " + (exp.row + 1) + ", column: " + exp.col);

        print_scope(level + 1);
        indent(level + 1);
        out.append("Leaving the function scope\n");
        delete_nodes_at_scope(global_scope);
        
        function_tracker = null;
        global_scope--;
    }

    /* Basically the insides of a function */
    public void visit (CompoundStatement exp, int level) {
        exp.declarations.accept(this, level);
        exp.expressions.accept(this, level);
    }

    /* if statements, return exp, etc, etc */
    public void visit( ExpList exp, int level ) {
        while (exp != null) {
            if (exp.head != null)
                exp.head.accept(this, level);
            exp = exp.tail;
        }
    }

    /* return x + 3; */
    public void visit(ReturnExp exp, int level) {
        if (exp.expression != null && function_tracker.type.getType().equals("VOID"))
            System.err.println("Error: VOID function attempted to return expression, at line " + (exp.row + 1) + ", column: " + (exp.col + 1));
        else if (exp.expression == null && function_tracker.type.getType().equals("INT"))
            System.err.println("Error: INT function attempted to return empty expression, at line " + (exp.row + 1) + ", column: " + (exp.col + 1));
        else if (exp.expression != null) {
            return_tracker.add(level);
            exp.expression.accept(this, level);
        }
    }

    public void visit( AssignExp exp, int level ) {
        exp.lhs.accept(this, level);
        exp.rhs.accept(this, level);
    }

    public void visit( VariableExp exp, int level ) {
        if (!check_non_array_exists(exp.name))
            System.err.println("Error: variable undeclared, at line " + (exp.row + 1) + ", column: " + (exp.col + 1));
    }

    public void visit( ArrVariableExp exp, int level ){
        exp.expressions.accept(this, level);
        if(!check_array_exists(exp.name))
            System.err.println("Error: array undeclared, at line " + (exp.row + 1) + ", column: " + (exp.col + 1));
    }

    public void visit( OpExp exp, int level) {
        exp.left.accept(this, level);
        exp.right.accept(this, level);
    }
  
    public void visit( IfExp exp, int level ){
        exp.test.accept(this, level);

        global_scope++;
        
        indent(level + 1);
        out.append("Entering a new if block:\n");

        if (exp.thenpart != null) 
            exp.thenpart.accept(this, level + 1);

        print_scope(level + 1);
        delete_nodes_at_scope(global_scope);
        
        indent(level + 1);
        out.append("Leaving the if block\n");

        if (exp.elsepart != null) {
            
            indent(level + 1);
            out.append("Entering a new else block:\n");

            exp.elsepart.accept(this, level + 1);
            print_scope(level + 1);

            delete_nodes_at_scope(global_scope);

            indent(level + 1);
            out.append("Leaving the else block\n");
        }

        global_scope--;
    }
      
    public void visit( RepeatExp exp, int level ) {
        exp.test.accept(this, level);

        global_scope++;

        indent(level + 1);
        out.append("Entering a new while block:\n");

        if (exp.statement != null)
            exp.statement.accept(this, level + 1);

        print_scope(level + 1);
        delete_nodes_at_scope(global_scope);

        indent(level + 1);
        out.append("Leaving the while block\n");

        global_scope--;
    }
    
    public void visit(CallExpression exp, int level) {

        ANode function_called = find_function(exp.name);

        if (function_called == null) {
            System.err.println("Error: function undeclared, at line " + (exp.row + 1) + ", column: " + (exp.col + 1));
            return;
        }

        ExpList args = exp.args;
        FunctionDeclaration function_dec = (FunctionDeclaration) function_called.def;

        /* (INT, INT, INT) or (INT) or () */
        List<String> function_sig = function_dec.parameters == null ? new ArrayList<String>() : function_dec.parameters.print_value;
        int args_expected = function_sig.size();
        int arg_index = 0;

        /* (INT[], INT, INT, INT) */
        /* call(k[], 3, k, m) */

        while (args != null) {
            if (args.head != null) {
                /* checks to make sure the args are good */
                /* must be of the same type */
                args.head.accept(this, level);
                if (function_sig.get(arg_index).equals("INT")) {
                    System.err.println("Error: function argument INT expected, at line " + (exp.row + 1) + ", column: " + (args.head.col + 1));
                } else if (function_sig.get(arg_index).equals("INT[]") && (args.head instanceof VariableExp)) {
                    System.err.println("Error: function argument INT[] expected, at line " + (exp.row + 1) + ", column: " + (args.head.col + 1));
                }
                arg_index++;
            }
            args = args.tail;
        }

        if (args_expected != arg_index)
            System.err.println("Error: function argument count mismatch, at line " + (exp.row + 1) + ", column: " + (exp.col + 1));

    }
   
}