package cs.ratnani.util;

/**
 * This interface allows some class to be put into a `TriggerList` for when
 * that trigger fires.
 *
 * @see TriggerList
 * @author Ammar Ratnani
 * @version 0.0
 */
public interface TriggerListener {
    void onTrigger();
}
