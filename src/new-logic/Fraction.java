import java.util.List;
import java.util.ArrayList;

/**
 * This Fraction class stores a numerator and denominator and
 * has various common mathematical functions that Fractions
 * can preform. Some of the methods are static and others are
 * non-static. Fill in all of the parts of the program below
 * that say // YOUR CODE GOES HERE // and delete return null.
 *
 * ALL CREDIT GOES TO https://github.com/akshitasantra ON GITHUB, tysm.
 * I didnt see a license on your page but I can remove this if you ever want me too
 * To clarify all but 2 of these functions arent mine.
 *
 */
public class Fraction implements Comparable<Fraction> {
    private int numerator;
    private int denominator;

    /**
     * This is the default constructor, it should start out
     * this Fraction's numerator as 0 and the denominator as 1.
     */
    public Fraction() {
        numerator = 0;
        denominator = 1;
    }

    /**
     * Starts this Fraction using a given numerator and denominator
     * @param numerator A numerator to use for this Fraction
     * @param denominator A denominator to use for this Fraction
     */
    public Fraction(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    /**
     * Getter method that returns the numerator
     * @return This Fraction's numerator
     */
    public int getNumerator() {
        return numerator;
    }

    /**
     * Getter method that returns the denominator
     * @return This Fraction's denominator
     */
    public int getDenominator() {
        return denominator;
    }

    /**
     * Setter method that changes this Fraction's numerator
     * @param numerator A new numerator to use for this Fraction
     */
    public void setNumerator(int numerator) {
        this.numerator = numerator;
    }

    /**
     * Setter method that changes this Fraction's denominator
     * @param denominator A new denominator to use for this Fraction
     */
    public void setDenominator(int denominator) {
        if(denominator > 0){
            this.denominator = denominator;
        }
    }

    /**
     * This method returns the decimal value of this Fraction
     * @return This Fraction's value as a decimal.
     */
    public double getDecimalValue() {
        return (double) numerator/denominator;
    }

    /**
     * Overrides default toString, prints out "#/#".
     * @return This Fraction as a String of "#/#".
     */
    @Override
    public String toString() {
        return this.numerator+"/"+this.denominator;
    }

    /**
     * Compares to fractions by finding a common denominator and comparing their numerators
     * @param val The fraction to compare to.
     * @return    An int value representing the result of the comparison
     */
    public int compareTo(Fraction val) {
        return ((Double)getDecimalValue()).compareTo(val.getDecimalValue());
    }

    /**
     * This non-static method adds a given Fraction into this one.
     * It should modify this.numerator and this.denominator.
     * If they don't have a common denominator, just multiply them
     * together and leave the result un-reduced.
     * @param a Fraction a to add with the current Fraction.
     */
    public void add(Fraction a) {
        if(this.denominator == a.denominator){
            this.numerator += a.numerator;
        } else {
            this.numerator = (a.denominator*this.numerator) + (this.denominator*a.numerator);
            this.denominator = a.denominator * this.denominator;
        }
    }

    /**
     * This static method adds two given Fractions together
     * and returns a new Fraction containing the answer.
     * If they don't have a common denominator, just multiply them
     * together and leave the result un-reduced.
     * Do not use this.numerator or this.denominator.
     * @param a Fraction a to add with b.
     * @param b Fraction b to add with a.
     * @return A new Fraction that is a plus b.
     */
    public static Fraction add(Fraction a, Fraction b) {
        Fraction c = new Fraction();
        if(b.getDenominator() == a.getDenominator()){
            a.numerator += b.getNumerator();
        }
        else{
            c.numerator = (a.getDenominator()*b.getNumerator())+ (b.getDenominator()*a.getNumerator());
            c.denominator = a.getDenominator() * b.getDenominator();
        }
        return c;
    }

    /**
     * This non-static method subtracts a given Fraction from this one.
     * It should modify this.numerator and this.denominator.
     * If they don't have a common denominator, just multiply them
     * together and leave the result un-reduced.
     * @param a Fraction a to add with the current Fraction.
     */
    public void subtract(Fraction a) {
        if(this.denominator == a.denominator){
            this.numerator -= a.numerator;
        }
        else{
            this.numerator = (a.denominator*this.numerator)-(this.denominator*a.numerator);
            this.denominator = a.denominator * this.denominator;
        }
    }

    /**
     * This static method subtracts two given Fractions together
     * and returns a new Fraction containing the answer.
     * If they don't have a common denominator, just multiply them
     * together and leave the result un-reduced.
     * Do not use this.numerator or this.denominator.
     * @param a Fraction a which will have b subtracted from it.
     * @param b Fraction b to subtract from a.
     * @return A new Fraction that is a minus b.
     */
    public static Fraction subtract(Fraction a, Fraction b) {
        Fraction c = new Fraction();
        if(b.denominator == a.getDenominator()){
            a.numerator -= b.getNumerator();
        }
        else{
            c.numerator = (b.getDenominator()*a.getNumerator())-(a.getDenominator()*b.getNumerator());
            c.denominator = a.getDenominator() * b.getDenominator();
        }
        return c;
    }

    /**
     * This non-static method multiplies a given Fraction with this one.
     * It should modify this.numerator and this.denominator.
     * @param a Fraction a to multiply with the current Fraction.
     */
    public void multiply(Fraction a) {
        this.numerator = this.numerator * a.numerator;
        this.denominator = this.denominator * a.denominator;
    }

    /**
     * This static method multiplies Fraction a and b.
     * Do not use this.numerator or this.denominator.
     * @param a Fraction a to multiply with b.
     * @param b Fraction b to multiply with a.
     * @return A new Fraction that is a times b.
     */
    public static Fraction multiply(Fraction a, Fraction b) {
        Fraction c = new Fraction();
        c.numerator = b.getNumerator() * a.getNumerator();
        c.denominator = b.getDenominator() * a.getDenominator();
        return c;
    }

    /**
     * This non-static method divides a given Fraction with this one.
     * It should modify this.numerator and this.denominator.
     * @param a Fraction a to divide from the current Fraction.
     */
    public void divide(Fraction a) {
        this.numerator = this.numerator * a.denominator;
        this.denominator = this.denominator * a.numerator;
    }

    /**
     * This static method divides Fraction b from a.
     * Do not use this.numerator or this.denominator.
     * @param a Fraction a to use as the dividend.
     * @param b Fraction b to use as the divisor.
     * @return A new Fraction that is a divided by b.
     */
    public static Fraction divide(Fraction a, Fraction b) {
        Fraction c = new Fraction();
        c.numerator = a.getNumerator() * b.getDenominator();
        c.denominator = a.getDenominator() * b.getNumerator();
        return c;
    }

    /**
     * This non-static method finds the greatest common factor
     * between this.numerator and this.denominator.
     * @return The GCF between this.numerator and this.denominator.
     */
    public int gcf() {
        List<Integer> nList = new ArrayList<Integer>();
        List<Integer> dList = new ArrayList<Integer>();
        for(int i = 1; i < this.numerator; i++){
            if(this.numerator%i == 0){
                nList.add(i);
            }
        }
        for(int i = 1; i < this.denominator; i++){
            if(this.denominator%i == 0){
                dList.add(i);
            }
        }
        int cryAbtIt = 0;
        for(int i = nList.size()-1; i >= 0; i--){
            for(int j = dList.size()-1; j >= 0; j--){
                if(nList.get(i) == dList.get(j)){
                    return nList.get(i);
                }
            }
        }
        return 1;
    }

    /**
     * This static method finds the greatest common factor
     * between a given Fraction's numerator and denominator.
     * Do not use this.numerator or this.denominator.
     * @param a A Fraction to find the GCF of.
     * @return The GCF between the Fraction's numerator and denominator.
     */
    public static int gcf(Fraction a) {
        List<Integer> nList = new ArrayList<Integer>();
        List<Integer> dList = new ArrayList<Integer>();
        for(int i = 1; i < a.getNumerator(); i++){
            if(a.getNumerator()%i == 0){
                nList.add(i);
            }
        }
        for(int i = 1; i < a.getDenominator(); i++){
            if(a.getDenominator()%i == 0){
                dList.add(i);
            }
        }
        int cryAbtIt = 0;
        for(int i = nList.size()-1; i >= 0; i--){
            for(int j = dList.size()-1; j >= 0; j--){
                if(nList.get(i) == dList.get(j)){
                    return nList.get(i);
                }
            }
        }
        return 1;
    }

    /**
     * This non-static method reduces this current Fraction.
     * It should call the non-static gcf method and then divide
     * both this.numerator and this.denominator by their gcf.
     */
    public void reduce() {
        int gcf = this.gcf();
        this.numerator /= gcf;
        this.denominator /= gcf;
    }

    /**
     * This static method reduces a given Fraction.
     * It should call the static gcf method and then use
     * a.setNumerator(__) and a.setDenominator(__).
     * @param a A Fraction to reduce.
     */
    public static void reduce(Fraction a) {
        int gcf = a.gcf();
        a.numerator /= gcf;
        a.denominator /= gcf;
    }

    /**
     * Reduce two diadic (n / 2^k) fractions such that they have the same denomenator.
     * @param a A Fraction to reduce.
     */
    public static void reduceWithDiadic(Fraction a, Fraction b) {
        a.reduce();
        b.reduce();
        int adenom = a.denominator;
        int bdenom = b.denominator;

        //System.out.println("REDUCING "+a+" & "+b+" DIADICALLY");

        int comp = ((Integer)bdenom).compareTo(((Integer)adenom));
        if (comp == 0) return;
        if (comp > 0) {
            Fraction xchg_f = a;
            int xchg_i = adenom;
            a = b;
            b = xchg_f;
            adenom = bdenom;
            bdenom = xchg_i;
        }

        int fac = 0;
        while (bdenom < adenom) {
            bdenom <<= 1;
            fac++;
        }

        b.numerator <<= fac;
        b.denominator <<= fac;

        //System.out.println("REDUCING "+a+" & "+b+" DIADICALLY" + fac + " "+bdenom);
    }
}
