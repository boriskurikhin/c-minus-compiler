package absyn;

public class AssignExp extends Exp {
  public Exp lhs;
  public Exp rhs;


  public AssignExp( int row, int col, Exp lhs, Exp rhs ) {
    this.row = row;
    this.col = col;
    this.lhs = lhs;
    this.rhs = rhs;
  }
  
  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}
