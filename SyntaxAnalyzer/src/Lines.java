/*
 *
 * @author: Mohnish Thallavajhula
 * @ID: 800606747
 * @Webmail: mohnish.thallavajhula631@wku.edu
 */

/*This class is used to create an arraylist to hold the code
without the comments. i.e. the code with the comments removed
each line of code will be stored as a string along with the line numbers
 */
public class Lines {

   String eachLine;
   int lineNumber;

   public Lines(String line, int lineNumber) {
      this.eachLine = line;
      this.lineNumber = lineNumber;
   }
}
