import absyn.*;
import java.util.*;

public class CodeGenerator implements AbsynVisitor {

    

    public StringBuilder out;
    public int entry;
    public int globalLevel;
    public int globalOffset;

    public int lineNumber;
    public int nextLineNumber;

    public final int gp = 6;
    public final int ac = 0;
    public final int ac1 = 1;
    public final int fp = 5;
    public final int pc = 7;

    public final int ofpFO = 0;
    public final int retOF = -1;
    public final int initOF = -2;

    Map<String, ArrayList<ANode>> symbol_table;

    public CodeGenerator(StringBuilder out) {
        entry = 0;
        lineNumber = 0;
        nextLineNumber = 0;
        gloabalOffset = 0;

        symbol_table = new HashMap<String, ArrayList<ANode>>();
        this.out = out;
    }

    /* SymTable helper functions */

    private void insert_node(ANode node) {
        symbol_table
            .computeIfAbsent(node.name, k -> new ArrayList<ANode>())
            .add(node);
    }

    /* Printing functions */

    public void emitComment(String comment) {
        out.append("* " + comment + "\n");
    }

    public void emitRO (String op, int r, int s, int t, String comment) {
        String out_string = String.format("%3d: %5s %d,%d,%d \t%s\n", lineNumber, op, r, s, t, comment);
        out.append(out_string);

        lineNumber++;
        nextLineNumber = Math.max(nextLineNumber, lineNumber);
    }

    public void emitRM(String op, int r, int d, int s, String comment) {
        String out_string = String.format("%3d: %5s %d,%d(%d) \t%s\n", lineNumber, op, r, d, s, comment);
        out.append(out_string);

        lineNumber++;
        nextLineNumber = Math.max(nextLineNumber, lineNumber);
    }

    public void emitRM_Abs(String op, int r, int a, String comment) {
        String out_string = String.format("%3d: %5s %d,%d(%d) \t%s\n", lineNumber, op, r, (a - (lineNumber + 1)), pc, comment);
        out.append(out_string);

        lineNumber++;
        nextLineNumber = Math.max(nextLineNumber, lineNumber);
    }

    public int emitSkip(int amount){
        int i = lineNumber;
        lineNumber += amount;

        nextLineNumber = Math.max(nextLineNumber, lineNumber);
        return i;
    }

    public void emitBackup(int loc) {
        if (loc > nextLineNumber) {
            emitComment("BUG in emitBackup");
        }
        lineNumber = loc;
    }

    public void emitRestore() {
        lineNumber = nextLineNumber;
    }

    /* Traversal Functions */

    public void visit (Absyn tree, CodeGenerator vistor, String fileName) {
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

        emitRM("ST", 0, retOF, fp, "store return of");
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

        emitRM("ST", 0, retOF, fp, "store return");
        emitRM("LD", 0, initOF, fp, "load output value");
        emitRO("OUT", 0, 0, 0, "output");
        emitRM("LD", pc, -1, fp, "return to caller");
        
        int savedLoc2 = emitSkip(0);
        emitBackup(savedLoc);
        emitRM_Abs("LDA", pc, savedLoc2, "jump around i/o code");
        emitRestore();

        emitComment("End of standard prelude.");

        globalOffset = initOF;
        tree.accept(visitor, level);
    }

    public void visit( ExpList exp, int level ){
        
    }

    public void visit( AssignExp exp, int level ){}
  
    public void visit( IfExp exp, int level ){}
  
    public void visit( IntExp exp, int level ){}
  
    public void visit( OpExp exp, int level ){}
  
    public void visit( RepeatExp exp, int level ){}
  
    public void visit( VariableExp exp, int level ){}
  
    public void visit( ArrVariableExp exp, int level ){}
  
    public void visit (ArrayDeclaration exp, int level){}
  
    public void visit (DeclarationList exp, int level){}
  
    public void visit (VariableType exp, int level){}
  
    public void visit (NoValDeclaration exp, int level){}
  
    public void visit (CompoundStatement exp, int level){}
  
    public void visit (FunctionDeclaration exp, int level){}
  
    public void visit (VarDeclarationList exp, int level){}
  
    public void visit(CallExpression exp, int level){}
  
    public void visit(ReturnExp exp, int level){}

}


