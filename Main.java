/*
  Created by: Boris Skurikhin, Brayden Klemens
  File Name: Main.java
*/
   
import java.io.*;
import absyn.*;
   
class Main {
  public final static boolean SHOW_TREE = true;
  static public void main(String argv[]) {
    /* Start the parser */
    boolean display_ast = false;
    if (argv.length >= 2) {
      if (argv[1].equals("-a")) {
        display_ast = true;
      }
    }

    try {
      parser p = new parser(new Lexer(new FileReader(argv[0])));
      Absyn result = (Absyn)(p.parse().value);      
      if (SHOW_TREE && result != null && display_ast) {
         System.out.println("The abstract syntax tree is:");
         ShowTreeVisitor visitor = new ShowTreeVisitor();
         result.accept(visitor, 0); 
      }
    } catch (Exception e) {
      /* do cleanup here -- possibly rethrow e */
      e.printStackTrace();
    }
  }
}
