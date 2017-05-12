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
     * <a href="https://en.wikipedia.org/wiki/Color_wheel_graphs_of_complex_functions">
     *     https://en.wikipedia.org/wiki/Color_wheel_graphs_of_complex_functions
     * </a>
     *
     * @return The color of this number
     */
    public Color getColor(){
        // HLS value as specified on Wikipeida:
        //  https://en.wikipedia.org/wiki/Color_wheel_graphs_of_complex_functions
        double H = (Math.PI + getArg()) % (2*Math.PI);
            // We do mod 2Pi so it is never over it
        double L = (1 - Math.pow(2.0, -1*getAbs()));
        double S = 1.0;

        // Convert to RGB as specified on Wikipedia:
        //  https://en.wikipedia.org/wiki/HSL_and_HSV#Converting_to_RGB
        double C = (1 - Math.abs(2*L - 1)) * S;
        double Hp = H / (Math.PI/3);
        double X = C * (1 - Math.abs(Hp%2 - 1));
        double R; double G; double B;

        switch((int) Hp){
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

        double m = L - .05*C;
        R += m;
        G += m;
        B += m;

        return new Color((int) R, (int) G, (int) B);
    }
}
