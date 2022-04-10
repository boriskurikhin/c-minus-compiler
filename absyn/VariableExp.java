package absyn;

public class VariableExp extends Exp {
  public String name;
  public Exp expressions;
  public boolean passAsAddress;

  public VariableExp( int row, int col, String name, Exp expressions) {
    this.row = row;
    this.col = col;
    this.name = name;
    this.expressions = expressions;
    passAsAddress = false;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }

  public int accept (AbsynVisitorM3 visitor, int offset, boolean isAddress) {
    return visitor.visit(this, offset, isAddress);
  }
}
