package uk.ac.cam.ss2249.fsm;

import edu.uci.ics.jung.graph.DirectedOrderedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a non deterministic finite automaton (NFA) with null transitions.
 * This machine is immutable.
 *
 * @author Sam Snyder
 */
public class Machine {
    private Set<State> states;
    private State startState;
    private Set<State> acceptingStates;
    private static final int PLACE_OFFSET = 200;

    /**
     * Creates a machine and initialises sets.
     *
     * All instances are created from local static methods.
     */
    private Machine(){
        states = new HashSet<State>();
        acceptingStates = new HashSet<State>();
    }

    /**
     * Gets the starting state, s.
     *
     * @return start state
     */
    public State getStartState(){
        return startState;
    }

    /**
     * Returns whether a state is an accepting state in this machine
     *
     * @param s the state to test
     * @return whether s is accepting
     */
    public boolean stateIsAccepting(State s){
        return acceptingStates.contains(s);
    }

    /**
     * Tests if a string results in an accepting state (i.e matches the regular expression).
     *
     * @param s string to test
     * @return whether the string matches the regular expression this machine represents
     */
    public boolean matchString(String s){
        return startState.matchString(s, this);
    }

    /**
     * Creates a JUNG graph to represent the machines structure.
     *
     * http://jung.sourceforge.net
     *
     * @return a JUNG graph
     */
    public Graph<State, Transition> createGraph(){
        Graph<State, Transition> g = new DirectedOrderedSparseMultigraph<State, Transition> ();
        startState.addToGraph(g);
        return g;
    }

    /**
     * Parses a regular expression concrete syntax string.
     *
     * "a|b" is equivalent to Union(Ma, Mb)
     * "ab" is equivalent to Concat(Ma, Mb)
     * "a*" is equivalent to Star(Ma)
     *
     * @param r the concrete syntax regular expression
     * @return the machine representing the given regular expression
     * @throws ParseError an error occured when parsing the regular expression
     */
    public static Machine parseRegex(String r) throws ParseError {
        return machineFromSubstring(r);
    }

    private static Machine machineFromSubstring(String r) throws ParseError{
        if(r.length() == 1){
            return Machine.letterAxiom(new Letter(r.charAt(0)));
        }

        // eliminate brackets
        int bracketDepth = 0;
        StringBuilder currentRootBracket = new StringBuilder();
        List<String> rootBrackets = new ArrayList<String>();
        StringBuilder noBracketsBuilder = new StringBuilder();
        for(char c : r.toCharArray()){
            if(c == '('){
                bracketDepth++;
                if(bracketDepth == 1)
                    continue;
            }else if(c == ')'){
                bracketDepth--;
                if(bracketDepth == 0){
                    noBracketsBuilder.append((char) (PLACE_OFFSET + rootBrackets.size()));
                    rootBrackets.add(currentRootBracket.toString());
                    currentRootBracket = new StringBuilder();
                    continue;
                }
            }
            if(bracketDepth > 0) {
                currentRootBracket.append(c);
            }else{
                noBracketsBuilder.append(c);
            }
        }
        if(bracketDepth != 0){
            throw new ParseError("Brackets error");
        }
        String noBrackets = noBracketsBuilder.toString();

        String[] unionParts = noBrackets.split("\\|");
        if(unionParts.length > 1){
            Set<Machine> unionMachines = new HashSet<Machine>();
            for(String part : unionParts){
                String partFull = getFullString(part, rootBrackets);
                Machine m = machineFromSubstring(partFull);
                unionMachines.add(m);
            }
            return Machine.union(unionMachines);
        }else{
            List<Machine> concatMachines = new ArrayList<Machine>();

            for(int i=0; i<noBrackets.length(); i++){
                String fullString = getFullString(noBrackets.charAt(i), rootBrackets);
                Machine m = machineFromSubstring(fullString);
                if(i < noBrackets.length()-1 && noBrackets.charAt(i + 1) == '*'){
                    m = Machine.star(m);
                    i++;
                }
                concatMachines.add(m);
            }
            return Machine.concat(concatMachines);
        }
    }

    private static String getFullString(char c, List<String> brackets){
        if(c >= PLACE_OFFSET){
            return brackets.get(c - PLACE_OFFSET);
        }else{
            return String.valueOf(c);
        }
    }

    private static String getFullString(String p, List<String> brackets){
        StringBuilder r = new StringBuilder();
        for(char c : p.toCharArray()){
            if(c >= PLACE_OFFSET){
                r.append("(");
                r.append(brackets.get(c - PLACE_OFFSET));
                r.append(")");
            }else{
                r.append(c);
            }
        }
        return r.toString();
    }

    /**
     * Returns the machine representing the union of the given set of machines (unordered).
     *
     * This machine will result in an accepting state if any one of the given machines
     * results in an accepting state.
     *
     * @param machines machines to union
     * @return a new machine representing the union of these machines
     */
    public static Machine union(Set<Machine> machines){
        Machine r = new Machine();
        State q0 = new State();
        Set<State> totalStates = new HashSet<State>();
        Set<State> newAcceptingStates = new HashSet<State>();
        totalStates.add(q0);
        for(Machine machine : machines){
            q0.addTransitionTo(machine.startState, Letter.nullString());
            totalStates.addAll(machine.states);
            newAcceptingStates.addAll(machine.acceptingStates);
        }
        r.states = totalStates;
        r.acceptingStates = newAcceptingStates;
        r.startState = q0;
        return r;
    }

    /**
     * Returns the machine representing the concatenation of the given list of machines (ordered).
     *
     * This machine will result in an accepting state if all of the given machines result in
     * an accepting state.
     *
     * @param machines machines to concatenate
     * @return a new machine representing the concatenation of these machines
     */
    public static Machine concat(List<Machine> machines){
        Machine r = new Machine();
        r.acceptingStates.addAll(machines.get(machines.size()-1).acceptingStates);
        r.startState = machines.get(0).startState;
        for(Machine m : machines){
            r.states.addAll(m.states);
        }
        for(int i=0; i<machines.size()-1; i++){
            State nextStart = machines.get(i+1).startState;
            for(State acceptingState : machines.get(i).acceptingStates){
                acceptingState.addTransitionTo(nextStart, Letter.nullString());
            }
        }
        return r;
    }

    /**
     * Returns the machine representing the star operation of a given machine.
     *
     * This machine will result in an accepting state if the given machine results in
     * an accepting state n >= 0 times.
     *
     * This means the null string as well as any number of strings matching the
     * original machine will result in an accepting state in the new machine.
     *
     * @param m the machine to star
     * @return the machine representing the star operation of the given machine
     */
    public static Machine star(Machine m){
        Machine r = new Machine();
        State q0 = new State();
        q0.addTransitionTo(m.startState, Letter.nullString());
        for(State acceptingState : m.acceptingStates){
            acceptingState.addTransitionTo(q0, Letter.nullString());
        }
        r.states.add(q0);
        r.states.addAll(m.states);
        r.acceptingStates.add(q0);
        r.startState = q0;
        return r;
    }

    /**
     * Returns the machine which will result in an accepting state when a letter l
     * is applied to it.
     *
     * @param l the letter to represent
     * @return the machine representing the letter
     */
    public static Machine letterAxiom(Letter l){
        Machine r = new Machine();
        State q0 = new State();
        State q1 = new State();
        r.states.add(q0);
        r.states.add(q1);
        q0.addTransitionTo(q1, l);
        r.startState = q0;
        r.acceptingStates.add(q1);
        return r;
    }
}
