package absyn;

public class VariableExp extends Exp {
  public String name;
  public Exp expressions;

  public VariableExp( int row, int col, String name, Exp expressions) {
    this.row = row;
    this.col = col;
    this.name = name;
    this.expressions = expressions;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}
