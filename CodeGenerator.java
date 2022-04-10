import absyn.*;
import java.util.*;

/*

    Written by Boris Skurikhin and Brayden Klemens
    M3
*/

public class CodeGenerator implements AbsynVisitorM3 {

    public StringBuilder out;
    public int entry;
    public int globalScope;
    public int globalOffset;

    public boolean isParameter;

    public int emitLoc;
    public int highEmitLoc;

    public final int gp = 6;
    public final int ac = 0;
    public final int ac1 = 1;
    public final int fp = 5;
    public final int pc = 7;

    public static final int IN_ADDR = 4;
    public static final int OUT_ADDR = 7;

    Map<String, ArrayList<ANode>> symbol_table;
    Queue<String> globalInstructions;

    public CodeGenerator(StringBuilder out) {
        entry = Integer.MAX_VALUE;
        emitLoc = 0;
        highEmitLoc = 0;
        globalOffset = 0;
        globalScope = 0;

        isParameter = false;

        symbol_table = new HashMap<String, ArrayList<ANode>>();
        globalInstructions = new LinkedList<String>();
        this.out = out;
    }

    /* SymTable helper functions */

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

    private boolean check_array_exists(String name) {
        ANode last = find_node(name);
        if (last == null)
            return false;
        return (last.def instanceof ArrayDeclaration);
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

    private ANode find_function (String name) {
        ANode last = find_node(name);
        if (last == null)
            return null;
        if (last.def instanceof FunctionDeclaration)
            return last;
        return null;
    }

    /* Printing functions */

    public void emitGlobalInstructions() {
        while (!globalInstructions.isEmpty()) {
            out.append(String.format("%3d: %s\n", emitLoc, globalInstructions.poll()));
            emitLoc += 1;
            if (highEmitLoc < emitLoc) highEmitLoc = emitLoc;
        }
    }

    public void emitComment(String comment) {
        out.append("* " + comment + "\n");
    }

    public void emitRO (String op, int r, int s, int t, String comment) {
        String out_string = String.format("%3d: %5s %d,%d,%d \t%s\n", emitLoc, op, r, s, t, comment);
        out.append(out_string);

        emitLoc++;
        highEmitLoc = Math.max(highEmitLoc, emitLoc);
    }

    public void emitRM(String op, int r, int d, int s, String comment) {
        String out_string = String.format("%3d: %5s %d,%d(%d) \t%s\n", emitLoc, op, r, d, s, comment);
        out.append(out_string);

        emitLoc++;
        highEmitLoc = Math.max(highEmitLoc, emitLoc);
    }

    public void emitRM_Abs(String op, int r, int a, String comment) {
        String out_string = String.format("%3d: %5s %d,%d(%d) \t%s\n", emitLoc, op, r, (a - (emitLoc + 1)), pc, comment);
        out.append(out_string);

        emitLoc++;
        highEmitLoc = Math.max(highEmitLoc, emitLoc);
    }

    public int emitSkip(int amount){
        int i = emitLoc;
        emitLoc += amount;

        highEmitLoc = Math.max(highEmitLoc, emitLoc);
        return i;
    }

    public void emitBackup(int loc) {
        if (loc > highEmitLoc)
            emitComment("BUG in emitBackup");
        emitLoc = loc;
    }

    public void emitRestore() {
        emitLoc = highEmitLoc;
    }

    /* Traversal Functions */

    public void visit (Absyn tree, CodeGenerator visitor, String fileName) {
        emitComment("Prelude");
        emitRM("LD", gp, 0, ac, "load gp with maxaddr");
        emitRM("LDA", fp, 0, gp, "copy gp to fp");
        emitRM("ST", ac, 0, ac, "clear content at loc 0");
        
        // input
        int savedLoc = emitSkip(1);
        insert_node(new ANode("input", new FunctionDeclaration(
            0, 0, new VariableType(0, 0, VariableType.INT), "input", null, null
        ), 0, emitSkip(0)));

        emitComment("input()");
        emitRM("ST", ac, -1, fp, "store return");
        emitRO("IN", 0, 0, 0, "input");
        emitRM("LD", pc, -1, fp, "return to caller");

        //output
        VarDeclarationList output_params = new VarDeclarationList(
            new NoValDeclaration(
                0,
                0,
                "output_value",
                new VariableType(0, 0, VariableType.INT)
            ),
            null
        );
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
            0,
            emitSkip(0)
        ));
        emitComment("output()");
        emitRM("ST", ac, -1, fp, "store return");
        emitRM("LD", ac, -2, fp, "load output value");
        emitRO("OUT", 0, 0, 0, "output");
        emitRM("LD", pc, -1, fp, "return to caller");
       
        tree.accept(visitor, 0, false);

        int savedLoc2 = emitSkip(0);
        emitBackup(savedLoc);
        emitRM_Abs("LDA", pc, savedLoc2, "");
        emitRestore();

        if (entry == Integer.MAX_VALUE) {
            System.err.println("Runtime error: main function not found");
            emitRO("HALT", 0, 0, 0, "");
            return;
        }

        emitComment("finale");
        emitRM("ST", fp, globalOffset, fp, "push old frame pointer");
        emitRM("LDA", fp, globalOffset, fp, "push frame");
        emitRM("LDA", ac, 1, pc, "load ac with return pointer");
        emitRM_Abs("LDA", pc, entry, "jump to main loc");
        emitRM("LD", fp, 0, fp, "pop frame");
        emitRO("HALT", 0, 0, 0, "");
    }

    public int visit( IntExp exp, int offset, boolean isAddress) {
        emitComment("-> constant");
        emitRM("LDC", ac, Integer.parseInt(exp.value), 0, "load const");
        emitComment("<- constant");
        return offset;
    }

    public int visit (DeclarationList exp, int offset, boolean isAddress) {
        while (exp != null) {
            if (exp.head != null) {
                if (exp.head instanceof FunctionDeclaration) exp.head.accept(this, offset, false);
                else globalOffset = exp.head.accept(this, globalOffset, false);
            }
            exp = exp.tail;
        }
        return offset;
    }

    public int visit (NoValDeclaration exp, int offset, boolean isAddress) {
        ANode var = new ANode(exp.name, exp, globalScope, offset);
        insert_node(var);
        return offset - 1;
    }

    public int visit (ArrayDeclaration exp, int offset, boolean isAddress) {

        if (exp.size != null) {
            int size = Integer.parseInt(exp.size.value);
            int memory_offset = offset - size + 1; //10 -> 10, index 9 is where element 0 is
            ANode var = new ANode(exp.name, exp, globalScope, memory_offset);
            insert_node(var);

            if (globalScope > 0) {
                offset -= size;
                emitRM("LDC", ac, size, ac, "load array size into register");
                emitRM("ST", ac, offset, fp, "store array size in memory");
            } else {
                offset -= size;
                globalInstructions.offer(String.format(
                    "%5s %d, %d(%d)\t%s",
                    "LDC",
                    ac1,
                    size,
                    ac,
                    "load array size into ac1"
                ));
                globalInstructions.offer(String.format(
                    "%5s %d, %d(%d)\t%s",
                    "ST",
                    ac1,
                    offset,
                    gp,
                    "store array size in memory"
                ));
            }
        } else {
            exp.passAsAddress = true;
            insert_node(new ANode(exp.name, exp, globalScope, offset));
        }

        return offset - 1;
    }

    public int visit (FunctionDeclaration exp, int offset, boolean isAddress) {
        offset = -2;

        ANode func = new ANode(exp.name, exp, globalScope, emitSkip(0));
        insert_node(func);

        globalScope++;

        if (exp.name.equals("main")) {
            entry = emitLoc;
            emitGlobalInstructions();
        }

        emitComment("function decl: " + exp.name);
        emitRM("ST", ac, -1, fp, "store return");

        if (exp.parameters != null)
            offset = exp.parameters.accept(this, offset, false);

        offset = exp.body.accept(this, offset, false);
        emitRM("LD", pc, -1, fp, "return to caller");

        delete_nodes_at_scope(globalScope);
        globalScope--;

        return offset;
    }

    public int visit (CompoundStatement exp, int offset, boolean isAddress) {
        offset = exp.declarations.accept(this, offset, false);
        offset = exp.expressions.accept(this, offset, false);
        return offset;
    }

    public int visit (VarDeclarationList exp, int offset, boolean isAddress) {
        while (exp != null) {
            if (exp.head != null) {
                offset = exp.head.accept(this, offset, isAddress);
            }
            exp = exp.tail;
        }
        return offset;
    }

    public int visit( VariableExp exp, int offset, boolean isAddress) {
        ANode var = find_node(exp.name);
        if (exp.expressions != null)
            exp.expressions.accept(this, offset, false);

        if (check_array_exists(exp.name)) {
            boolean isPointer = ((ArrayDeclaration)(var.def)).passAsAddress;
            if (isPointer) emitRM("LD", ac, var.offset, var.scope == 0 ? gp : fp, "loading address pointer of " + exp.name + " into ac");
            else emitRM("LDA", ac, var.offset, var.scope == 0 ? gp : fp, "loading address of " + exp.name + " into ac");
        } else {
            if (isAddress) emitRM("LDA", ac, var.offset, var.scope == 0 ? gp : fp, "loading address of " + exp.name + " into ac");
            else emitRM("LD", ac, var.offset, var.scope == 0 ? gp : fp, "loading value of " + exp.name + " into ac");
        }

        return offset;
    }

    public int visit(ArrVariableExp exp, int offset, boolean isAddress) {
        ANode var = find_node(exp.name);
        
        exp.expressions.accept(this, offset, false);
        //TODO: handle writing to arrays passed by address into function

        boolean isPointer = ((ArrayDeclaration) (find_node(exp.name).def)).passAsAddress;

        if (isPointer) emitRM("LD", ac1, var.offset, var.scope == 0 ? gp : fp, "load arr address into ac");
        else emitRM("LDA", ac1, var.offset, var.scope == 0 ? gp : fp, "load array base address");

        emitRO("ADD", ac, ac, ac1, "get final array base address");
        if (!isAddress)
            emitRM("LD", ac, 0, ac, "store value into index");

        return offset;
    }

    public int visit( ExpList exp, int offset, boolean isAddress) {
        while (exp != null) {
            if (exp.head != null) {
                offset = exp.head.accept(this, offset, isAddress);
            }
            exp = exp.tail;
        }
        return offset;
    }

    public int visit( AssignExp exp, int offset, boolean isAddress) {
        exp.lhs.accept(this, offset - 1, true);
        emitRM("ST", ac, offset - 1, fp, "store lhs address in memory");
        exp.rhs.accept(this, offset - 2, false);
        emitRM("ST", ac, offset - 2, fp, "store rhs in memory");

        emitRM("LD", ac, offset - 1, fp, "load lhs into ac");
        emitRM("LD", ac1, offset - 2, fp, "load result of rhs into ac1");

        emitRM("ST", ac1, 0, ac, "write rhs into address given by lhs");
        emitRM("ST", ac1, offset, fp, "store result");

        return offset;
    }

    public int visit( OpExp exp, int offset, boolean isAddress) {
        exp.left.accept(this, offset - 1, false);
        emitRM("ST", ac, offset - 1, fp, "store lhs in memory");

        exp.right.accept(this, offset - 2, false);
        emitRM("ST", ac, offset - 2, fp, "store rhs in memory");

        emitRM("LD", ac, offset - 1, fp, "load lhs into reg0");
        emitRM("LD", ac1, offset - 2, fp, "load rhs into reg1");

        switch (exp.op) {
            case OpExp.PLUS:
                emitRO("ADD", ac, ac, ac1, "add lhs and rhs");
                break;
            case OpExp.MINUS:
                emitRO("SUB", ac, ac, ac1, "subtract rhs from lhs");
                break;
            case OpExp.TIMES:
                emitRO("MUL", ac, ac, ac1, "multiply lhs and rhs");
                break;
            case OpExp.DIVIDE:
                emitRO("DIV", ac, ac, ac1, "divide lhs with rhs");
                break;
            case OpExp.CHECKEQUALS:
                emitRO("SUB", ac, ac, ac1, "sub rhs from lhs");
                emitRM("JEQ", ac, 2, pc, "check if 0");
                emitRM("LDC", ac, 0, ac, "false");
                emitRM("LDA", pc, 1, pc, "jump around true");
                emitRM("LDC", ac, 1, ac, "true");
                break;
            case OpExp.CHECKNOTEQUALS:
                emitRO("SUB", ac, ac, ac1, "sub rhs from lhs");
                emitRM("JNE", ac, 2, pc, "check if not 0");
                emitRM("LDC", ac, 0, ac, "false");
                emitRM("LDA", pc, 1, pc, "jump around true");
                emitRM("LDC", ac, 1, ac, "true");
                break;
            case OpExp.LESSTHAN:
                emitRO("SUB", ac, ac, ac1, "sub rhs from lhs");
                emitRM("JLT", ac, 2, pc, "check lhs < rhs");
                emitRM("LDC", ac, 0, ac, "false");
                emitRM("LDA", pc, 1, pc, "jump around true");
                emitRM("LDC", ac, 1, ac, "true");
                break;
            case OpExp.GREATERTHAN:
                emitRO("SUB", ac, ac, ac1, "sub rhs from lhs");
                emitRM("JGT", ac, 2, pc, "check lhs > rhs");
                emitRM("LDC", ac, 0, ac, "false");
                emitRM("LDA", pc, 1, pc, "jump around true");
                emitRM("LDC", ac, 1, ac, "true");
                break;
            case OpExp.LESSTHANEQ:
                emitRO("SUB", ac, ac, ac1, "sub rhs from lhs");
                emitRM("JLE", ac, 2, pc, "check lhs <= rhs");
                emitRM("LDC", ac, 0, ac, "false");
                emitRM("LDA", pc, 1, pc, "jump around true");
                emitRM("LDC", ac, 1, ac, "true");
                break;
            case OpExp.GREATERTHANEQ:
                emitRO("SUB", ac, ac, ac1, "sub rhs from lhs");
                emitRM("JGE", ac, 2, pc, "check lhs >= rhs");
                emitRM("LDC", ac, 0, ac, "false");
                emitRM("LDA", pc, 1, pc, "jump around true");
                emitRM("LDC", ac, 1, ac, "true");
                break;
        }

        emitRM("ST", ac, offset, fp, "store result");
        return offset;
    }

    public int visit(ReturnExp exp, int offset, boolean isAddress) {
        if (exp.expression != null)
            exp.expression.accept(this, offset, false);
        emitRM("LD", pc, -1, fp, "return to caller");
        return offset;
    }

    public int visit(IfExp exp, int offset, boolean isAddress) {

        globalScope++;
        exp.test.accept(this, offset, false);
        int skip = emitSkip(1);
        int end = -1;
        
        exp.thenpart.accept(this, offset, false);
        delete_nodes_at_scope(globalScope);
        
        if (exp.elsepart != null) {
            int skip2 = emitSkip(1);
            end = emitSkip(0);

            exp.elsepart.accept(this, offset, false);
            delete_nodes_at_scope(globalScope);

            int save2 = emitSkip(0);
            emitBackup(skip2);
            emitRM_Abs("LDA", pc, save2, "jump past else");
            emitRestore();
        } else end = emitSkip(0);

        emitBackup(skip);
        emitRM_Abs("JEQ", ac, end, "jump if false");
        emitRestore();
        globalScope--;

        return offset;
    }

    public int visit( RepeatExp exp, int offset, boolean isAddress){
        globalScope++;

        int save1 = emitSkip(0);
        exp.test.accept(this, offset, false);

        int save2 = emitSkip(1);
        exp.statement.accept(this, offset, false);
        emitRM_Abs("LDA", pc, save1, "jump to test");

        int save3 = emitSkip(0);
        emitBackup(save2);
        emitRM_Abs("JEQ", ac, save3, "jump if true");
        emitRestore();

        delete_nodes_at_scope(globalScope);
        globalScope--;
        return offset;
    }

    public int visit(CallExpression exp, int offset, boolean isAddress) {
        ANode func = find_function(exp.name);

        ExpList args_passed = exp.args;
        FunctionDeclaration function_dec = (FunctionDeclaration) func.def;
        int offs = offset - 2;

        while (args_passed != null) {
            if (args_passed.head != null) {

                args_passed.head.accept(this, offs, false);
                emitRM("ST", ac, offs, fp, "load function argument");

                args_passed = args_passed.tail;
                offs--;
            }
        }
        
        emitRM("ST", fp, offset, fp, "store");
        emitRM("LDA", fp, offset, fp, "load new frame");
        emitRM("LDA", ac, ac1, pc, "save return in ac");
        emitRM_Abs("LDA", pc, func.offset, "jump to entry point of " + function_dec.name);
        emitRM("LD", fp, 0, fp, "pop frame");

        return offset;
    }

    public int visit (VariableType exp, int offset, boolean isAddress){
        return offset;
    }

}
