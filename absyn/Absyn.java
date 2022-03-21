package absyn;

abstract public class Absyn {
  public int row, col;
  public String print_value = "";

  abstract public void accept( AbsynVisitor visitor, int level );
}
