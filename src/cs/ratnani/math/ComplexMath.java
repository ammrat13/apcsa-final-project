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
 * @version 0.0
 */
public class ComplexMath {

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
     * @param b The subractend
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
     * @return Complex conjugate of z
     */
    public static Complex conj(Complex z){
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
                Math.exp(z.getRe())*Math.cos(z.getIm()),
                Math.exp(z.getRe())*Math.sin(z.getIm())
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
     * This method will take a string in postfix notation and compute it for
     * the complex number supplied.
     *
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
     * @param s The function
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
                        (((reUp-reDo) * c) / (double)w) + reDo,
                        (((imDo-imUp) * r) / (double)h) + imUp
                );
                // Set the color to the function's value at that point
                ret.setRGB(c, r, parsePostfix(s, in).getColor().getRGB());
            }
        }

        return ret;
    }

}
