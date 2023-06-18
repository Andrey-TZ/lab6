package classes.model;

import java.io.Serializable;

/**
 * Enum study group form of education
 */
public enum FormOfEducation implements Serializable {
    DISTANCE_EDUCATION("дистанционное обучение", "DISTANCE_EDUCATION", 1),
    FULL_TIME_EDUCATION("дневное очное обучение", "FULL_TIME_EDUCATION", 2),
    EVENING_CLASSES("вечернее обучение", "EVENING_CLASSES", 3);

    private final String meaning;
    private final String value;
    private final int id;

    FormOfEducation(String meaning, String value, int id) {
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
