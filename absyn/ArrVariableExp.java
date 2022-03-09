package absyn;

public class ArrVariableExp extends Exp {
  public String name;
  public Exp expressions;

  public ArrVariableExp( int row, int col, String name, Exp expressions) {
    this.row = row;
    this.col = col;
    this.name = name;
    this.expressions = expressions;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}
