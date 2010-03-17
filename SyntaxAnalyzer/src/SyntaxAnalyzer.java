/*
 *
 * @author: Mohnish Thallavajhula
 * @ID: 800606747
 * @Webmail: mohnish.thallavajhula631@wku.edu
 */

public class SyntaxAnalyzer {

   Scanner lex = null;
   Token currentToken = null;

   public SyntaxAnalyzer(String fileName) throws Exception {
      lex = new Scanner(fileName);
      currentToken = lex.getNextToken();
   }

   /**
    * parse() function is used to parse the tokens and check for errors.
    * This is done by executing the appropriate functions for the
    * corresponding token patterns
    */
   public void parse() {
      //Starting with the translation unit
      translationUnit();
   }

   public void translationUnit() {
      //call externalDeclaration() if the return type is void|int|float
      if (currentToken != null && (currentToken.getType() == TokenTypes.KW_FLOAT
              || currentToken.getType() == TokenTypes.KW_INT
              || currentToken.getType() == TokenTypes.KW_VOID)) {
         externalDeclaration();
      } else {
         //report error for not being int/float/void
         System.out.println("ERROR on linenumber: " + currentToken.getLineNumber());
      }
      //We use loops in the program to eliminate wherever there is left recursion
      while (currentToken != null && (currentToken.getType() == TokenTypes.KW_FLOAT
              || currentToken.getType() == TokenTypes.KW_INT
              || currentToken.getType() == TokenTypes.KW_VOID)) {
         externalDeclaration();
      }

   }

   public void externalDeclaration() {
      //parse the next token if Keyword INT is encountered
      if (currentToken.getType() == TokenTypes.KW_INT) {
         currentToken = lex.getNextToken();
      } else if (currentToken.getType() == TokenTypes.KW_FLOAT) {
         //parse the next token if Keyword FLOAT is encountered
         currentToken = lex.getNextToken();
      } else if (currentToken.getType() == TokenTypes.KW_VOID) {
         //parse the next token if Keyword VOID is encountered
         currentToken = lex.getNextToken();
      }
      currentToken = lex.getNextToken();
      if (currentToken.getType() == TokenTypes.LEFT_PARA) {
         //This is to make the control go back two tokens. This is to make the
         //control point to the keywords INT | FLOAT
         lex.decrementCounter();
         lex.decrementCounter();
         lex.decrementCounter();
         currentToken = lex.getNextToken();
         functionDefinitionHeader();
      } else if (currentToken.getType() == TokenTypes.COMMA
              || currentToken.getType() == TokenTypes.SEMICOLON
              || currentToken.getType() == TokenTypes.ASSIGNMENT
              || currentToken.getType() == TokenTypes.LEFT_SQUARE) {
         //This is to make the control go back two tokens. This is to make the
         //control point to the keywords INT | FLOAT.
         lex.decrementCounter();
         lex.decrementCounter();
         lex.decrementCounter();
         currentToken = lex.getNextToken();
         variableDeclaration();
      } else {
         System.out.println("ERROR on linenumber: " + currentToken.getLineNumber());
      }
   }

   public void functionDefinitionHeader() {
      //This method is called when the program encounters left paranthesis
      //INT | FLOAT is matched and the parser is advanced
      match(currentToken.getType());
      //Identifier is matched and the parser is advanced
      match(TokenTypes.IDENTIFIER);
      //'(' is matched and the parser is advanced
      match(TokenTypes.LEFT_PARA);
      //Now parameters declaration list is called
      parametersDeclarationList();
      //')' is matched and the parser is advanced
      match(TokenTypes.RIGHT_PARA);
      //Now, if there is a semicolon, it means that it is a function declaration
      //and hence, the method is called
      if (currentToken.getType() == TokenTypes.SEMICOLON) {
         functionDeclaration();
      } else if (currentToken.getType() == TokenTypes.LEFT_CURLY) {
         //else if { is matched, then it means it is the starting of
         //function body and hence the method is called
         functionBody();
      }
   }

   public void functionDeclaration() {
      //declaration ends with a ';'
      match(TokenTypes.SEMICOLON);
   }

   public void functionBody() {
      //{ is matched
      match(TokenTypes.LEFT_CURLY);
      //call variable declaration list as the functon body may have varables declared
      variableDeclarationList();
      //and it may have list of statements
      statementList();
      match(TokenTypes.RIGHT_CURLY);
   }

   public void variableDeclaration() {
      if (currentToken.getType() == TokenTypes.KW_VOID) {
         //Variable cannot be declared with the type void
         //Report error
         System.out.println("ERROR on linenumber: " + currentToken.getLineNumber());
      } else if (currentToken.getType() == TokenTypes.KW_INT || currentToken.getType() == TokenTypes.KW_FLOAT) {
         match(currentToken.getType());
         //call identifier list if keywords INT | FLOAT have been encountered
         identifierList();
         //End declarations and match wth a ';'
         match(TokenTypes.SEMICOLON);
      }

   }

   public void identifierList() {
      //Ex: int a,b,c=0;
      identifierDefinition();
      while (currentToken.getType() == TokenTypes.COMMA) {
         //if a ',' is encountered then match it and advance to the method functionDefinition
         match(TokenTypes.COMMA);
         identifierDefinition();
      }
   }

   public void identifierDefinition() {
      //after matching identifier check for '=' | '['
      match(TokenTypes.IDENTIFIER);
      if (currentToken.getType() == TokenTypes.ASSIGNMENT) {
         match(TokenTypes.ASSIGNMENT);
         constant();
      } else if (currentToken.getType() == TokenTypes.LEFT_SQUARE) {
         //dimension declaration for arrays
         dimensionDeclaration();
      }
   }

   public void dimensionDeclaration() {
      //Ex: [10]
      match(TokenTypes.LEFT_SQUARE);
      match(TokenTypes.INTEGER);
      match(TokenTypes.RIGHT_SQUARE);
      //This is for elimination of left recursion using while loop
      while (currentToken.getType() == TokenTypes.LEFT_SQUARE) {
         match(TokenTypes.LEFT_SQUARE);
         match(TokenTypes.INTEGER);
         match(TokenTypes.RIGHT_SQUARE);
      }
   }

   public void variableDeclarationList() {
      //call variable declaration and then eliminate left recursion 
      variableDeclaration();
      while (currentToken.getType() == TokenTypes.KW_INT || currentToken.getType() == TokenTypes.KW_FLOAT) {
         variableDeclaration();
      }
   }

   public void parametersDeclaration() {

      if (currentToken.getType() == TokenTypes.KW_FLOAT || currentToken.getType() == TokenTypes.KW_INT) {
         parametersDeclarationList();
      } else {
         match(TokenTypes.RIGHT_PARA);
      }

   }

   public void parametersDeclarationList() {
      if (currentToken.getType() == TokenTypes.KW_FLOAT || currentToken.getType() == TokenTypes.KW_INT) {

         parametersDeclarationSpecification();
         while (currentToken.getType() == TokenTypes.COMMA) {
            match(TokenTypes.COMMA);

            parametersDeclarationSpecification();
         }
      }
   }

   public void parametersDeclarationSpecification() {
      if (currentToken.getType() == TokenTypes.KW_FLOAT || currentToken.getType() == TokenTypes.KW_INT) {
         match(currentToken.getType());
      }
      match(TokenTypes.IDENTIFIER);
      while (currentToken.getType() == TokenTypes.LEFT_SQUARE) {
         match(TokenTypes.LEFT_SQUARE);
         match(TokenTypes.RIGHT_SQUARE);
      }
   }

   public void statementList() {
      //Matching the FIRST of statement
      if ((currentToken.getType() == TokenTypes.KW_RETURN
              || currentToken.getType() == TokenTypes.KW_WHILE
              || currentToken.getType() == TokenTypes.KW_IF
              || currentToken.getType() == TokenTypes.LEFT_CURLY
              || currentToken.getType() == TokenTypes.LEFT_PARA
              || currentToken.getType() == TokenTypes.IDENTIFIER
              || currentToken.getType() == TokenTypes.ADDOP
              || currentToken.getType() == TokenTypes.STRING_CONSTANT
              || currentToken.getType() == TokenTypes.INTEGER
              || currentToken.getType() == TokenTypes.FLOAT)) {
         statement();
      }//Matching the FIRST of statement
      //here we are eliminating left recursion
      while (currentToken.getType() == TokenTypes.KW_RETURN
              || currentToken.getType() == TokenTypes.KW_WHILE
              || currentToken.getType() == TokenTypes.KW_IF
              || currentToken.getType() == TokenTypes.LEFT_CURLY
              || currentToken.getType() == TokenTypes.LEFT_PARA
              || currentToken.getType() == TokenTypes.IDENTIFIER
              || currentToken.getType() == TokenTypes.ADDOP
              || currentToken.getType() == TokenTypes.STRING_CONSTANT
              || currentToken.getType() == TokenTypes.INTEGER
              || currentToken.getType() == TokenTypes.FLOAT) {
         statement();
      }
   }

   public void statement() {
      // return expression ;
      if (currentToken.getType() == TokenTypes.KW_RETURN) {
         match(TokenTypes.KW_RETURN);
         if (currentToken.getType() != TokenTypes.SEMICOLON) {
            expression();
         }
         match(TokenTypes.SEMICOLON);
         //while(expression)
      } else if (currentToken.getType() == TokenTypes.KW_WHILE) {
         match(TokenTypes.KW_WHILE);
         match(TokenTypes.LEFT_PARA);
         expression();
         match(TokenTypes.RIGHT_PARA);
         statement();
         //Ex: if(expression)
      } else if (currentToken.getType() == TokenTypes.KW_IF) {
         match(TokenTypes.KW_IF);
         match(TokenTypes.LEFT_PARA);
         expression();
         match(TokenTypes.RIGHT_PARA);
         statement();
         if (currentToken.getType() == TokenTypes.KW_ELSE) {
            match(TokenTypes.KW_ELSE);
            statement();
         }

      } else if (currentToken.getType() == TokenTypes.LEFT_CURLY) {
         //Ex: {statementList}
         match(TokenTypes.LEFT_CURLY);
         statementList();
         match(TokenTypes.RIGHT_CURLY);
      } else {
         //expression();
         expression();
         match(TokenTypes.SEMICOLON);
      }
   }

   public void expression() {
      relationExpression();
      if (currentToken.getType() == TokenTypes.ASSIGNMENT) {
         match(TokenTypes.ASSIGNMENT);
         relationExpression();
      }
   }

   public void relationExpression() {
      simpleExpression();
      if (currentToken.getType() == TokenTypes.RELOP) {
         match(TokenTypes.RELOP);
         simpleExpression();
      }
   }

   public void simpleExpression() {
      term();
      while (currentToken.getType() == TokenTypes.ADDOP || currentToken.getType() == TokenTypes.SUBOP) {
         match(currentToken.getType());
         term();
      }
   }

   public void term() {
      factor();
      while (currentToken.getType() == TokenTypes.MULOP || currentToken.getType() == TokenTypes.DIVOP) {
         match(currentToken.getType());
         factor();
      }
   }

   public void factor() {
      if (currentToken.getType() == TokenTypes.LEFT_PARA) {
         match(TokenTypes.LEFT_PARA);
         expression();
         match(TokenTypes.RIGHT_PARA);
      } else if (currentToken.getType() == TokenTypes.ADDOP || currentToken.getType() == TokenTypes.SUBOP) {
         match(currentToken.getType());
         factor();
      } else if (currentToken.getType() == TokenTypes.IDENTIFIER) {
         match(TokenTypes.IDENTIFIER);
         if (currentToken.getType() == TokenTypes.LEFT_PARA) {
            match(TokenTypes.LEFT_PARA);
            if (currentToken.getType() == TokenTypes.LEFT_CURLY
                    || currentToken.getType() == TokenTypes.ADDOP
                    || currentToken.getType() == TokenTypes.IDENTIFIER
                    || currentToken.getType() == TokenTypes.STRING_CONSTANT
                    || currentToken.getType() == TokenTypes.INTEGER
                    || currentToken.getType() == TokenTypes.FLOAT) {
               expressionList();
            }
            match(TokenTypes.RIGHT_PARA);
         } else if (currentToken.getType() == TokenTypes.LEFT_SQUARE) {
            factorL();
         }

      } else if (currentToken.getType() == TokenTypes.INTEGER
              || currentToken.getType() == TokenTypes.FLOAT
              || currentToken.getType() == TokenTypes.STRING_CONSTANT) {
         match(currentToken.getType());
      }
   }

   public void factorL() {
      while (currentToken.getType() == TokenTypes.LEFT_SQUARE) {
         match(TokenTypes.LEFT_SQUARE);
         expression();
         match(TokenTypes.RIGHT_SQUARE);
      }
   }

   public void constant() {
      if (currentToken.getType() == TokenTypes.STRING_CONSTANT) {
         match(TokenTypes.STRING_CONSTANT);
      } else if (currentToken.getType() == TokenTypes.FLOAT
              || currentToken.getType() == TokenTypes.INTEGER) {
         numericConstant();
      }
   }

   public void numericConstant() {
      if (currentToken.getType() == TokenTypes.FLOAT
              || currentToken.getType() == TokenTypes.INTEGER) {
         match(currentToken.getType());
      }
   }

   public void expressionList() {
      expression();
      while (currentToken.getType() == TokenTypes.COMMA) {
         match(TokenTypes.COMMA);
         expression();
      }
   }

   // match the currentToken against the tokenType specified
   // and advance to the next token
   private boolean match(TokenTypes tokenType) {
      if (currentToken == null) {
         System.out.println("unexpected eof");
         return false;
      }
      if (tokenType == currentToken.getType()) {
         //This commented print statement is just to check the tokens that are being sent
         //by the lexical analyzer and matched by the syntax analyzer
         //System.out.println(currentToken.getLexeme() + " " + currentToken.getType() + " " + currentToken.getLineNumber());
         currentToken = lex.getNextToken();
         return true;
      } else {
         System.out.println("token " + tokenType + " is expected on line " + currentToken.getLineNumber());
         return false;

      }
   }
}
