package absyn;

public class RepeatExp extends Exp {
  public Exp test;
  public Exp statement;

  public RepeatExp( int row, int col, Exp test, Exp statement ) {
    this.row = row;
    this.col = col;
    this.test = test;
    this.statement = statement;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}
