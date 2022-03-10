/*
  Created by: Boris Skurikhin, Brayden Klemens
  File Name: ShowTreeVisitor.java
*/

import absyn.*;

public class ShowTreeVisitor implements AbsynVisitor {

  final static int SPACES = 4;
  public StringBuilder out;

  public ShowTreeVisitor (StringBuilder out) {
    this.out = out;
  }

  private void indent( int level ) {
    for( int i = 0; i < level * SPACES; i++ ) out.append( " " );
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
    out.append( "AssignExp:\n" );
    level++;
    exp.lhs.accept( this, level );
    exp.rhs.accept( this, level );
  }

  public void visit( IfExp exp, int level ) {
    indent( level );
    int save = level;
    out.append( "IfExp:\n" );
    level++;
    exp.test.accept( this, level );
    exp.thenpart.accept( this, level );
    if (exp.elsepart != null ) {
      indent(save);
      out.append("ElseExp:\n");
      exp.elsepart.accept( this, level );
    }
  }

  public void visit( IntExp exp, int level ) {
    indent( level );
    out.append( "IntExp: " + exp.value + "\n"); 
  }

  public void visit(NoValDeclaration exp, int level) {
    indent( level );
    out.append( "NoValDeclaration: " );
    if (exp.type.type == VariableType.VOID) {
      out.append("VOID " + exp.name + "\n");
    } else if (exp.type.type == VariableType.INT) {
      out.append("INT " + exp.name + "\n");
    } else if (exp.type.type == VariableType.ERROR) {
      out.append("ERROR " + exp.name + "\n");
    }
  }

  public void visit( OpExp exp, int level ) {
    indent( level );
    out.append( "OpExp:" ); 
    switch( exp.op ) {
      case OpExp.PLUS:
        out.append( " + \n" );
        break;
      case OpExp.MINUS:
        out.append( " - \n" );
        break;
      case OpExp.TIMES:
        out.append( " * \n" );
        break;
      case OpExp.DIVIDE:
        out.append( " / \n" );
        break;
      case OpExp.CHECKEQUALS:
        out.append( " == \n" );
        break;
      case OpExp.CHECKNOTEQUALS:
        out.append( " != \n" );
        break;
      case OpExp.LESSTHAN:
        out.append( " < \n" );
        break;
      case OpExp.GREATERTHAN:
        out.append( " > \n" );
        break;
      case OpExp.LESSTHANEQ:
        out.append( " <= \n" );
        break;
      case OpExp.GREATERTHANEQ:
        out.append( " >= \n" );
        break;
      case OpExp.ERROR:
        out.append( "ERROR \n" );
        break;
      default:
        out.append( "Unrecognized operator at line " + exp.row + " and column " + exp.col + "\n");
    }
    level++;
    if (exp.left != null && exp.right != null) {
      exp.left.accept( this, level );
      exp.right.accept( this, level );
    }
  }

  public void visit( ReadExp exp, int level ) {
    indent( level );
    out.append( "ReadExp:\n" );
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
    out.append("ArrayDeclaration: " + arrType + " " + exp.name + " [");

    if (exp.size == null || exp.size.value == null) {
      out.append("]\n");
    } else {
      out.append(exp.size.value + "]\n");
    }
  }

  public void visit(FunctionDeclaration exp, int level) {
    indent(level);
    String funcType = (exp.type.type == VariableType.VOID ? "VOID" : "INT");
    out.append("FunctionDeclaration: " + funcType + " " + exp.name + "\n");
    level++;
    
    if (exp.parameters != null)
      exp.parameters.accept(this, level);

    if (exp.body != null)
      exp.body.accept(this, level);

  }

  public void visit(CompoundStatement statement, int level) {
    indent(level);
    out.append("CompoundStatement:\n");

    if (statement.declarations != null || statement.expressions != null)
      level++;
      
    if (statement.declarations != null)
      statement.declarations.accept(this, level);

    if (statement.expressions != null)
      statement.expressions.accept(this, level);

  }

  public void visit( RepeatExp exp, int level ) {
    indent( level );
    out.append( "WhileExp:\n" );
    level++;
    exp.test.accept( this, level ); 
    exp.statement.accept( this, level );
  }

  public void visit ( VariableType exp, int level) {
    indent(level);
    out.append("VariableType: " + (exp.type == VariableType.VOID ? "VOID" : "INT") + "\n");
  }

  public void visit( VariableExp exp, int level ) {
    indent( level );
    out.append( "VariableExp: " + exp.name + "\n");

    if (exp.expressions != null)
      exp.expressions.accept(this, level);
  }

  public void visit(ArrVariableExp exp, int level) {
    indent(level);
    out.append("ArrVariableExp: " + exp.name + "\n");
    level++;
    if (exp.expressions != null)
      exp.expressions.accept(this, level);
  }

  public void visit (CallExpression exp, int level) {
    indent(level);
    out.append("CallExpression: " + exp.name + "\n");
    level++;
    if (exp.args != null)
      exp.args.accept(this, level);
  }

  public void visit(ReturnExp exp, int level) {
    indent(level);
    out.append("ReturnExp:\n");
    level++;
    if (exp.expression != null)
      exp.expression.accept(this, level);
  }

  public void visit( WriteExp exp, int level ) {
    indent( level );
    out.append( "WriteExp:\n" );
    exp.output.accept( this, ++level );
  }

}
