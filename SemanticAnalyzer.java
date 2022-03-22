import absyn.*;
import java.util.*;
import java.util.regex.*;

public class SemanticAnalyzer implements AbsynVisitor {

    public StringBuilder out;
    
    final static int SPACES = 4;

    int global_scope; /* global scope for the analyzer */
    Map<String, ArrayList<ANode>> symbol_table;

    FunctionDeclaration function_tracker;
    Set<Integer> return_tracker;
    boolean is_value_required;

    public SemanticAnalyzer (StringBuilder out) {
        this.out = out;
        global_scope = 0;

        function_tracker = null;
        symbol_table = new HashMap<>();
        return_tracker = new HashSet<Integer>();
        is_value_required = false;

        /*
            TODO: handle dead code (stuff thats after a return statement for that scope)
            TODO: checking array index out of bounds (not that important)
            TODO: accessing elements in array that don't have value stored (might assume everything is 0 or random junk)
        */

        //int input();
        // inserting the input() pre-defined function
        insert_node(new ANode("input", new FunctionDeclaration(
            0, 0, new VariableType(0, 0, VariableType.INT), "input", null, null
        ), 0));

        VarDeclarationList output_params = new VarDeclarationList(
            new NoValDeclaration(
                0,
                0,
                "output_value",
                new VariableType(0, 0, VariableType.INT)
            ),
            null
        );

        output_params.print_value = new ArrayList<String>(Arrays.asList("INT"));

        //void output(int output_value);
        // inserting the output() pre-defined function
        insert_node(new ANode(
            "output", 
            new FunctionDeclaration(
                0,
                0,
                new VariableType(0, 0, VariableType.VOID),
                "output",
                output_params,
                null
            ),
            0
        ));

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
        ANode last = find_node(name);
        if (last == null)
            return false;
        return (last.def instanceof NoValDeclaration);
    }

    /* TODO: we could potentially check array index out of bounds here */
    private boolean check_array_exists(String name) {
        ANode last = find_node(name);
        if (last == null)
            return false;
        return (last.def instanceof ArrayDeclaration);
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
            is_value_required = true;
            exp.expression.accept(this, level);
            is_value_required = false;
        }
    }

    public void visit( AssignExp exp, int level ) {
        //k  = 3 * x + m * f() * 3
        exp.lhs.accept(this, level);

        is_value_required = true;
        exp.rhs.accept(this, level);
        is_value_required = false;
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
        is_value_required = true;
        exp.left.accept(this, level);
        exp.right.accept(this, level);
        is_value_required = false;
    }
  
    public void visit( IfExp exp, int level ){
        is_value_required = true;
        exp.test.accept(this, level);
        is_value_required = false;

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
        is_value_required = true;
        exp.test.accept(this, level);
        is_value_required = false;

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

        ExpList args_passed = exp.args;
        FunctionDeclaration function_dec = (FunctionDeclaration) function_called.def;

        if (is_value_required && !function_dec.type.getType().equals("INT")) {
            System.err.println("Error: calling VOID function as part of integer expression, at line: " + (exp.row) + ", column: " +
                (exp.col + 1));
            return;
        }

        /* (INT, INT, INT) or (INT) or () */
        List<String> function_sig = function_dec.parameters == null ? new ArrayList<String>() : function_dec.parameters.print_value;
        int sig_index = 0;

        while (args_passed != null) {
            if (args_passed.head != null) {
                // Argument expects an array of ints
                if (sig_index >= function_sig.size()) {
                    System.err.println("Error: function argument mismatch - too many args passed, at line: " + 
                        (exp.row + 1) + ", column: " + (args_passed.head.col));
                    return;
                }

                if (function_sig.get(sig_index).equals("INT[]")) {
                    // A variable is passed
                    if (args_passed.head instanceof VariableExp) {
                        VariableExp v = (VariableExp) args_passed.head;
                        // Check if passed variable is of type array
                        if (!check_array_exists(v.name))
                            System.err.println("Error: function argument mismatch - array expected, at line: " + 
                            (exp.row + 1) + ", column: " + (args_passed.head.col));
                    } else {
                        args_passed.head.accept(this, level);
                        System.err.println("Error: function argument mismatch - array expected, at line: " + (exp.row + 1) 
                        + ", column: " + (args_passed.head.col));
                    }
                } else {
                    //we're expecting an int, but are given a reference to array
                    if (args_passed.head instanceof VariableExp) {
                        VariableExp v = (VariableExp) args_passed.head;

                        if (check_array_exists(v.name))
                            System.err.println("Error: function argument mismatch - int expected, at line: " + 
                            (exp.row + 1) + ", column: " + (args_passed.head.col));
                    } else {
                        args_passed.head.accept(this, level);
                    }
                }
                sig_index++;
            }
            args_passed = args_passed.tail;
        }

        if (sig_index != function_sig.size()) {
            System.err.println("Error: function argument mismatch - insufficient number of args passeds, at line: " + 
                            (exp.row + 1) + ", column: " + (exp.col));
        }
    }
}