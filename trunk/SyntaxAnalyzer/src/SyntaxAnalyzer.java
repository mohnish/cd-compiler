
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
         System.out.println("ERROR on linenumber: " + currentToken.getLineNumber());
      }
      while (currentToken.getType() == TokenTypes.KW_FLOAT
              || currentToken.getType() == TokenTypes.KW_INT
              || currentToken.getType() == TokenTypes.KW_VOID) {
         externalDeclaration();
      }

   }

   public void externalDeclaration() {
      //todo
      if (currentToken.getType() == TokenTypes.KW_INT) {
         match(TokenTypes.KW_INT);
      } else if (currentToken.getType() == TokenTypes.KW_FLOAT) {
         match(TokenTypes.KW_FLOAT);
      } else if (currentToken.getType() == TokenTypes.KW_VOID) {
         match(TokenTypes.KW_VOID);
      }
      match(TokenTypes.IDENTIFIER);
      if (currentToken.getType() == TokenTypes.LEFT_PARA) {
         functionDefinition();
      } else if (currentToken.getType() == TokenTypes.COMMA
              || currentToken.getType() == TokenTypes.SEMICOLON
              || currentToken.getType() == TokenTypes.ASSIGNMENT
              || currentToken.getType() == TokenTypes.LEFT_SQUARE) {
         variableDeclaration();
      } else {
         //report error
         System.out.println("ERROR on linenumber: " + currentToken.getLineNumber());
      }
   }

   public void functionDefinition() {
      functionDefinitionHeader();
      if (currentToken.getType() == TokenTypes.SEMICOLON) {
         functionDeclaration();
      } else if (currentToken.getType() == TokenTypes.LEFT_CURLY) {
         functionBody();
      } else {
         //Error reporting
         System.out.println("ERROR on linenumber: " + currentToken.getLineNumber());
      }
   }

   public void functionDefinitionHeader() {
      match(TokenTypes.LEFT_PARA);
      parametersDeclaration();
      match(TokenTypes.RIGHT_PARA);
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
      //todo
      identifierDefinition();
      while (currentToken.getType() == TokenTypes.COMMA) {
         match(TokenTypes.COMMA);
         identifierDefinition();
      }
   }

   public void identifierDefinition() {
      //todo
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
      parametersDeclarationList();

   }

   public void parametersDeclarationList() {
      if (currentToken.getType() == TokenTypes.KW_FLOAT || currentToken.getType() == TokenTypes.KW_INT) {
         parametersDeclarationSpecification();
         while (currentToken.getType() == TokenTypes.COMMA) {
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
      //todo
      statement();
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
      while (currentToken.getType() == TokenTypes.ADDOP) {
         term();
      }
   }

   public void term() {
      factor();
      while (currentToken.getType() == TokenTypes.MULOP) {
         factor();
      }
   }

   public void factor() {
      if (currentToken.getType() == TokenTypes.LEFT_PARA) {
         match(TokenTypes.LEFT_PARA);
         expression();
         match(TokenTypes.RIGHT_PARA);
      } else if (currentToken.getType() == TokenTypes.ADDOP) {
         match(TokenTypes.ADDOP);
         factor();
      } else if (currentToken.getType() == TokenTypes.IDENTIFIER) {
         match(TokenTypes.IDENTIFIER);
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
      } else if (currentToken.getType() == TokenTypes.INTEGER
              || currentToken.getType() == TokenTypes.FLOAT
              || currentToken.getType() == TokenTypes.STRING_CONSTANT) {
         constant();
      } else if (currentToken.getType() == TokenTypes.IDENTIFIER) {
         factorL();
      }
   }

   public void factorL() {
      match(TokenTypes.IDENTIFIER);
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

   public void expressionList(){
      expression();
      while(currentToken.getType() == TokenTypes.SEMICOLON){
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
         System.out.println(currentToken.getLexeme()+ " " + currentToken.getType() + " " + currentToken.getLineNumber());
         currentToken = lex.getNextToken();
         return true;
      } else {
         System.out.println("token " + tokenType + " is expected on line " + currentToken.getLineNumber());
         return false;

      }
   }
}
//   private int getIntValue(Token cur) {
//      return Integer.parseInt(cur.getLexeme());
//   }
// apply the operator on the left and right and produce the result
//   	private int apply(Token opToken, int left, int right) {
//   		String op = opToken.getLexeme();
//   		if(op.equals("+")) {
//   			return left + right;
//   		}
//   		else if(op.equals("-")) {
//   			return left - right;
//   		}
//   		else if(op.equals("*")) {
//   			return left * right;
//   		}
//   		else if(op.equals("/")) {
//   			return left / right;
//   		}
//   		// hopefully we never reach pt
//   		return 0;
//   	}
//    public int expression() {
//    	int left = term();
//    	int right;
//    	while(currentToken != null && currentToken.getType() == TokenTypes.ADDOP) {
//    		Token op = currentToken;
//    		match(tokentypes.ADDOPERATOR);
//    		right = term();
//    		// apply the operator on left and right and store the result back to left
//    		left = apply(op, left, right);
//    	}
//    	return left;
//    }
//
//    public int term() {
//    	int left = factor();
//    	int right;
//    	while(currentToken != null && currentToken.getType() == tokentypes.MULOPERATOR) {
//    		Token op = currentToken;
//    		match(tokentypes.MULOPERATOR);
//    		right = factor();
//    		// apply the operator on left and right and store the result back to left
//    		left = apply(op, left, right);
//    	}
//    	return left;
//
//    }
//
//    public int factor() {
//    	int value = 0;
//    	if(currentToken.getType() == tokentypes.INTEGERCONST) {
//    		value = getIntValue(currentToken);
//    		match(tokentypes.INTEGERCONST);
//    	}
//    	else if (currentToken.getType() == tokentypes.LEFTPARANTHESIS) {
//    		match(tokentypes.LEFTPARANTHESIS);
//    		value = expression();
//    		match(tokentypes.RIGHTPARANTHESIS);
//    	}
//
//    	return value;
//    }
//   boolean firstOfExternalDeclaration() {
//      if (currentToken.getType() == TokenTypes.KW_FLOAT
//              || currentToken.getType() == TokenTypes.KW_INT
//              || currentToken.getType() == TokenTypes.KW_VOID) {
//         return true;
//      } else {
//         return false;
//      }
//   }

