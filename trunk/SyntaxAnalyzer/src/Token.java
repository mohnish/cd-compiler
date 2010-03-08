/*
 *
 * @author: Mohnish Thallavajhula
 * @ID: 800606747
 * @Webmail: mohnish.thallavajhula631@wku.edu
 */
/* this class is used to create the token object and put the created objects
 * in an arraylist
 * the arraylist objects hold the type, lexeme, and the linenumber
*/
public class Token {

   TokenTypes type;
   String lexeme;
   int lineNumber;

   public Token(TokenTypes type, String lexeme, int lineNumber) {
      this.type = type;
      this.lexeme = lexeme;
      this.lineNumber = lineNumber;
   }

   public TokenTypes getType() {
      return type;
   }

   public String getLexeme() {
      return lexeme;
   }

   public int getLineNumber() {
      return lineNumber;
   }
}
