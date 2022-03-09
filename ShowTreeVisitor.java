import absyn.*;

public class ShowTreeVisitor implements AbsynVisitor {

  final static int SPACES = 4;

  private void indent( int level ) {
    for( int i = 0; i < level * SPACES; i++ ) System.out.print( " " );
  }

  public void visit( ExpList expList, int level ) {
    while( expList != null ) {
      if (expList.head != null) expList.head.accept( this, level );
      expList = expList.tail;
    } 
  }

  public void visit(VarDeclarationList exp, int level) {
    while( exp != null ) {
      if (exp.head != null) exp.head.accept( this, level );
      exp = exp.tail;
    } 
  }

  public void visit( AssignExp exp, int level ) {
    indent( level );
    System.out.println( "AssignExp:" );
    level++;
    exp.lhs.accept( this, level );
    exp.rhs.accept( this, level );
  }

  public void visit( IfExp exp, int level ) {
    indent( level );
    int save = level;
    System.out.println( "IfExp:" );
    level++;
    exp.test.accept( this, level );
    exp.thenpart.accept( this, level );
    if (exp.elsepart != null ) {
      indent(save);
      System.out.println("ElseExp: ");
      exp.elsepart.accept( this, level );
    }
  }

  public void visit( IntExp exp, int level ) {
    indent( level );
    System.out.println( "IntExp: " + exp.value ); 
  }

  public void visit(NoValDeclaration exp, int level) {
    indent( level );
    System.out.print( "NoValDeclaration: " );
    if (exp.type.type == VariableType.VOID) {
      System.out.println("VOID " + exp.name);
    } else if (exp.type.type == VariableType.INT) {
      System.out.println("INT " + exp.name);
    }
  }

  public void visit( OpExp exp, int level ) {
    indent( level );
    System.out.print( "OpExp:" ); 
    switch( exp.op ) {
      case OpExp.PLUS:
        System.out.println( " + " );
        break;
      case OpExp.MINUS:
        System.out.println( " - " );
        break;
      case OpExp.TIMES:
        System.out.println( " * " );
        break;
      case OpExp.DIVIDE:
        System.out.println( " / " );
        break;
      case OpExp.CHECKEQUALS:
        System.out.println( " = " );
        break;
      case OpExp.LESSTHAN:
        System.out.println( " < " );
        break;
      case OpExp.GREATERTHAN:
        System.out.println( " > " );
        break;
      case OpExp.LESSTHANEQ:
        System.out.println( " <= " );
        break;
      case OpExp.GREATERTHANEQ:
        System.out.println( " >= " );
        break;
      default:
        System.out.println( "Unrecognized operator at line " + exp.row + " and column " + exp.col);
    }
    level++;
    exp.left.accept( this, level );
    exp.right.accept( this, level );
  }

  public void visit( ReadExp exp, int level ) {
    indent( level );
    System.out.println( "ReadExp:" );
    exp.input.accept( this, ++level );
  }

  public void visit(DeclarationList exp, int level) {
    while( exp != null ) {
      exp.head.accept( this, level );
      exp = exp.tail;
    }
  }

  public void visit(ArrayDeclaration exp, int level) {
    indent(level);
    String arrType = (exp.type.type == VariableType.VOID ? "VOID" : "INT");
    System.out.print("ArrayDeclaration: " + arrType + " " + exp.name + " [");

    if (exp.size == null || exp.size.value == null) {
      System.out.println("]");
    } else {
      System.out.println(exp.size.value + "]");
    }
  }

  public void visit(FunctionDeclaration exp, int level) {
    indent(level);
    String funcType = (exp.type.type == VariableType.VOID ? "VOID" : "INT");
    System.out.println("FunctionDeclaration: " + funcType + " " + exp.name + " ");
    level++;
    
    if (exp.parameters != null)
      exp.parameters.accept(this, level);

    if (exp.body != null)
      exp.body.accept(this, level);

  }

  public void visit(CompoundStatement statement, int level) {
    indent(level);
    System.out.println("CompoundStatement: ");

    if (statement.declarations != null || statement.expressions != null)
      level++;
      
    if (statement.declarations != null)
      statement.declarations.accept(this, level);

    if (statement.expressions != null)
      statement.expressions.accept(this, level);

  }

  public void visit( RepeatExp exp, int level ) {
    indent( level );
    System.out.println( "RepeatExp:" );
    level++;
    exp.test.accept( this, level ); 
    exp.statement.accept( this, level );
  }

  public void visit ( VariableType exp, int level) {
    indent(level);
    System.out.println("VariableType: " + (exp.type == VariableType.VOID ? "VOID" : "INT"));
  }

  public void visit( VariableExp exp, int level ) {
    indent( level );
    System.out.println( "VariableExp: " + exp.name );

    if (exp.expressions != null)
      exp.expressions.accept(this, level);
  }

  public void visit(ArrVariableExp exp, int level) {
    indent(level);
    System.out.println("ArrVariableExp: " + exp.name);
    level++;
    if (exp.expressions != null)
      exp.expressions.accept(this, level);
  }

  public void visit (CallExpression exp, int level) {
    indent(level);
    System.out.println("CallExpression: " + exp.name);
    level++;
    if (exp.args != null)
      exp.args.accept(this, level);
  }

  public void visit(ReturnExp exp, int level) {
    indent(level);
    System.out.println("ReturnExp: ");
    level++;
    if (exp.expression != null)
      exp.expression.accept(this, level);
  }

  public void visit( WriteExp exp, int level ) {
    indent( level );
    System.out.println( "WriteExp:" );
    exp.output.accept( this, ++level );
  }

}
