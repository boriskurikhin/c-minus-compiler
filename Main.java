/*
  Created by: Boris Skurikhin, Brayden Klemens
  File Name: Main.java
*/
   
import java.io.*;
import java.util.*;
import absyn.*;
   
class Main{
  public final static boolean SHOW_TREE = true;
  static public void main(String argv[]) throws Exception {
    /* Start the parser */
    BufferedWriter writer = null;
    
    // Captures stuff that's written to sys error
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream err_stream = new PrintStream(bytes, true, "UTF-8");
    PrintStream og_err_stream = System.err;

    System.setErr(err_stream);

    boolean display_ast = false;
    boolean display_sym = false;
    boolean display_code = false;

    int filename_index = 0, i = 0;
    File file;

    for (String arg : argv) {
      if (arg.charAt(0) != '-')
        filename_index = i;
      else if (arg.equals("-a"))
        display_ast = true;
      else if (arg.equals("-s"))
        display_sym = true;
      else if (arg.equals("-c"))
        display_code = true;
      i++;
    }

    if (display_code) file = new File(argv[filename_index].replace(".cm", ".tm"));
    else if (display_sym) file = new File(argv[filename_index].replace(".cm", ".sym"));
    else if (display_ast) file = new File(argv[filename_index].replace(".cm", ".abs"));
    else return; /* no flags specified, exiting */

    try {
      writer = new BufferedWriter(new FileWriter(file));
      parser p = new parser(new Lexer(new FileReader(argv[0])));

      //M1
      Absyn result = (Absyn)(p.parse().value);
      ShowTreeVisitor visitor = new ShowTreeVisitor(new StringBuilder());
      result.accept(visitor, 0);

      if (display_ast && result != null) {
        writer.write(visitor.out.toString());
        writer.close();
      }

      if (bytes.toString("UTF-8").length() > 0 || display_ast) {
        System.setErr(og_err_stream);
        System.err.print(bytes.toString("UTF-8"));
        return;
      }

      //M2
      SemanticAnalyzer analyzer = new SemanticAnalyzer(new StringBuilder());
      result.accept(analyzer, 0);

      if (display_sym && result != null) {
        analyzer.print_scope(0);
        analyzer.out.append("Leaving the global scope\n");
        writer.write(analyzer.out.toString());
        writer.close();
      }

      if (bytes.toString("UTF-8").length() > 0 || display_sym) {
        System.setErr(og_err_stream);
        System.err.print(bytes.toString("UTF-8"));
        return;
      }
      
      //M3
      CodeGenerator generator = new CodeGenerator(new StringBuilder());
      generator.visit(result, generator, argv[filename_index].replace(".cm", ".tm"));

      writer.write(generator.out.toString());
      writer.close();

      if (bytes.toString("UTF-8").length() > 0) {
        System.setErr(og_err_stream);
        System.err.print(bytes.toString("UTF-8"));
      }

    } catch (Exception e) {
      /* do cleanup here -- possibly rethrow e */
      e.printStackTrace();
    }
  }
}
