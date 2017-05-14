package cs.ratnani.util;

/**
 * This class represents a Tuple, an immutable ordered collection of two
 * objects. It also provides methods to access each object.
 *
 * @author Ammar Ratnani
 * @version 0.0
 */
public class Tuple<T1,T2> {

    // Private Variables: ------------------------------------------------------

    private final T1 fst;
    private final T2 snd;


    // Constructors: -----------------------------------------------------------

    public Tuple(T1 f, T2 s){
        fst = f;
        snd = s;
    }


    // Getters/Setters: --------------------------------------------------------

    public T1 fst() { return fst; }

    public T2 snd() { return snd; }

}
