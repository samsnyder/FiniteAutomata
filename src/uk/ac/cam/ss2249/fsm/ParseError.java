package uk.ac.cam.ss2249.fsm;

/**
 * <p>An error occurred while parsing the concrete syntax regular expression</p>
 *
 * @author Sam Snyder
 */
public class ParseError extends Exception {
    ParseError(String m){
        super(m);
    }
}
