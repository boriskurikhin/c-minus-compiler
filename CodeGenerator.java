import absyn.*;
import java.util.*;

public class CodeGenerator implements AbsynVisitor {

    

    public StringBuilder out;
    public int entry;
    public int globalScope;
    public int globalOffset;
    public int localOffset;

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

    public CodeGenerator(StringBuilder out) {
        entry = 0;
        emitLoc = 0;
        highEmitLoc = 0;
        globalOffset = 0;
        globalScope = 0;
        localOffset = 0;
        isParameter = false;

        symbol_table = new HashMap<String, ArrayList<ANode>>();
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

    private ANode find_function (String name) {
        ANode last = find_node(name);
        if (last == null)
            return null;
        if (last.def instanceof FunctionDeclaration)
            return last;
        return null;
    }

    /* Printing functions */

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
        if (loc > highEmitLoc) {
            emitComment("BUG in emitBackup");
        }
        emitLoc = loc;
    }

    public void emitRestore() {
        emitLoc = highEmitLoc;
    }

    /* Traversal Functions */

    public void visit (Absyn tree, CodeGenerator visitor, String fileName) {
        emitComment("C-Minus Compilation to TM Code");
		emitComment("File: " + fileName);
        emitComment("Standard prelude: ");

        emitRM("LD", gp, 0, ac, "load gp with maxaddress");
        emitRM("LDA", fp, 0, gp, "copy gp to fp");
        emitRM("ST", ac, 0, ac, "clear location 0");

        // IO stuff
        // input function

        emitComment("Jump around i/o routines here");
        emitComment("code for input()");

        int savedLoc = emitSkip(1);
        int funcLoc = emitSkip(0);

        insert_node(new ANode("input", new FunctionDeclaration(
            0, 0, new VariableType(0, 0, VariableType.INT), "input", null, null
        ), 0, funcLoc));

        emitRM("ST", 0, -1, fp, "store return of");
        emitRO("IN", 0, 0, 0, "input");
        emitRM("LD", pc, -1, fp, "return to caller");

        // output function

        emitComment("code for output routine");
        funcLoc = emitSkip(0);

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
            funcLoc
        ));

        emitRM("ST", 0, -1, fp, "store return");
        emitRM("LD", 0, -2, fp, "load output value");
        emitRO("OUT", 0, 0, 0, "output");
        emitRM("LD", pc, -1, fp, "return to caller");
        
        int savedLoc2 = emitSkip(0);
        emitBackup(savedLoc);
        emitRM_Abs("LDA", pc, savedLoc2, "jump around i/o code");
        emitRestore();

        emitComment("End of standard prelude.");

        //enter code
        tree.accept(visitor, 0);

        //Begin standard finale
        ANode mainFunc = find_function("main");
        emitRM("ST", fp, globalOffset, fp, "push ofp");
        emitRM("LDA", fp, globalOffset, fp, "push frame");
        emitRM("LDA", 0, 1, pc, "Load ac with ret ptr");

        emitRM_Abs("LDA", pc, mainFunc.offset, "jump to main loc");
        emitRM("LD", fp, 0, fp, "pop frame");
        emitRO("HALT", 0, 0, 0, "");
    }

    public void visit( IntExp exp, int level) {
        emitComment("-> constant");
        emitRM("LDC", ac, Integer.parseInt(exp.value), 0, "load const");
        emitComment("<- constant");
        //TODO: add the weird load left thing?
    }

    public void visit (DeclarationList exp, int level) {
        while (exp != null) {
            if (exp.head != null)
                exp.head.accept(this, level);
            exp = exp.tail;
        }
    }

    public void visit (NoValDeclaration exp, int level){
        emitComment("-> vardecl");
        emitComment("allocating" + (level == 0 ? " global " : " ") + "var: " + exp.name);

        ANode var = new ANode(exp.name, exp, globalScope, globalOffset);
        insert_node(var);
        globalOffset--;
    }

    public void visit (ArrayDeclaration exp, int level){
        emitComment("-> arrdecl");
        emitComment("allocating" + (level == 0 ? " global " : " ") + "arr var: " + exp.name);

        int size = Integer.parseInt(exp.size.value);
        int off = globalOffset - (size - 1);
        ANode var = new ANode(exp.name, exp, globalScope, globalOffset);
        insert_node(var);
        globalOffset -= size;
    }


    public void visit (FunctionDeclaration exp, int level) {
        localOffset = -2;

        emitComment("-> fundecl");
        emitComment(" processing function: " + exp.name);
        emitComment(" jump around functions body here");

        int savedLoc = emitSkip(1);
        globalScope++;

        ANode var = new ANode(exp.name, exp, globalScope, emitLoc);
        insert_node(var);

        emitRM("ST", 0, -1, fp, "store return");

        isParameter = true;
        if (exp.parameters != null) exp.parameters.accept(this, localOffset);
        isParameter = false;

        if (exp.body != null) exp.body.accept(this, localOffset);
        emitRM("LD", pc, -1, fp, "return caller");
        int savedLoc2 = emitSkip(0);
        emitBackup(savedLoc);
        emitRM_Abs("LDA", pc, savedLoc2, "jump around function body");
        emitRestore();
        emitComment("<- function decl");

        globalScope--;
    }

    public void visit (CompoundStatement exp, int level) {
        int savedLocal = localOffset;
        emitComment("-> compound statement");
        exp.declarations.accept(this, level);
        exp.expressions.accept(this, localOffset);
        emitComment("<- compound statement");
        localOffset = savedLocal;
    }

    public void visit (VarDeclarationList exp, int level) {
        VarDeclarationList head = exp;
        localOffset = level;
        int savedLocal = localOffset;

        while (exp != null) {
            if (exp.head != null) {
                exp.head.accept(this, localOffset);
            }
            exp = exp.tail;
        }

        localOffset = savedLocal;
    }

    public void visit( ExpList exp, int level ){
        while (exp != null) {
            if (exp.head != null)
                exp.head.accept(this, level);
            exp = exp.tail;
        }        
    }

    public void visit( AssignExp exp, int level) {
        emitComment("-> op");

        exp.lhs.accept(this, level);
        emitRM("ST", ac, level--, fp, "op: push left");
        exp.rhs.accept(this, level);
        emitRM("LD", 1, ++level, fp, "op: load left");
        emitRM("ST", ac, 0, 1, "assign: store value");

        emitComment("<- op");
    }

    public void visit(CallExpression exp, int level){
        int i = -2;

        ANode func = find_function(exp.name);
        emitComment("-> call");
        emitComment("call of function: " + exp.name);

        while(exp.args != null) {
            if (exp.args.head != null) {
                exp.args.head.accept(this, level);
                emitRM("ST", ac, level + i, fp, "op: push left");
                i--;
            }
            exp.args = exp.args.tail;
        }

        emitRM("ST", fp, level, fp, "push ofp");
        emitRM("LDA", fp, level, fp, "Push frame");
        emitRM("LDA", 0, 1, pc, "Load ac with ret ptr");
        emitRM_Abs("LDA", pc, func.offset, "jump to fun loc");
        emitRM("LD", fp, 0, fp, "Pop frame");
        emitComment("<- call");
    }

  
    public void visit( IfExp exp, int level ){}
  
  
    public void visit( OpExp exp, int level ){}
  
    public void visit( RepeatExp exp, int level ){}
  
    public void visit( VariableExp exp, int level ){}
  
    public void visit( ArrVariableExp exp, int level ){}
  
    public void visit (VariableType exp, int level){}
          
  
    public void visit(ReturnExp exp, int level){}

}


