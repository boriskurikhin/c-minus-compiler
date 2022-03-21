package absyn;

import java.util.*;

abstract public class Absyn {
  public int row, col;
  public List<String> print_value = new ArrayList<String>();

  abstract public void accept( AbsynVisitor visitor, int level );
}
