
/*
 * expr = expr + term | term
 * term = term * factor | factor
 * factor = number | ( expr )
 *
 */
public class SyntaxAnalyzer {

   Scanner lex = null;
   Token currentToken = null;

   public SyntaxAnalyzer(String fileName) throws Exception {
      lex = new Scanner(fileName);
      currentToken = lex.getNextToken();
   }

   public void parse() {
      translationUnit();
   }

   public void translationUnit() {
      if (currentToken != null && (currentToken.getType() == TokenTypes.KW_FLOAT
              || currentToken.getType() == TokenTypes.KW_INT
              || currentToken.getType() == TokenTypes.KW_VOID)) {
         externalDeclaration();
      } else {
         //report error for not being int/float/void
         System.out.println("ERROR in TU on linenumber: " + currentToken.getLineNumber());
      }
      while (currentToken != null && (currentToken.getType() == TokenTypes.KW_FLOAT
              || currentToken.getType() == TokenTypes.KW_INT
              || currentToken.getType() == TokenTypes.KW_VOID)) {
         externalDeclaration();
      }

   }

   public void externalDeclaration() {
      if (currentToken.getType() == TokenTypes.KW_INT) {
         currentToken = lex.getNextToken();
      } else if (currentToken.getType() == TokenTypes.KW_FLOAT) {
         currentToken = lex.getNextToken();
      } else if (currentToken.getType() == TokenTypes.KW_VOID) {
         currentToken = lex.getNextToken();
      }
      currentToken = lex.getNextToken();
      if (currentToken.getType() == TokenTypes.LEFT_PARA) {
         lex.decrementCounter();
         lex.decrementCounter();
         lex.decrementCounter();
         currentToken = lex.getNextToken();
         functionDefinitionHeader();
      } else if (currentToken.getType() == TokenTypes.COMMA
              || currentToken.getType() == TokenTypes.SEMICOLON
              || currentToken.getType() == TokenTypes.ASSIGNMENT
              || currentToken.getType() == TokenTypes.LEFT_SQUARE) {
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
      match(currentToken.getType());
      match(TokenTypes.IDENTIFIER);
      match(TokenTypes.LEFT_PARA);
      parametersDeclarationList();
      match(TokenTypes.RIGHT_PARA);
      if (currentToken.getType() == TokenTypes.SEMICOLON) {
         functionDeclaration();
      } else if (currentToken.getType() == TokenTypes.LEFT_CURLY) {
         functionBody();
      }
   }

   public void functionDeclaration() {
      match(TokenTypes.SEMICOLON);
   }

   public void functionBody() {
      match(TokenTypes.LEFT_CURLY);
      variableDeclarationList();
      statementList();
      match(TokenTypes.RIGHT_CURLY);
   }

   public void variableDeclaration() {
      if (currentToken.getType() == TokenTypes.KW_VOID) {
         //Report error
      } else if (currentToken.getType() == TokenTypes.KW_INT || currentToken.getType() == TokenTypes.KW_FLOAT) {
         match(currentToken.getType());
         identifierList();
         match(TokenTypes.SEMICOLON);
      }

   }

   public void identifierList() {
      identifierDefinition();
      while (currentToken.getType() == TokenTypes.COMMA) {
         match(TokenTypes.COMMA);
         identifierDefinition();
      }
   }

   public void identifierDefinition() {
      match(TokenTypes.IDENTIFIER);
      if (currentToken.getType() == TokenTypes.ASSIGNMENT) {
         match(TokenTypes.ASSIGNMENT);
         constant();
      } else if (currentToken.getType() == TokenTypes.LEFT_SQUARE) {
         dimensionDeclaration();
      }
   }

   public void dimensionDeclaration() {
      match(TokenTypes.LEFT_SQUARE);
      match(TokenTypes.INTEGER);
      match(TokenTypes.RIGHT_SQUARE);
      while (currentToken.getType() == TokenTypes.LEFT_SQUARE) {
         match(TokenTypes.LEFT_SQUARE);
         match(TokenTypes.INTEGER);
         match(TokenTypes.RIGHT_SQUARE);
      }
   }

   public void variableDeclarationList() {
      //todo
      variableDeclaration();
      while (currentToken.getType() == TokenTypes.KW_INT || currentToken.getType() == TokenTypes.KW_FLOAT) {
         variableDeclaration();
      }
   }

   public void parametersDeclaration() {
      if (currentToken.getType() == TokenTypes.KW_FLOAT || currentToken.getType() == TokenTypes.KW_INT) {
         parametersDeclarationList();
//         match(TokenTypes.RIGHT_PARA);
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
//         parametersDeclarationSpecification();
      }
   }

   public void statementList() {

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
      }
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
      //todo
      if (currentToken.getType() == TokenTypes.KW_RETURN) {
         match(TokenTypes.KW_RETURN);
         if (currentToken.getType() != TokenTypes.SEMICOLON) {
            expression();
         }
         match(TokenTypes.SEMICOLON);
      } else if (currentToken.getType() == TokenTypes.KW_WHILE) {
         match(TokenTypes.KW_WHILE);
         match(TokenTypes.LEFT_PARA);
         expression();
         match(TokenTypes.RIGHT_PARA);
         statement();
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
         match(TokenTypes.LEFT_CURLY);
         statementList();
         match(TokenTypes.RIGHT_CURLY);
      } else {
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
//         constant();
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
         System.out.println(currentToken.getLexeme() + " " + currentToken.getType() + " " + currentToken.getLineNumber());
         currentToken = lex.getNextToken();
         return true;
      } else {
         System.out.println("token " + tokenType + " is expected on line " + currentToken.getLineNumber());
         return false;

      }
   }
}
