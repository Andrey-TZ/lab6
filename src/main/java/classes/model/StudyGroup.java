package classes.model;

import exceptions.EmptyFieldException;
import exceptions.WrongFieldException;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * Study group class. Main collection class
 */
public class StudyGroup implements Comparable<StudyGroup>, Serializable {
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    public static int idSetter = 1;
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private long studentsCount; //Значение поля должно быть больше 0
    private FormOfEducation formOfEducation; //Поле не может быть null
    private Semester semesterEnum = null; //Поле может быть null
    private Person groupAdmin = null; //Поле может быть null
    private String user;

    /**
     *
     * @param name name of the group
     * @param coordinates coordinates of the group
     * @param studentsCount counts of students in group
     * @param formOfEducation form of education in group
     * @param semesterEnum semester of group
     * @param groupAdmin group admin
     * @throws WrongFieldException if given param takes an invalid value
     * @throws EmptyFieldException if given param is null and its invalid
     */

    public StudyGroup(int id, String name, Coordinates coordinates, LocalDateTime creationDate, long studentsCount, FormOfEducation formOfEducation, Semester semesterEnum, Person groupAdmin, String user) throws WrongFieldException, EmptyFieldException {
        this.id = id;
        setName(name);
        setCoordinates(coordinates);
        this.creationDate = creationDate;
        setStudentsCount(studentsCount);
        setFormOfEducation(formOfEducation);
        setSemesterEnum(semesterEnum);
        setGroupAdmin(groupAdmin);
        this.user = user;

    }

    public StudyGroup(){
        this.id = hashCode();
        this.creationDate = LocalDateTime.now();
    }




    public StudyGroup(int id, String name, Coordinates coordinates, LocalDateTime creationDate, long studentsCount, FormOfEducation formOfEducation, Semester semesterEnum, Person groupAdmin) throws WrongFieldException, EmptyFieldException {
        idSetter = id;
        idSetter ++;
        setId(id);
        setName(name);
        setCoordinates(coordinates);
        this.creationDate = creationDate;
        setStudentsCount(studentsCount);
        setFormOfEducation(formOfEducation);
        setSemesterEnum(semesterEnum);
        setGroupAdmin(groupAdmin);
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws EmptyFieldException {
        if (name == null || name.isEmpty()) {
            throw new EmptyFieldException("Имя не может быть пустой строкой");
        } else {
            this.name = name;
        }
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) throws NullPointerException {
        if (coordinates == null) throw new NullPointerException("Координаты не могут быть пустыми");
        else this.coordinates = coordinates;
    }



    public long getStudentsCount() {
        return studentsCount;
    }

    public void setStudentsCount(long studentsCount) throws WrongFieldException {
        if (studentsCount < 1) throw new WrongFieldException("Количество студентов должно быть больше 0 ");
        else this.studentsCount = studentsCount;

    }

    public String getCreationDate(){
        return creationDate.toString();
    }

    public FormOfEducation getFormOfEducation() {
        return formOfEducation;
    }

    public void setFormOfEducation(FormOfEducation formOfEducation) throws EmptyFieldException {
        if (formOfEducation == null) throw new EmptyFieldException("Форма обучения не может быть пустой");
        else this.formOfEducation = formOfEducation;
    }

    public Semester getSemesterEnum() {
        return semesterEnum;
    }

    public void setSemesterEnum(Semester semesterEnum) {
        this.semesterEnum = semesterEnum;
    }

    public Person getGroupAdmin() {
        return groupAdmin;
    }

    public void setGroupAdmin(Person groupAdmin) {
        this.groupAdmin = groupAdmin;
    }

    /**
     * Get study group fields
     * @return String with study group object fields
     */

    @Override
    public String toString() {
        return "Студенческая группа c " +
                "id = " + id + ":" +
                "\nназвание: '" + name + "'" +
                ", координаты: " + coordinates +
                ", дата создания: " + creationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) +
                ", количество студентов: " + studentsCount +
                ", форма обучения: " + formOfEducation +
                ", семестр: " + semesterEnum +
                ", админ группы: " + groupAdmin;
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hash(id, name, coordinates, creationDate, studentsCount, formOfEducation, semesterEnum, groupAdmin);
    }

    @Override
    public int compareTo(StudyGroup obj) {
        return Long.compare(studentsCount, obj.getStudentsCount());
    }


    public void update(StudyGroup obj){
        this.name = obj.getName();
        this.studentsCount = obj.getStudentsCount();
        this.groupAdmin = obj.getGroupAdmin();
        this.coordinates=obj.getCoordinates();
        this.formOfEducation = obj.getFormOfEducation();
        this.semesterEnum = obj.getSemesterEnum();
    }

    public void checkId(){
        if (id >= idSetter){
            idSetter = id;
            idSetter++;
        }
        else{
            this.setId(idSetter++);
        }
    }
}
