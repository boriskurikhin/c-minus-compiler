package absyn;

public class NoValDeclaration extends VarDeclaration {
  public String name;
  public VariableType type;

  /* 2 + 3 + 4 */

  /* int k ;
     k = 5
  */

  public NoValDeclaration ( int row, int col, String name, VariableType type) {
    this.row = row;
    this.col = col;
    this.name = name;
    this.type = type;
  }
  
  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}
