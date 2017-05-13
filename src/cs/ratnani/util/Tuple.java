package cs.ratnani.util;

/**
 * This class represents a Tuple, an ordered collection of two objects. It also
 * provides methods to access and set each object.
 */
public class Tuple<T1,T2> {

    // Private Variables: ------------------------------------------------------

    T1 fst;
    T2 snd;


    // Constructors: -----------------------------------------------------------

    public Tuple(T1 f, T2 s){
        fst = f;
        snd = s;
    }


    // Getters/Setters: --------------------------------------------------------

    public T1 fst() { return fst; }
    public void setFst(T1 f) { fst = f; }

    public T2 snd() { return snd; }
    public void setSnd(T2 s) { snd = s; }

}
