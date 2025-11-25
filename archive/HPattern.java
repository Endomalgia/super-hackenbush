
class HPattern {

  private String pattern;

  /* SHORTHAND NOTATION
    0       > green
    +       > blue
    -       > red
    [a-z]   > identifier, allows loops & related connections
    ()      > establishes hierarchy

    +a(+(-0+(+a))) = a blue apple with a red stem and green leaf
  */

  public applyPattern(String shorthand) {

  }

  public applyPattern(GeoNode noder, String shorthand) {

  }

  public isValid() {

  }

}



public class Matching {

  public Matching() {}

  public static boolean nestParen(String n) {
    int len = n.length();
    Boolean result = false;
    int par_index = -1;

    if (n == null) {return false;}
    if (len == 0) {return true;}

    for (int i=0; i<len; i++) {
      Character c = n.charAt(i);
      // If you get an ( save its location. Set this as the first ( you encounter
      if (!result & c == '(') {
        result = true;
        par_index = i;
        continue;
      }
      // If youve seen a ( and find a ) return the string without those specific parens
      // if no ( has been found return false, this is a lost cause
      /*
        ()     ->  **
        (()()) ->  *(*())
        ()())  ->  **())
      */
      if (c == ')') {
        if (result == false) {return false;}
        if (i+1 == len) {
          // Check could be circumvented by checking if any other ( are found after the first
          System.out.println("\t"+n+"->"+n.substring(par_index+1, i));
          return nestParen(n.substring(par_index+1, i));
        }
        System.out.println("\t"+n+"->"+n.substring(par_index+1, i) + n.substring(i+1));
        return nestParen(n.substring(par_index+1, i) + n.substring(i+1));
      }
    }
    return !result; // If there has been a ( return false, otherwise true
  }
}
