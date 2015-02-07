package uk.ac.cam.ss2249.fsm;

import edu.uci.ics.jung.graph.Graph;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>Represents a state in the NFA.</p>
 *
 * @author Sam Snyder
 */
public class State implements Cloneable{
    private Set<Transition> transitions;

    /**
     * <p>Creates a state with initialised empty sets.</p>
     */
    protected State(){
        transitions = new HashSet<Transition>();
    }

    /**
     * <p>Adds all the transitions to the current graph if they are not there
     * already, then recursively does the same to all destinations of
     * the transitions.</p>
     *
     * @param g the graph to add to
     */
    protected void addToGraph(Graph<State, Transition> g){
        for(Transition transition : transitions){
            if(g.containsEdge(transition))
                continue;
            g.addEdge(transition, this, transition.getDestination());
            transition.getDestination().addToGraph(g);
        }
    }

    /**
     * <p>Tests whether the string given can go further from this node.
     * If it can, it recursively tries all the transitions, reducing
     * the length of the string by one.</p>
     *
     * @param s the (possibly partial) string to test
     * @param m machine the state is in
     * @return if the string can reach an accepting state from this node
     */
    protected boolean matchString(String s, Machine m){
        if(m.stateIsAccepting(this) && s.length() == 0){
            return true;
        }
        char c = s.length() == 0 ? 0 : s.charAt(0);
        for(Transition transition : transitions){
            if(transition.getLetter().equals(c)){
                boolean result = transition.getDestination().matchString(s.substring(1), m);
                if(result){
                    return true;
                }
            }
        }
        for(Transition transition : transitions){
            if(transition.getLetter().isNullString()){
                boolean result = transition.getDestination().matchString(s, m);
                if(result){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * <p>Adds a transition from this state to another, with a letter as the edge.</p>
     *
     * @param s the destination state
     * @param l the letter on the edge (could be null string)
     */
    protected void addTransitionTo(State s, Letter l){
        transitions.add(new Transition(l, s));
    }

    @Override
    public State clone() throws CloneNotSupportedException {
        State s = (State) super.clone();
        s.transitions = new HashSet<Transition>(transitions);
        return s;
    }

}
