package classes.model;

import java.io.Serializable;

/**
 * Enum for available semesters for study group
 */
public enum Semester implements Serializable {
    SECOND("второй", "SECOND", 1),
    FOURTH("четвёртый", "FOURTH", 2),
    FIFTH("пятый", "FIFTH", 3),
    SIXTH("шестой", "SIXTH", 4);


    private final String meaning;
    private final String value;
    private final int id;


    Semester(String meaning, String value, int id) {
        this.meaning = meaning;
        this.value = value;
        this.id = id;
    }

    public int getId() {
        return id;
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
