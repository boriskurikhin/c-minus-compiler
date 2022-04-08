package absyn;

public class IntExp extends Exp {
  public String value;

  public IntExp( int row, int col, String value ) {
    this.row = row;
    this.col = col;
    this.value = value;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }

  public int accept (AbsynVisitorM3 visitor, int offset, boolean isAddress) {
    return visitor.visit(this, offset, isAddress);
  }
}
