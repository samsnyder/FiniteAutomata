package uk.ac.cam.ss2249.fsm;

/**
 * An error occurred while parsing the concrete syntax regular expression
 *
 * @author Sam Snyder
 */
public class ParseError extends Exception {
    ParseError(String m){
        super(m);
    }
}
