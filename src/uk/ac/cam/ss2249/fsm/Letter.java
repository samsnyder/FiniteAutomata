package uk.ac.cam.ss2249.fsm;

/**
 * <p>Represents a letter in the alphabet.</p>
 *
 * <p>If value is equal to NULL_CHAR, it represents the null string.</p>
 */
public class Letter {
    private char value;
    private static final char NULL_CHAR = 199;

    /**
     * <p>Creates a letter with a character</p>
     *
     * @param v the character
     */
    protected Letter(char v){
        value = v;
    }

    /**
     * <p>Tests if the letter is equal to a character</p>
     *
     * @param c the character to test equality for
     * @return the result
     */
    public boolean equals(char c){
        return value == c;
    }

    /**
     * <p>Tests if the letter represents a null string</p>
     *
     * @return whether this letter represents a null string
     */
    public boolean isNullString(){
        return value == 199;
    }

    /**
     * <p>Returns a letter representing the null string</p>
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
