package cs.ratnani.math;

import java.awt.image.BufferedImage;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * This class contains static functions on complex numbers, such as add,
 * multiply, exponent, etc.
 *
 * @see Complex
 * @author Ammar Ratnani
 * @version 2017.05.16
 */
public class ComplexMath {

    public static final Complex ZERO = new Complex();
    public static final Complex ONE = new Complex(1);
    public static final Complex I = new Complex(0,1);

    /**
     * @param a A summand
     * @param b A summand
     * @return a+b
     */
    public static Complex add(Complex a, Complex b){
        return new Complex(
                a.getRe() + b.getRe(),
                a.getIm() + b.getIm()
        );
    }

    /**
     * @param a The minuend
     * @param b The subtrahend
     * @return a-b
     */
    public static Complex sub(Complex a, Complex b){
        return new Complex(
                a.getRe() - b.getRe(),
                a.getIm() - b.getIm()
        );
    }

    /**
     * @param a A multiplicand
     * @param b A multiplicand
     * @return a*b
     */
    public static Complex mul(Complex a, Complex b){
        // (a+bi)(c+di)
        // ac + cbi + adi - bd
        // (ac - bd) + (bc + ad)i
        return new Complex(
                a.getRe()*b.getRe() - a.getIm()*b.getIm(),
                a.getIm()*b.getRe() + a.getRe()*b.getIm()
        );
    }

    /**
     * @param a The dividend
     * @param b The divisor
     * @return a/b
     */
    public static Complex div(Complex a, Complex b){
        // a / b == a * (1 / b)
        return mul(a, inv(b));
    }

    /**
     * @param z A complex number
     * @return -z
     */
    public static Complex negate(Complex z){
        return new Complex(-1*z.getRe(), -1*z.getIm());
    }

    /**
     * @param z A complex number
     * @return Complex conjugate of z
     */
    public static Complex conj(Complex z){
        // conj(a + bi) == a - bi
        return new Complex(
                z.getRe(),
                -1*z.getIm()
        );
    }

    /**
     * @param z A complex number
     * @return Multiplicative inverse of z
     */
    public static Complex inv(Complex z){
        // 1 / (a+bi)
        // (1 / (a+bi)) * ((a-bi)/(a-bi))
        // (a-bi) / (a^2 + b^2)
        // (a / (a^2 + b^2)) - (b / (a^2 + b^2))i
        double denom = z.getRe()*z.getRe() + z.getIm()*z.getIm();
        return new Complex(
                z.getRe()/denom,
                -1 * (z.getIm()/denom)
        );
    }

    /**
     * @param z A complex number
     * @return exp(z)
     */
    public static Complex exp(Complex z){
        // exp(a + bi)
        // exp(a) * exp(bi)
        // exp(a) * (cos(b) + i*sin(b))
        // exp(a)*cos(b) + (exp(a)*sin(b))i
        return new Complex(
                Math.exp(z.getRe()) * Math.cos(z.getIm()),
                Math.exp(z.getRe()) * Math.sin(z.getIm())
        );
    }

    /**
     * @param z A complex number
     * @return ln(z)
     */
    public static Complex ln(Complex z){
        // a+bi = r*exp(i*t)
        // ln(a+bi) = ln(r) + i*t
        return new Complex(
                Math.log(z.getAbs()),
                z.getArg()
        );
    }

    /**
     * @param z A complex number
     * @return sin(z)
     */
    public static Complex sin(Complex z){
        //           exp(iz) - exp(-iz)
        // sin(z) = --------------------
        //                   2i
        return div(
                sub( exp(mul(I,z)), exp(negate(mul(I,z))) ),
                mul(new Complex(2), I)
        );
    }

    /**
     * @param z A complex number
     * @return cos(z)
     */
    public static Complex cos(Complex z){
        //           exp(iz) + exp(-iz)
        // cos(z) = --------------------
        //                   2
        return div(
                add( exp(mul(I,z)), exp(negate(mul(I,z))) ),
                new Complex(2)
        );
    }

    /**
     * Returns the principal value of a^b. In the complex, the results can be
     * multi-valued (except for integers), each separated by 2*pi*k on either
     * the real or the imaginary axis for any integer k. The principal value is
     * the one where k=0.
     *
     * @param a A complex number
     * @param b A complex number
     * @return The principal value of a^b.
     */
    public static Complex pow(Complex a, Complex b){
        // a^b
        // exp(ln(a))^b
        // exp(ln(a) * b) <-- Principal value only
        return exp( mul(ln(a), b) );
    }

    /**
     * This method will take a string in postfix notation and compute it for
     * the complex number supplied.
     *
     * @throws IllegalArgumentException When the string is not valid postfix
     * @param s The operations in postfix. Use `z` to represent the complex
     *          number supplied
     * @param z The complex number to use in `s`
     * @return The value of `s` at `z`
     */
    public static Complex parsePostfix(String s, Complex z){
        // Do it as described on Wikipeida:
        //  https://en.wikipedia.org/wiki/Reverse_Polish_notation#Postfix_algorithm

        Stack<Complex> vals = new Stack<>();

        String[] tokens = s.split(" ");
        for(String t : tokens){
            // If the next token is a complex number
            try{
                vals.push(Complex.parseComplex(t));
            } catch(NumberFormatException e){
                // It is an operator or "z"
                try{
                    // Try every token
                    // Remember: pops get the arguments in reverse order
                    // We have to use braces for scoping
                    switch(t){
                        case "z": {
                            vals.push(z);
                            break;
                        }
                        case "+": {
                            Complex arg2 = vals.pop();
                            Complex arg1 = vals.pop();
                            vals.push(add(arg1, arg2));
                            break;
                        }
                        case "-": {
                            Complex arg2 = vals.pop();
                            Complex arg1 = vals.pop();
                            vals.push(sub(arg1, arg2));
                            break;
                        }
                        case "*": {
                            Complex arg2 = vals.pop();
                            Complex arg1 = vals.pop();
                            vals.push(mul(arg1,arg2));
                            break;
                        }
                        case "/": {
                            Complex arg2 = vals.pop();
                            Complex arg1 = vals.pop();
                            vals.push(div(arg1, arg2));
                            break;
                        }
                        case "^": {
                            Complex arg2 = vals.pop();
                            Complex arg1 = vals.pop();
                            vals.push(pow(arg1, arg2));
                            break;
                        }
                        case "conj": {
                            vals.push(conj(vals.pop()));
                            break;
                        }
                        case "inv": {
                            vals.push(inv(vals.pop()));
                            break;
                        }
                        case "exp": {
                            vals.push(exp(vals.pop()));
                            break;
                        }
                        case "ln": {
                            vals.push(ln(vals.pop()));
                            break;
                        }
                        case "sin": {
                            vals.push(sin(vals.pop()));
                            break;
                        }
                        case "cos": {
                            vals.push(cos(vals.pop()));
                            break;
                        }
                        case "abs": {
                            vals.push(new Complex(vals.pop().getAbs()));
                            break;
                        }
                        case "arg": {
                            vals.push(new Complex(vals.pop().getArg()));
                            break;
                        }
                        case "re": {
                            vals.push(new Complex(vals.pop().getRe()));
                            break;
                        }
                        case "im": {
                            vals.push(new Complex(vals.pop().getIm()));
                            break;
                        }
                        default: {
                            throw new IllegalArgumentException("Token '" + t + "' not defined");
                        }
                    }
                } catch(EmptyStackException f){
                    // The string is not formatted correctly
                    throw new IllegalArgumentException("Not valid postfix string");
                }
            }
        }

        if(vals.size() == 1){
            return vals.pop();
        } else {
            // The string is not formatted correctly
            throw new IllegalArgumentException("Not valid postfix string");
        }
    }

    /**
     * This method will take a postfix function and a specified origin and spit
     * out a plot of the function as a w-by-h image
     *
     * @param s The function in postfix notation
     * @param reUp The upper bound on the real axis
     * @param reDo The lower bound on the real axis
     * @param imUp The upper bound on the imaginary axis
     * @param imDo The lower bound on the imaginary axis
     * @param w The width of the image produced
     * @param h The height of the image produced
     * @return The plot of the function
     */
    public static BufferedImage plot(String s, double reUp, double reDo, double imUp, double imDo, int w, int h){
        BufferedImage ret = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        // For each pixel
        for(int r = 0; r < h; r++){
            for(int c = 0; c < w; c++){
                // Calculate the complex number associated with that pixel
                Complex in = new Complex(
                        numAtC(c,w,reUp,reDo),
                        numAtR(r,h,imUp,imDo)
                );
                // Set the color to the function's value at that point
                ret.setRGB(c, r, parsePostfix(s, in).getColor().getRGB());
            }
        }

        return ret;
    }

    /**
     * This function will, given a column and the range of the plot, output the
     * real part of the complex number at that column
     *
     * @param c The column to compute for
     * @param w The width of the plot
     * @param reUp The upper bound on the real axis
     * @param reDo The lower bound on the real axis
     * @return The real part of the numbers at column `c`
     */
    public static double numAtC(int c, int w, double reUp, double reDo){
        return (((reUp-reDo) * c) / w) + reDo;
    }

    /**
     * This function will, given a row and the range of the plot, output the
     * imaginary part of the complex number at that row
     *
     * @param r The row to compute for
     * @param h The height of the plot
     * @param imUp The upper bound on the imaginary axis
     * @param imDo The lower bound on the imaginary axis
     * @return The imaginary part of the numbers at row `r`
     */
    public static double numAtR(int r, int h, double imUp, double imDo){
        return (((imDo-imUp) * r) / h) + imUp;
    }

    /**
     * This function will, given a complex number and the plot range, output
     * the column the number is on
     *
     * @param z The complex number to compute the row for
     * @param w The width of the plot
     * @param reUp The upper bound on the real axis
     * @param reDo The lower bound on the real axis
     * @return The column on which `z` is
     */
    public static int colOf(Complex z, int w, double reUp, double reDo){
        // Inverse of `numAtC()`
        return (int)( w * (z.getRe() - reDo) / (reUp - reDo) );
    }

    /**
     * This function will, given a complex number and the plot range, output
     * the row the number is on
     *
     * @param z The complex number to compute the row for
     * @param h The height of the plot
     * @param imUp The upper bound on the imaginary axis
     * @param imDo The lower bound on the imaginary axis
     * @return The row on which `z` is
     */
    public static int rowOf(Complex z, int h, double imUp, double imDo){
        // Inverse of `numAtR()`
        return (int)( h * (z.getIm() - imUp) / (imDo - imUp) );
    }

}
