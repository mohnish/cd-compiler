/*
 *
 * @author: Mohnish Thallavajhula
 * @ID: 800606747
 * @Webmail: mohnish.thallavajhula631@wku.edu
 */
import java.io.*;

public class SyntaxAnalyzer {

   public static void main(String[] args) throws FileNotFoundException, IOException {
      System.out.println("**Lexical Analyzer**\n");
      try {
         //creating new scanner object
         Scanner myScanner = new Scanner(args[0]);
         Token next;
         while ((next = myScanner.getNextToken()) != null) {
            switch (next.type) {
               case 1://Keyword
                  System.out.println("Keyword " + next.getLexeme() + " " + next.getLineNumber());
                  break;
                  
               case 2://Identifier
                  System.out.println("Identifier " + next.getLexeme() + " " + next.getLineNumber());
                  break;

               case 3://Float
                  System.out.println("Float " + next.getLexeme() + " " + next.getLineNumber());
                  break;

               case 4://Integer
                  System.out.println("Integer " + next.getLexeme() + " " + next.getLineNumber());
                  break;

               case 5://Addition Operator
                  System.out.println("Addition Op " + next.getLexeme() + " " + next.getLineNumber());
                  break;

               case 6://Subtraction Operator
                  System.out.println("Subtraction Op " + next.getLexeme() + " " + next.getLineNumber());
                  break;

               case 7://Multiplication Operator
                  System.out.println("Multiplication Op " + next.getLexeme() + " " + next.getLineNumber());
                  break;

               case 8://Division Operator
                  System.out.println("Division Op " + next.getLexeme() + " " + next.getLineNumber());
                  break;

               case 9://OpenBrace
                  System.out.println("OpenBrace " + next.getLexeme() + " " + next.getLineNumber());
                  break;

               case 10://Closed Brace
                  System.out.println("CloseBrace " + next.getLexeme() + " " + next.getLineNumber());
                  break;

               case 11://Closed Paranthesis
                  System.out.println("CloseParanthesis " + next.getLexeme() + " " + next.getLineNumber());
                  break;
               case 12://Open Paranthesis
                  System.out.println("OpenParanthesis " + next.getLexeme() + " " + next.getLineNumber());
                  break;
               case 13://Others
                  System.out.println("Others " + next.getLexeme() + " " + next.getLineNumber());
                  break;
               case 14://Relation Op
                  System.out.println("Relation Op " + next.getLexeme() + " " + next.getLineNumber());
                  break;
               case 15://Error
                  System.out.println("Error " + next.getLexeme() + " " + next.getLineNumber());
                  break;
               case 16://StringConstant
                  System.out.println("StringConstant " + next.getLexeme() + " " + next.getLineNumber());
                  break;
               default:
                  System.out.println(" ");
                  break;

            }
         }
      }
         catch (ArrayIndexOutOfBoundsException e) {
         System.out.println("Supply a file name.");
      }
         catch (FileNotFoundException e) {
         System.out.println("No such file found. Make sure the file is available. ");
      }



   }
}
