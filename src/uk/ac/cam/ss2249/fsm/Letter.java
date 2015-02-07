package uk.ac.cam.ss2249.fsm;

/**
 * Represents a letter in the alphabet.
 *
 * If value is equal to NULL_CHAR, it represents the null string.
 */
public class Letter {
    private char value;
    private static final char NULL_CHAR = 199;

    /**
     * Creates a letter with a character
     * @param v the character
     */
    protected Letter(char v){
        value = v;
    }

    /**
     * Tests if the letter is equal to a character
     *
     * @param c the character to test equality for
     * @return the result
     */
    public boolean equals(char c){
        return value == c;
    }

    /**
     * Tests if the letter represents a null string
     *
     * @return whether this letter represents a null string
     */
    public boolean isNullString(){
        return value == 199;
    }

    /**
     * Returns a letter representing the null string
     *
     * @return the letter
     */
    public static Letter nullString(){
        return new Letter(NULL_CHAR);
    }

    @Override
    public String toString(){
        if(isNullString())
            return "null";
        else
            return String.valueOf(value);
    }
}
