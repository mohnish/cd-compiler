/*
 *
 * @author: Mohnish Thallavajhula
 * @ID: 800606747
 * @Webmail: mohnish.thallavajhula631@wku.edu
 */

import java.io.*;
import java.util.*;
import java.util.ArrayList;

public class Scanner {

   String fileName;
   BufferedReader readContent;
   ArrayList<Lines> lineList;
   ArrayList<Token> token;
   int counter = 0;

   public Scanner(String fileName) throws FileNotFoundException {
      this.fileName = fileName;
      if (this.fileName.endsWith(".c") || this.fileName.endsWith(".C")) {
         tokenize();
      } else {
         System.out.println("The file '" + this.fileName + "' has an unsupported file extension. "
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
         }//added all the lines into the arraylist: lineList

//         actualFile = new ArrayList<Lines>();
         int actualLineNumber;
         String actualLine = "";
         for (int looper = 0; looper < lineList.size(); looper++) {
            actualLineNumber = looper + 1;
            eachChar = lineList.get(looper).eachLine.toCharArray();
            //checking for comments
            for (int charLoop = 0; charLoop < eachChar.length; charLoop++) {
               if (eachChar[charLoop] == '/') {
                  if (charLoop + 1 < eachChar.length) {
                     if (eachChar[charLoop + 1] == '*') {
                        isComment = true;
                     }
                  }
               }
               if (isComment) {
                  if (eachChar[charLoop] == '*') {
                     if (charLoop + 1 < eachChar.length) {
                        if (eachChar[charLoop + 1] == '/') {
                           charLoop++;//to skip the ending forward slash of a comment
                           //i.e. /* khfkdsfsd *'/'
                           isComment = false;
                        }
                     }
                  }
               } else {
                  actualLine = actualLine + eachChar[charLoop];
               }
            }
            actualFile.add(new Lines(actualLine, actualLineNumber));
            actualLine = "";
         }
         //END PARSE 1
         //we now have the actual program as it is with the comments removed
         //and the line numbers preserved

      } catch (FileNotFoundException ex) {
         System.out.println("File not found " + ex);
      } catch (IOException ex) {
         System.out.println("Error reading the input file " + ex);
      } catch (Exception e) {
         System.out.println("File Exception Occurred: " + e);
      }

      for (int i = 0; i < actualFile.size(); i++) {
//         System.out.println(actualFile.get(i).eachLine + " " + actualFile.get(i).lineNumber);
         char[] sampleChar = actualFile.get(i).eachLine.toCharArray();
         String testString = "";
         for (int k = 0; k < sampleChar.length; k++) {
            testString = "";
            //test string constants
            if (sampleChar[k] == '"') {
               k++;
               while (sampleChar[k] != '"') {
                  testString += sampleChar[k];
                  k++;
               }
//               System.out.println("String: " + testString + " " + actualFile.get(i).lineNumber);
               token.add(new Token((short) 16, testString, actualFile.get(i).lineNumber));
            } else if (Character.isLetter(sampleChar[k])) {//test identifiers and keywords

               while (k < sampleChar.length && (Character.isLetter(sampleChar[k]) || Character.isDigit(sampleChar[k]))) {
                  testString += sampleChar[k];
                  k++;
               }
               if (isKeyword(testString)) {//keyword check
//                  System.out.println("Keyword: " + testString + " " + actualFile.get(i).lineNumber);
                  token.add(new Token((short) 1, testString, actualFile.get(i).lineNumber));
                  k--;
               } else {//identifier check
//                  System.out.println("Identifier: " + testString + " " + actualFile.get(i).lineNumber);
                  token.add(new Token((short) 2, testString, actualFile.get(i).lineNumber));
                  k--;
               }

            } else if (Character.isDigit(sampleChar[k])) {
               //number check
               while (Character.isDigit(sampleChar[k]) || sampleChar[k] == '.') {
                  testString += sampleChar[k];
                  k++;
               }//float check
               if (testString.contains(".")) {
//                  System.out.println("Float: " + testString + " " + actualFile.get(i).lineNumber);
                  token.add(new Token((short) 3, testString, actualFile.get(i).lineNumber));
                  k--;
               } else {//integer check
//                  System.out.println("Integer: " + testString + " " + actualFile.get(i).lineNumber);
                  token.add(new Token((short) 4, testString, actualFile.get(i).lineNumber));
                  k--;
               }
            } else if (sampleChar[k] == '+') {//operators  check
               testString += sampleChar[k];
//               System.out.println("Addition Op " + testString+ " " + actualFile.get(i).lineNumber);
               token.add(new Token((short) 5, testString, actualFile.get(i).lineNumber));
            } else if (sampleChar[k] == '-') {
               testString += sampleChar[k];
//               System.out.println("Subtraction Op " + testString+ " " + actualFile.get(i).lineNumber);
               token.add(new Token((short) 6, testString, actualFile.get(i).lineNumber));
            } else if (sampleChar[k] == '*') {
               testString += sampleChar[k];
//               System.out.println("Multiplication Op " + testString+ " " + actualFile.get(i).lineNumber);
               token.add(new Token((short) 7, testString, actualFile.get(i).lineNumber));
            } else if (sampleChar[k] == '/') {
               testString += sampleChar[k];
//               System.out.println("Division Op " + testString+ " " + actualFile.get(i).lineNumber);
               token.add(new Token((short) 8, testString, actualFile.get(i).lineNumber));
            } else if (sampleChar[k] == '{') {
               testString += sampleChar[k];
//               System.out.println("BraceOpen " + testString+ " " + actualFile.get(i).lineNumber);
               token.add(new Token((short) 9, testString, actualFile.get(i).lineNumber));
            } else if (sampleChar[k] == '}') {
               testString += sampleChar[k];
//               System.out.println("BraceClosed " + testString+ " " + actualFile.get(i).lineNumber);
               token.add(new Token((short) 10, testString, actualFile.get(i).lineNumber));
            } else if (sampleChar[k] == '#' || sampleChar[k] == '@' || sampleChar[k] == '$'
                    || sampleChar[k] == '`' || sampleChar[k] == '~' || sampleChar[k] == '%' || sampleChar[k] == '^'
                    || sampleChar[k] == '&' || sampleChar[k] == '-' || sampleChar[k] == '|' || sampleChar[k] == '?'
                    || sampleChar[k] == '\\') {
               testString += sampleChar[k];
//               System.out.println("Error " + testString+ " " + actualFile.get(i).lineNumber);
               token.add(new Token((short) 15, testString, actualFile.get(i).lineNumber));
            } else if (sampleChar[k] == ')') {
               testString += sampleChar[k];
//               System.out.println("ParanthesisClosed " + testString+ " " + actualFile.get(i).lineNumber);
               token.add(new Token((short) 11, testString, actualFile.get(i).lineNumber));
            } else if (sampleChar[k] == '(') {
               testString += sampleChar[k];
//               System.out.println("ParanthesisOpen " + testString+ " " + actualFile.get(i).lineNumber);
               token.add(new Token((short) 12, testString, actualFile.get(i).lineNumber));
            } else if (sampleChar[k] == ';' || sampleChar[k] == '[' || sampleChar[k] == ']' || sampleChar[k] == ',' || sampleChar[k] == '=') {
               testString += sampleChar[k];
//               System.out.println("Other " + testString+ " " + actualFile.get(i).lineNumber);
               token.add(new Token((short) 13, testString, actualFile.get(i).lineNumber));
            } else if (sampleChar[k] == '=' || sampleChar[k] == '>' || sampleChar[k] == '<' || sampleChar[k] == '!') {
               testString += sampleChar[k];
               if (sampleChar[k + 1] == '=') {
                  testString += sampleChar[k + 1];
                  k++;
//                  System.out.println("Relation Op " + testString+ " " + actualFile.get(i).lineNumber);
                  token.add(new Token((short) 14, testString, actualFile.get(i).lineNumber));
               } else if (sampleChar[k] == '<' || sampleChar[k] == '>') {
//                  System.out.println("Relation Op "+  sampleChar[k]+ " " + actualFile.get(i).lineNumber);
                  token.add(new Token((short) 14, testString, actualFile.get(i).lineNumber));
               }
               if (sampleChar[k] == '!') {
//                     System.out.println("Error "+  sampleChar[k]+ " " + actualFile.get(i).lineNumber );
                  token.add(new Token((short) 15, testString, actualFile.get(i).lineNumber));
               }

            }
         }

      }

   }

   boolean isKeyword(String tokenString) {
      if (tokenString.equals("void") || tokenString.equals("main")
              || tokenString.equals("return") || tokenString.equals("if")
              || tokenString.equals("else") || tokenString.equals("while")) {
         return true;
      } else {
         return false;
      }
   }
}


