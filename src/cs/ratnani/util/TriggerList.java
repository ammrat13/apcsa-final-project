package cs.ratnani.util;

import java.util.ArrayList;


/**
 * This class maintains a list of objects that implement the `TriggerListener`
 * interface. It also provides a method to send a notification to all those
 * listening when the event happens.
 *
 * @see TriggerListener
 * @author Ammar Ratnani
 * @version 0.0
 */
public class TriggerList {

    // Private Variables: ------------------------------------------------------

    private final ArrayList<TriggerListener> listeners;


    // Constructors: -----------------------------------------------------------

    public TriggerList(){
        listeners = new ArrayList<>();
    }

    public TriggerList(ArrayList<TriggerListener> l){
        listeners = l;
    }


    // Public Methods: ---------------------------------------------------------

    public void add(TriggerListener l){
        listeners.add(l);
    }

    public void trigger(){
        // Notify each one
        for(TriggerListener e : listeners){
            e.onTrigger();
        }
    }

}
