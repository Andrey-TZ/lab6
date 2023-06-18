package classes.model;

import exceptions.EmptyFieldException;
import exceptions.WrongFieldException;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Person Class. Used to save Admin group
 */
public class Person implements Comparable<Person>, Serializable {
    private int id;
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Date birthday; //Поле не может быть null
    private Float height; //Поле может быть null, Значение поля должно быть больше 0

    public Person() {
    }

    /**
     * @param name     - имя человека
     * @param birthday - день рождения человека
     * @param height   - рост человека
     * @throws EmptyFieldException при передаче пустой строки или null
     * @throws WrongFieldException при передаче неверного аргумента (например, строки вместо числа)
     */

    public Person(int id, String name, Date birthday, Float height) throws EmptyFieldException, WrongFieldException {
        this.id = id;
        setName(name);
        setBirthday(birthday);
        setHeight(height);
    }

    public void setName(String name) throws EmptyFieldException {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        } else throw new EmptyFieldException("Поле \"имя\" не может быть пустым");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setBirthday(Date birthday) throws EmptyFieldException {
        if (birthday != null) {
            this.birthday = birthday;
        } else throw new EmptyFieldException("Поле \"день рождения\" не может быть пустым");
    }

    public String getBirthday() {
        return new SimpleDateFormat("dd-MM-yyyy").format(birthday);
    }

    public Date getBirthdayDate() {
        return birthday;
    }


    public void setHeight(Float height) throws EmptyFieldException, WrongFieldException {
        if (height == null) {
            throw new EmptyFieldException("Поле \"рост\" не может быть пустым");
        } else if (height < 1) {
            throw new WrongFieldException("Рост должен быть больше 0!");
        } else this.height = height;
    }


    public Float getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return name + " родился " + getBirthday() + ", рост - " + height;
    }

    /**
     * Comparison by date of birth
     *
     * @param oth the object to be compared.
     */
    @Override
    public int compareTo(Person oth) {
        int result = birthday.compareTo(oth.getBirthdayDate());
        switch (result) {
            case 1 -> {
                return -1;
            }
            case -1 -> {
                return 1;
            }
            default -> {
                return 0;
            }
        }
    }

    @Override
    public boolean equals(Object oth){
        if(oth == this){
            return true;
        }
        if(oth == null || oth.getClass() != this.getClass()){
            return false;
        }
        Person other = (Person) oth;
        return id == other.id;
    }
}
