/*
  Created by: Boris Skurikhin, Brayden Klemens
  File Name: Main.java
*/
   
import java.io.*;
import java.util.*;
import absyn.*;
   
class Main{
  public final static boolean SHOW_TREE = true;
  static public void main(String argv[])  {
    /* Start the parser */
    BufferedWriter writer = null;
    boolean display_ast = false;
    int filename_index = 0, i = 0;
    File file;

    for (String arg : argv) {
      if (arg.charAt(0) != '-')
        filename_index = i;
      if (arg.equals("-a"))
        display_ast = true;
      i++;
    }

    /* as per the spec, if the -a flag is not included we exit */
    if (!display_ast) return ;

    file = new File(argv[filename_index].replace(".cm", ".ast"));

    try {
      writer = new BufferedWriter(new FileWriter(file));

      parser p = new parser(new Lexer(new FileReader(argv[0])));
      Absyn result = (Absyn)(p.parse().value);      
      if (SHOW_TREE && result != null) {
        StringBuilder out = new StringBuilder();
        ShowTreeVisitor visitor = new ShowTreeVisitor(out);
        result.accept(visitor, 0);
        writer.write(visitor.out.toString());
        writer.close();
      }
    } catch (Exception e) {
      /* do cleanup here -- possibly rethrow e */
      e.printStackTrace();
    }
  }
}
