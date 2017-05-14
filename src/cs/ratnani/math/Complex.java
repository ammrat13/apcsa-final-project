package cs.ratnani.math;

import java.awt.*;


/**
 * This class represents a complex number. It has a constructor for the re-im
 * representation and has methods to access this number's real part, imaginary
 * part, complex modulus (r), and complex argument (theta). It also has a
 * method to compute the color of this number.
 *
 * @author Ammar Ratnani
 * @version 0.0
 */
public class Complex {

    // Instance Variables: -----------------------------------------------------

    // We store in re-im format
    private final double re;
    private final double im;


    // Constructors: -----------------------------------------------------------

    /**
     * Constructor that takes in complex numbers in re-im format.
     *
     * @param a The real part of the new number
     * @param b The imaginary part of the new number
     */
    public Complex(double a, double b){
        re = a;
        im = b;
    }

    /**
     * Constructor that takes in real numbers and makes them complex (imaginary
     * part is 0)
     *
     * @param a The real part of the new number
     */
    public Complex(double a){
        re = a;
        im = 0.0;
    }

    /**
     * Default constructor returns 0+0i
     */
    public Complex(){
        re = 0.0;
        im = 0.0;
    }


    // Getters/Setters: --------------------------------------------------------

    /** @return The real part of this number */
    public double getRe(){ return re; }

    /** @return The imaginary part of this number */
    public double getIm(){ return im; }


    // Public Methods: ---------------------------------------------------------

    /** @return The string representation of this number in re-im format to
     *          three decimal places
     */
    public String toString(){
        if(im >= 0)
            return String.format("%.3f + %.3fi", re, im);
        else
            // Change it to a minus sign if `im` is negative
            return String.format("%.3f - %.3fi", re, -1*im);
    }

    /** @return The complex modulus (absolute value) of this number */
    public double getAbs(){ return Math.sqrt(re*re + im*im); }

    /** @return The complex argument (theta) of this number in radians */
    public double getArg(){ return Math.atan2(im, re); }

    /**
     * Returns the color of this number as specified on RapidTables:
     * <a href="http://www.rapidtables.com/convert/color/hsl-to-rgb.htm">
     *     http://www.rapidtables.com/convert/color/hsl-to-rgb.htm
     * </a>
     *
     * @return The color of this number
     */
    public Color getColor(){
        // HLS value as specified on Wikipeida:
        //  https://en.wikipedia.org/wiki/Color_wheel_graphs_of_complex_functions
        double H = (Math.PI + getArg()) % (2*Math.PI);
            // We do mod 2Pi so it is never over it
        double L = (1 - Math.pow(2.0, -1*getAbs())) * .6;
        double S = 1.0;

        // Convert to RGB as specified on RapidTables:
        //  http://www.rapidtables.com/convert/color/hsl-to-rgb.htm
        // As far as I know, `Color` does not take HLS, so we have to convert
        //  manually
        double C = (1 - Math.abs(2*L - 1)) * S;
        double Hp = H / (Math.PI/3);
        double X = C * (1 - Math.abs(Hp%2 - 1));
        double R; double G; double B;

        switch((int) H){
            case 0:
                R = C;
                G = X;
                B = 0.0;
                break;
            case 1:
                R = X;
                G = C;
                B = 0.0;
                break;
            case 2:
                R = 0.0;
                G = C;
                B = X;
                break;
            case 3:
                R = 0.0;
                G = X;
                B = C;
                break;
            case 4:
                R = X;
                G = 0.0;
                B = C;
                break;
            default:
                R = C;
                G = 0.0;
                B = X;
        }

        double m = L - .5*C;
        R += m;
        G += m;
        B += m;

        // R, G, and B are between 0 and 1, so we have to multiply by 255
        return new Color((int) (255*R), (int) (255*G), (int) (255*B));
    }

    /**
     * This method takes in a string and returns a complex number the string
     * represents, or throws a number format exception if it is invalid.
     *
     * @throws NumberFormatException When the string does not represent a
     *                               complex number
     * @param s The string representing a complex number.
     * @return The complex number represented by `s`.
     */
    public static Complex parseComplex(String s){
        // If it is a real number: matches a number, followed by an optional
        //  point, followed by an optional decimal part
        if(s.matches("[0-9]+\\.?[0-9]*")){
            // If it matches, we can parse to a double
            return new Complex(Double.parseDouble(s),0);
        }

        // If it is an imaginary number: matches a number, followed by an
        //  optional decimal point, followed by an optional decimal part,
        //  followed by "i" or "j"
        if(s.matches("[0-9]+\\.?[0-9]*(i|j)")){
            // The last part can be converted to a double, so strip the last
            //  character
            return new Complex(0, Double.parseDouble(s.substring(0,s.length()-1)));
        }

        // If it is a complex number: matches a real number, a plus, another
        //  real number, then "i" or "j"
        if(s.matches("[0-9]+\\.?[0-9]*\\+[0-9]+\\.?[0-9]*(i|j)")){
            // Get the real and imaginary parts
            String real = s.split("\\+")[0];
            String imag = s.split("\\+")[1];
            return new Complex(
                    Double.parseDouble(real),
                    Double.parseDouble(imag.substring(0,imag.length()-1))
            );
        }

        // We didn't find anything
        throw new NumberFormatException("Failed to parse: " + s);
    }
}