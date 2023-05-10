package classes.model;

import exceptions.WrongFieldException;

import java.io.Serializable;

/**
 * Class of Coordinates of StudyGroup
 * @author Vorotnikov Andrey @Andrey-TZ
 */
public class Coordinates implements Serializable {
    private Float x; //Значение поля должно быть больше -478, Поле не может быть null
    private float y;

    public Coordinates() {
    }

    public Coordinates(Float x, float y) throws WrongFieldException {
        setX(x);
        setY(y);
    }

    public void setX(Float x) throws WrongFieldException {
        if (x == null) throw new WrongFieldException("X не может быть null");
        if (x < -478) throw new WrongFieldException("X должен быть больше -478");
        this.x = x;
    }

    public Float getX() {
        return x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getY() {
        return y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
