package classes.model;

import java.io.Serializable;

/**
 * Enum for available semesters for study group
 */
public enum Semester implements Serializable {
    SECOND("второй", "SECOND"),
    FOURTH("четвёртый", "FOURTH"),
    FIFTH("пятый", "FIFTH"),
    SIXTH("шестой", "SIXTH");


    private final String meaning;
    private final String value;

    Semester(String meaning, String value) {
        this.meaning = meaning;
        this.value = value;
    }

    @Override
    public String toString() {
        return meaning;
    }

    /**
     * Method to show element with its value
     *
     * @return string representation with value in brackets
     */

    public String toStringWithValue() {
        return toString() + "(" + value + ")";
    }
}
