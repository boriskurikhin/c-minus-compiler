package absyn;

public class ExpList extends Absyn {
  public Exp head;
  public ExpList tail;

  public ExpList( Exp head, ExpList tail ) {
    this.head = head;
    this.tail = tail;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }

  public int accept (AbsynVisitorM3 visitor, int offset, boolean isAddress) {
    return visitor.visit(this, offset, isAddress);
  }
}
