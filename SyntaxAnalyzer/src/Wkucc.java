/*
 *
 * @author: Mohnish Thallavajhula
 * @id: 800606747
 * @Webmail: mohnish.thallavajhula631@wku.edu
 */

public class Wkucc {

   public static void main(String[] args) throws Exception {
//      Scanner myScanner = null;
      //Check: Number of arguments
      if (args.length < 1) {
         System.out.println("Please give a file name.");
         System.exit(0);
      } else if (args.length > 1) {
         System.out.println("Too many arguments.");
         System.exit(0);
      } else {
         SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(args[0]);
         syntaxAnalyzer.parse();
      }
//      Token next;
//      while ((next = myScanner.getNextToken()) != null) {
//      }      
   }
}
