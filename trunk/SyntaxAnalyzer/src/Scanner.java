/*
 *
 * @author: Mohnish Thallavajhula
 * @ID: 800606747
 * @Webmail: mohnish.thallavajhula631@wku.edu
 */

import java.io.*;
import java.util.*;

public class Scanner {

   String fileName;
   BufferedReader readContent;
   ArrayList<Lines> lineList;
   ArrayList<Token> token;
   int counter = 0;

   public Scanner(String fileName) {
      this.fileName = fileName;
      if (this.fileName.endsWith(".c") || this.fileName.endsWith(".C")) {
         tokenize();
      } else {
         System.out.println("The file '" + this.fileName
                 + "' has an unsupported file extension. "
                 + "Please use only files with '.c' extension.");
      }

   }

   public Token getNextToken() {
      if (counter < token.size()) {
         Token finalToken = token.get(counter);
         counter++;
         return finalToken;
      } else {
         return null;
      }
   }

   public void decrementCounter() {
      if(counter > 0){
         counter -= 1;
      }
   }

   public void tokenize() {

      //linelist holds the entire code lines along with the comments
      lineList = new ArrayList<Lines>();
      token = new ArrayList<Token>();//this list contains the final tokens
      //this arraylist holds the contents of the
      //along with the corresponding line numbers. comments will not be loaded
      //into this arraylist.
      ArrayList<Lines> actualFile = new ArrayList<Lines>();

      try {
         String fileContents;
         char[] eachChar;
         boolean isComment = false;
         int lineNumber = 0;
         readContent = new BufferedReader(new FileReader(new File(this.fileName)));
         //first parse to remove comments from the program at the same
         //time preserving the original line numbers
         //START PARSE 1
         while ((fileContents = readContent.readLine()) != null) {
            lineNumber++;
            lineList.add(new Lines(fileContents, lineNumber));
         }
         //added all the lines into the arraylist: lineList

         int actualLineNumber;
         String actualLine = "";

         for (int looper = 0; looper < lineList.size(); looper++) {
            actualLineNumber = looper + 1;
            eachChar = lineList.get(looper).eachLine.toCharArray();

            //checking for comments
            for (int charLoop = 0; charLoop < eachChar.length; charLoop++) {
               if (eachChar[charLoop] == '/' && charLoop + 1 < eachChar.length && eachChar[charLoop + 1] == '*') {
                  isComment = true;
               }
               if (isComment) {
                  if (eachChar[charLoop] == '*' && charLoop + 1 < eachChar.length && eachChar[charLoop + 1] == '/') {
                     charLoop++;
                     isComment = false;
                  }
               } else {
                  actualLine = actualLine + eachChar[charLoop];
               }
            }
            actualFile.add(new Lines(actualLine, actualLineNumber));
            actualLine = "";
         }
         //END PARSE 1
         //we now have the actual program, as it is, with the comments removed
         //and the line numbers preserved

      } catch (FileNotFoundException ex) {
         System.out.println("File not found " + ex);
      } catch (IOException ex) {
         System.out.println("Error reading the input file " + ex);
      } catch (Exception e) {
         System.out.println("File Exception Occurred: " + e);
      }

      for (int i = 0; i < actualFile.size(); i++) {
         final char[] currentChar = actualFile.get(i).eachLine.toCharArray();
         String tokenValue = "";
         for (int k = 0; k < currentChar.length; k++) {

            //**************************
            //START TOKENIZING FROM HERE
            //**************************

            tokenValue = "";
            boolean quoteEncountered = false;
            //test string constants
            if (currentChar[k] == '"') {
               //a quote is encountered and the string starts
               quoteEncountered = true;
               k++;//advance to the next character
               if (k < currentChar.length) {
                  while (quoteEncountered) {
                     if (k < currentChar.length) {
                        if (currentChar[k] != '"') {
                           tokenValue += currentChar[k];
                           k++;
                        } else {
                           token.add(new Token(TokenTypes.STRING_CONSTANT, tokenValue, actualFile.get(i).lineNumber));
                           quoteEncountered = false;
                        }
                     } else {
                        //just to make sure that quote encountered
                        token.add(new Token(TokenTypes.ERROR, tokenValue, actualFile.get(i).lineNumber));
                        k--;
                        quoteEncountered = false;
                     }
                  }
               }


            } else if (Character.isLetter(currentChar[k])) {
               //test identifiers and keywords
               while (k < currentChar.length && Character.isLetterOrDigit(currentChar[k])) {
                  tokenValue += currentChar[k];
                  k++;
               }
               if (tokenValue.equalsIgnoreCase("int")) {
                  //keyword check
                  token.add(new Token(TokenTypes.KW_INT, tokenValue, actualFile.get(i).lineNumber));
                  k--;
               } else if (tokenValue.equalsIgnoreCase("else")) {
                  //keyword check
                  token.add(new Token(TokenTypes.KW_ELSE, tokenValue, actualFile.get(i).lineNumber));
                  k--;
               } else if (tokenValue.equalsIgnoreCase("if")) {
                  //keyword check
                  token.add(new Token(TokenTypes.KW_IF, tokenValue, actualFile.get(i).lineNumber));
                  k--;
               } else if (tokenValue.equalsIgnoreCase("float")) {
                  token.add(new Token(TokenTypes.KW_FLOAT, tokenValue, actualFile.get(i).lineNumber));
                  k--;
               } else if (tokenValue.equalsIgnoreCase("void")) {
                  token.add(new Token(TokenTypes.KW_VOID, tokenValue, actualFile.get(i).lineNumber));
                  k--;
               } else if (tokenValue.equalsIgnoreCase("return")) {
                  token.add(new Token(TokenTypes.KW_RETURN, tokenValue, actualFile.get(i).lineNumber));
                  k--;
               } else if (tokenValue.equalsIgnoreCase("while")) {
                  token.add(new Token(TokenTypes.KW_WHILE, tokenValue, actualFile.get(i).lineNumber));
                  k--;
               } else { //identifier check
                  token.add(new Token(TokenTypes.IDENTIFIER, tokenValue, actualFile.get(i).lineNumber));
                  k--;
               }

            } else if (Character.isDigit(currentChar[k])) {
               //number starts with 0 then control enters this part
               if (currentChar[k] == '0') {
                  //enters this if the next char is a '.'
                  if (k + 1 < currentChar.length && currentChar[k + 1] == '.') {
                     //when the value contains a '.' it obviously is a float
                     while (k < currentChar.length && (Character.isDigit(currentChar[k]) || currentChar[k] == '.')) {
                        tokenValue += currentChar[k];
                        k++;
                     }
                     token.add(new Token(TokenTypes.FLOAT, tokenValue, actualFile.get(i).lineNumber));
                     k--;
                  } //if the number has digits followed by 0 then the control enters
                  //this part - everything in this token will be tokenizer as error
                  else if (k + 1 < currentChar.length && Character.isDigit(currentChar[k + 1])) {
                     while (k < currentChar.length && (Character.isDigit(currentChar[k]) || currentChar[k] == '.')) {
                        tokenValue += currentChar[k];
                        k++;
                     }
                     token.add(new Token(TokenTypes.ERROR, tokenValue, actualFile.get(i).lineNumber));
                     k--;
                  } //if the number has characters followed by 0 then the control enters
                  //this part - everything in this token will be tokenizer as error
                  else if (k + 1 < currentChar.length && Character.isLetter(currentChar[k + 1])) {
                     while (k < currentChar.length && Character.isLetterOrDigit(currentChar[k])) {
                        tokenValue += currentChar[k];
                        k++;
                     }
                     token.add(new Token(TokenTypes.ERROR, tokenValue, actualFile.get(i).lineNumber));
                     k--;
                  } else {
                     //if it is just a 0, then 0 is returned as integer without any error
                     tokenValue += currentChar[k];
                     token.add(new Token(TokenTypes.INTEGER, tokenValue, actualFile.get(i).lineNumber));
                  }
               } else {
                  //number doesnt start with 0
                  while (k < currentChar.length && (Character.isDigit(currentChar[k]) || currentChar[k] == '.')) {
                     tokenValue += currentChar[k];
                     k++;
                  }//float check
                  if (tokenValue.contains(".")) {
                     //checking for trailing errors
                     String test = tokenValue;
                     char[] testChar = test.toCharArray();
                     if (testChar[testChar.length - 1] == '0' && testChar[testChar.length - 2] == '.') {
                        token.add(new Token(TokenTypes.FLOAT, tokenValue, actualFile.get(i).lineNumber));
                        k--;
                     } else if (testChar[testChar.length - 1] != '0') {
                        //lo
                        token.add(new Token(TokenTypes.FLOAT, tokenValue, actualFile.get(i).lineNumber));
                     } else {
                        token.add(new Token(TokenTypes.ERROR, tokenValue, actualFile.get(i).lineNumber));
                     }

                  } else {
                     //integer check
                     token.add(new Token(TokenTypes.INTEGER, tokenValue, actualFile.get(i).lineNumber));
                     k--;
                  }
               }
            } else if (currentChar[k] == '+') {//operators  check
               tokenValue += currentChar[k];
               token.add(new Token(TokenTypes.ADDOP, tokenValue, actualFile.get(i).lineNumber));
            } else if (currentChar[k] == '-') {
               tokenValue += currentChar[k];
               token.add(new Token(TokenTypes.SUBOP, tokenValue, actualFile.get(i).lineNumber));
            } else if (currentChar[k] == '*') {
               tokenValue += currentChar[k];
               token.add(new Token(TokenTypes.MULOP, tokenValue, actualFile.get(i).lineNumber));
            } else if (currentChar[k] == '/') {
               tokenValue += currentChar[k];
               token.add(new Token(TokenTypes.DIVOP, tokenValue, actualFile.get(i).lineNumber));
            } else if (currentChar[k] == '{') {
               tokenValue += currentChar[k];
               token.add(new Token(TokenTypes.LEFT_CURLY, tokenValue, actualFile.get(i).lineNumber));
            } else if (currentChar[k] == '}') {
               tokenValue += currentChar[k];
               token.add(new Token(TokenTypes.RIGHT_CURLY, tokenValue, actualFile.get(i).lineNumber));
            } else if (currentChar[k] == '#' || currentChar[k] == '@' || currentChar[k] == '$'
                    || currentChar[k] == '`' || currentChar[k] == '~' || currentChar[k] == '%' || currentChar[k] == '^'
                    || currentChar[k] == '&' || currentChar[k] == '_' || currentChar[k] == '|' || currentChar[k] == '?'
                    || currentChar[k] == '\\') {
               tokenValue += currentChar[k];
               token.add(new Token(TokenTypes.ERROR, tokenValue, actualFile.get(i).lineNumber));
            } else if (currentChar[k] == ')') {
               tokenValue += currentChar[k];
               token.add(new Token(TokenTypes.RIGHT_PARA, tokenValue, actualFile.get(i).lineNumber));
            } else if (currentChar[k] == '(') {
               tokenValue += currentChar[k];
               token.add(new Token(TokenTypes.LEFT_PARA, tokenValue, actualFile.get(i).lineNumber));
            } else if (currentChar[k] == '[') {
               tokenValue += currentChar[k];
               token.add(new Token(TokenTypes.LEFT_SQUARE, tokenValue, actualFile.get(i).lineNumber));
            } else if (currentChar[k] == ']') {
               tokenValue += currentChar[k];
               token.add(new Token(TokenTypes.RIGHT_SQUARE, tokenValue, actualFile.get(i).lineNumber));
            } else if (currentChar[k] == ',') {
               tokenValue += currentChar[k];
               token.add(new Token(TokenTypes.COMMA, tokenValue, actualFile.get(i).lineNumber));
            } else if (currentChar[k] == ';') {
               tokenValue += currentChar[k];
               token.add(new Token(TokenTypes.SEMICOLON, tokenValue, actualFile.get(i).lineNumber));
            } else if (currentChar[k] == '=' || currentChar[k] == '>' || currentChar[k] == '<' || currentChar[k] == '!') {
               tokenValue += currentChar[k];
               if (k + 1 < currentChar.length && currentChar[k + 1] == '=') {
                  k++;
                  tokenValue += currentChar[k];
                  token.add(new Token(TokenTypes.RELOP, tokenValue, actualFile.get(i).lineNumber));
               } else if (currentChar[k] == '<' || currentChar[k] == '>') {
                  token.add(new Token(TokenTypes.RELOP, tokenValue, actualFile.get(i).lineNumber));
               } else if (currentChar[k] == '!') {
                  token.add(new Token(TokenTypes.ERROR, tokenValue, actualFile.get(i).lineNumber));
               } else if (currentChar[k] == '=') {
                  token.add(new Token(TokenTypes.ASSIGNMENT, tokenValue, actualFile.get(i).lineNumber));
               }

            }
         }

      }

   }
}
