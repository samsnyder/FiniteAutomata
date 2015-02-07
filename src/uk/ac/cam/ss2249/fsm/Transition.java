package uk.ac.cam.ss2249.fsm;

/**
 * <p>Represents an immutable transition to a state, with the edge as a letter.</p>
 */
public class Transition {
    private Letter letter;
    private State destination;

    /**
     * <p>Creates a transition to a state d with the letter l as the edge letter.</p>
     *
     * @param l the letter for the edge
     * @param d the destination state
     */
    Transition(Letter l, State d){
        letter = l;
        destination = d;
    }

    public Letter getLetter() {
        return letter;
    }

    public State getDestination() {
        return destination;
    }
}
