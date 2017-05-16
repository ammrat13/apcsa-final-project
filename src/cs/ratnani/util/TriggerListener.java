package cs.ratnani.util;

/**
 * This interface allows some class to be put into a `TriggerList` for when
 * that trigger fires.
 *
 * @see TriggerList
 * @author Ammar Ratnani
 * @version 2017.05.16
 */
public interface TriggerListener {
    void onTrigger();
}
