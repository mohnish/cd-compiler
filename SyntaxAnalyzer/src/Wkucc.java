/*
 *
 * @author: Mohnish Thallavajhula
 * @id: 800606747
 * @Webmail: mohnish.thallavajhula631@wku.edu
 */

public class Wkucc {

   public static void main(String[] args) throws Exception {
      if (args.length < 1) {
         //No filename error
         System.out.println("Please give a file name.");
         System.exit(0);
      } else if (args.length > 1) {
         //More than one argument entered
         System.out.println("Too many arguments.");
         System.exit(0);
      } else {
         //If everything is fine,  create SyntaxAnalyzer object
         SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(args[0]);
         /*
          ******************
         START PARSING HERE
          ******************
          */
         syntaxAnalyzer.parse();
      }
   }
}
