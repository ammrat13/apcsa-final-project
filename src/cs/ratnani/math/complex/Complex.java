package cs.ratnani.math.complex;

import java.awt.Color;


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
    private double re;
    private double im;


    // Constructors: -----------------------------------------------------------

    /**
     * Constructor that takes in complex numbers in re-im format.
     *
     * @param a The real part of the new number
     * @param b The imaginary part of the new number
     */
    public Complex(double a, double b){
        setRe(a);
        setRe(b);
    }


    // Getters/Setters: --------------------------------------------------------

    /** @return The real part of this number */
    public double getRe(){ return re; }

    /** @param a The new real part of this number */
    public void setRe(double a){ re = a; }

    /** @return The imaginary part of this number */
    public double getIm(){ return im; }

    /** @param b The new imaginary part of this number */
    public void setIm(double b){ im = b; }


    // Public Methods: ---------------------------------------------------------

    /** @return The complex modulus (absolute value) of this number */
    public double getAbs(){
        return Math.sqrt(re*re + im*im);
    }

    /** @param r The new complex modulus (absolute value) */
    public void setAbs(double r){
        // Scale both `re` and `im` proportionally to the new radius `r`
        double oldR = getAbs();
        re = (r/oldR) * re;
        im = (r/oldR) * im;
    }

    /** @return The complex argument (theta) of this number in radians */
    public double getArg(){
        return Math.atan2(im, re);
    }

    /** @param t The new complex argument (theta) in radians */
    public void setArg(double t){
        double oldR = getAbs();
        double oldT = getArg();
        re = oldR * Math.cos(oldT + t);
        im = oldR * Math.sin(oldT + t);
    }

    /**
     * Returns the color of this number as specified on Wikipedia:
     * <a>https://en.wikipedia.org/wiki/Color_wheel_graphs_of_complex_functions</a>
     *
     * @return The color of this number
     */
    public Color getColor(){
        return null;
    }

}
