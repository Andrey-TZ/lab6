package classes.model;

import java.io.Serializable;

/**
 * Enum study group form of education
 */
public enum FormOfEducation implements Serializable {
    DISTANCE_EDUCATION("дистанционное обучение", "DISTANCE_EDUCATION"),
    FULL_TIME_EDUCATION("дневное очное обучение", "FULL_TIME_EDUCATION"),
    EVENING_CLASSES("вечернее обучение", "EVENING_CLASSES");

    private final String meaning;
    private final String value;

    FormOfEducation(String meaning, String value) {
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
