package cs.ratnani.math;

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

}
