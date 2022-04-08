package absyn;

public interface AbsynVisitorM3 {

  public int visit( ExpList exp, int offset, boolean isAddress );

  public int visit( AssignExp exp, int offset, boolean isAddress );

  public int visit( IfExp exp, int offset, boolean isAddress );

  public int visit( IntExp exp, int offset, boolean isAddress );

  public int visit( OpExp exp, int offset, boolean isAddress );

  public int visit( RepeatExp exp, int offset, boolean isAddress );

  public int visit( VariableExp exp, int offset, boolean isAddress );

  public int visit( ArrVariableExp exp, int offset, boolean isAddress );

  public int visit (ArrayDeclaration exp, int offset, boolean isAddress);

  public int visit (DeclarationList exp, int offset, boolean isAddress);

  public int visit (VariableType exp, int offset, boolean isAddress);

  public int visit (NoValDeclaration exp, int offset, boolean isAddress);

  public int visit (CompoundStatement exp, int offset, boolean isAddress);

  public int visit (FunctionDeclaration exp, int offset, boolean isAddress);

  public int visit (VarDeclarationList exp, int offset, boolean isAddress);

  public int visit(CallExpression exp, int offset, boolean isAddress);

  public int visit(ReturnExp exp, int offset, boolean isAddress);

}
