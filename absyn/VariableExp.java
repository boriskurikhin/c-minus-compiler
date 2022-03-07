package absyn;

public class VariableExp extends Exp {
  public String name;

  public VariableExp( int row, int col, String name) {
    this.row = row;
    this.col = col;
    this.name = name;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}
