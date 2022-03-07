package absyn;

public class ReadExp extends Exp {
  public VariableExp input;

  public ReadExp( int row, int col, VariableExp input ) {
    this.row = row;
    this.col = col;
    this.input = input;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}
