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
    boolean display_sym = false;

    int filename_index = 0, i = 0;
    File file;

    for (String arg : argv) {
      if (arg.charAt(0) != '-')
        filename_index = i;
      else if (arg.equals("-a"))
        display_ast = true;
      else if (arg.equals("-s"))
        display_sym = true;
      i++;
    }

    if (display_sym) file = new File(argv[filename_index].replace(".cm", ".sym"));
    else if (display_ast) file = new File(argv[filename_index].replace(".cm", ".abs"));
    else return; /* no flags specified, exiting */

    try {
      writer = new BufferedWriter(new FileWriter(file));
      parser p = new parser(new Lexer(new FileReader(argv[0])));
      Absyn result = (Absyn)(p.parse().value);

      if (display_sym && result != null) {
        StringBuilder out = new StringBuilder();
        SemanticAnalyzer analyzer = new SemanticAnalyzer(out);
        result.accept(analyzer, 0);
        analyzer.print_scope(0);
        analyzer.out.append("Leaving the global scope\n");
        writer.write(analyzer.out.toString());
        writer.close();
      } else if (display_ast && result != null) {
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
