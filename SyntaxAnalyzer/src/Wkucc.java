/*
 *
 * @author: Mohnish Thallavajhula
 * @id: 800606747
 * @Webmail: mohnish.thallavajhula631@wku.edu
 */

public class Wkucc {

   public static void main(String[] args) {
      System.out.println("----Start Analysis----\n");

      Scanner myScanner = null;
      //Check: Number of arguments
      if (args.length < 1) {
         System.out.println("Please give a file name.");
         System.exit(0);
      } else if (args.length > 1) {
         System.out.println("Too many arguments.");
         System.exit(0);
      } else {
         myScanner = new Scanner(args[0]);
      }

      Token next;
      while ((next = myScanner.getNextToken()) != null) {
         switch (next.type) {
            case KEYWORD://Keyword
               System.out.println("Keyword " + next.getLexeme() + " "
                       + next.getLineNumber());
               break;

            case IDENTIFIER://Identifier
               System.out.println("Identifier " + next.getLexeme() + " "
                       + next.getLineNumber());
               break;

            case FLOAT://Float
               System.out.println("Float " + next.getLexeme() + " "
                       + next.getLineNumber());
               break;

            case INTEGER://Integer
               System.out.println("Integer " + next.getLexeme() + " "
                       + next.getLineNumber());
               break;

            case ADDOP://Addition Operator
               System.out.println("Addition Op " + next.getLexeme() + " " + next.getLineNumber());
               break;

            case SUBOP://Subtraction Operator
               System.out.println("Subtraction Op " + next.getLexeme() + " " + next.getLineNumber());
               break;

            case MULOP://Multiplication Operator
               System.out.println("Multiplication Op " + next.getLexeme() + " " + next.getLineNumber());
               break;

            case DIVOP://Division Operator
               System.out.println("Division Op " + next.getLexeme() + " " + next.getLineNumber());
               break;

            case LEFT_CURLY://OpenBrace
               System.out.println("OpenBrace " + next.getLexeme() + " " + next.getLineNumber());
               break;

            case RIGHT_CURLY://Closed Brace
               System.out.println("CloseBrace " + next.getLexeme() + " " + next.getLineNumber());
               break;

            case RIGHT_PARA://Closed Paranthesis
               System.out.println("CloseParanthesis " + next.getLexeme() + " " + next.getLineNumber());
               break;
            case LEFT_PARA://Open Paranthesis
               System.out.println("OpenParanthesis " + next.getLexeme() + " " + next.getLineNumber());
               break;
            case OTHERS://Others
               System.out.println("Others " + next.getLexeme() + " " + next.getLineNumber());
               break;
            case RELOP://Relation Op
               System.out.println("Relation Op " + next.getLexeme() + " " + next.getLineNumber());
               break;
            case ERROR://Error
               System.out.println("Error " + next.getLexeme() + " " + next.getLineNumber());
               break;
            case STRING_CONSTANT://StringConstant
               System.out.println("STRING_CONSTANT " + next.getLexeme() + " " + next.getLineNumber());
               break;
            case ASSIGNMENT://AssignmentOp
               System.out.println("AssignmentOp " + next.getLexeme() + " " + next.getLineNumber());
               break;
            default:
               System.out.println("");
               break;
         }
      }
      System.out.println("\n----Analysis Complete----");
   }
}
