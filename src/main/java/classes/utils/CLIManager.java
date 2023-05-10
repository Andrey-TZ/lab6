package classes.utils;

import exceptions.EmptyFieldException;
import exceptions.WrongFieldException;
import classes.model.*;

import javax.naming.NoPermissionException;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Helps to get values from terminal.
 */
public class CLIManager {
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Read Strong from terminal.
     *
     * @return CLI read result. Can be empty string.
     */
    private static String requestString() throws NumberFormatException {
        if (!scanner.hasNextLine()) System.exit(0);
        return scanner.nextLine();
    }

    public static Path requestFilePath(){
        Path path;
        while (true)
        {
            try {
                System.out.print("Введите путь к файлу с коллекцией: ");
                path = Paths.get(requestString());
                if (!Files.exists(path)) throw new FileNotFoundException("Файл \"" + path + "\" не найден");
                if (!Files.isReadable(path)) throw new NoPermissionException("Нет получается прочитать " + path );
                if (!Files.isWritable(path)) throw new NoPermissionException("Не получается записать данные в " + path);
                return path;
            } catch (NoPermissionException | FileNotFoundException e) {
                System.out.println( e.getMessage());
            }
        }
    }
    /**
     * Read float from terminal.
     *
     * @return Float number or null, if input is empty.
     * @throws NumberFormatException if input does not float number.
     */
    private static Float requestFloat() throws NumberFormatException {
        if (!scanner.hasNextLine()) System.exit(0);
        String number = scanner.nextLine();
        if (number.length() == 0) return null;
        return Float.parseFloat(number);
    }

    /**
     * Read long from terminal.
     *
     * @return Long number or null, if input is empty.
     * @throws NumberFormatException if input does not long number.
     */
    private static Long requestLong() throws NumberFormatException {
        if (!scanner.hasNextLine()) System.exit(0);
        String number = scanner.nextLine();
        if (number.length() == 0) return null;
        return Long.parseLong(number);
    }

    private static Integer requestInt() throws NumberFormatException {
        int value;
        while (true) {
            if (!scanner.hasNextLine()) System.exit(0);
            String number = scanner.nextLine();
            if (number.length() == 0) return null;
            try {
                value = Integer.parseInt(number);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Требуется число!");
            }
        }
        return value;
    }

    /**
     * Read date from terminal.
     *
     * @return Date in format "dd-MM-yyyy"
     */
    private static Date requestDate() {
        Date date;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);
        while (true) {
            try {
                if (!scanner.hasNextLine()) System.exit(0);
                date = dateFormat.parse(requestString());
                if (date.after(new Date())) {
                    System.out.println("Нельзя ставить дату позже сегодняшней!");
                    System.out.print("Введите дату рождения: ");
                    continue;
                }
                if (date.before( dateFormat.parse("01-01-1900"))){
                    System.out.println("Люди не живут так долго :(");
                    System.out.print("Введите дату рождения: ");
                    continue;
                }
                break;
            } catch (ParseException e) {
                System.out.println("Проверьте корректность данных");
                System.out.print("Попробуйте ещё раз: ");
            }
        }
        return date;
    }

    /**
     * Request Semester from terminal. Method will show all the options. Not case-sensitive.
     *
     * @return Semester enum object or null, if input is empty.
     * @see Semester
     */
    private static Semester requestSemester() {
        for (int i = 1; i < 4; i++) {
            System.out.print(i + " - " + Semester.values()[i - 1] + ", ");
        }
        System.out.println("4 - " + Semester.values()[3]);
        Integer value = requestInt();


        while (true) {
            if(value == null) return null;
            switch (value) {
                case 1 -> {
                    return Semester.values()[0];
                }
                case 2 -> {
                    return Semester.values()[1];
                }
                case 3 -> {
                    return Semester.values()[2];
                }
                case 4 -> {
                    return Semester.values()[3];
                }
                default -> {
                    System.out.println("Введите число в диапазоне от 1 до 4");
                    value = requestInt();
                }
            }
        }
    }

    /**
     * Request FormOfEducation from terminal. Method will show all the options. Not case-sensitive.
     *
     * @return FormOfEducation enum object
     * @throws IllegalArgumentException if input does not match to any option.
     * @see FormOfEducation
     */
    private static FormOfEducation requestFormOfEducation() throws IllegalArgumentException {

        for (int i = 1; i < 4; i++) {
            System.out.print(i + " - " + FormOfEducation.values()[i - 1] + ", ");
        }
        System.out.println();


        while (true) {
            try {
                switch (requestInt()) {
                    case 1 -> {
                        return FormOfEducation.values()[0];
                    }
                    case 2 -> {
                        return FormOfEducation.values()[1];
                    }
                    case 3 -> {
                        return FormOfEducation.values()[2];
                    }
                    default -> {
                        System.out.println("Введите число в диапазоне от 1 до 3");
                    }
                }
            } catch (NullPointerException e) {
                System.out.println("Введите число в диапазоне от 1 до 3");
            }

        }
    }

    /**
     * Requests Coordinates from terminal.
     *
     * @return Coordinates object
     */
    private static Coordinates requestCoordinates() {
        Coordinates coordinates = new Coordinates();

        //request X coordinate
        while (true) {
            try {
                System.out.print("Введите координату X: ");
                coordinates.setX(requestFloat());
                break;
            } catch (WrongFieldException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("X координата должна быть числом!");
            }
        }

        //request Y coordinate
        while (true) {
            try {
                System.out.print("Введите координату Y: ");
                coordinates.setY(requestFloat());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Y координата должна быть числом!");
            } catch (NullPointerException e) {
                coordinates.setY(0);
                break;
            }
        }
        return coordinates;
    }

    /**
     * Requests adminGroup from terminal.
     *
     * @return Person object
     */
    public static Person requestAdminGroup() {
        Person adminGroup = new Person();

        // request name
        while (true) {
            try {
                System.out.print("Введите имя админа групы: ");
                String name = requestString().toLowerCase();
                if (name.isEmpty()) return null;
                adminGroup.setName(name.substring(0, 1).toUpperCase() + name.substring(1));
                break;
            } catch (EmptyFieldException e) {
                System.out.println(e.getMessage());
            }
        }

        //request birthdate
        while (true) {
            try {
                System.out.print("Введите дату рождения админа в формате ДД-ММ-ГГГГ: ");
                adminGroup.setBirthday(requestDate());
                break;
            } catch (EmptyFieldException e) {
                System.out.println(e.getMessage());
            }
        }

        //request height
        while (true) {
            try {
                System.out.print("Введите рост: ");
                adminGroup.setHeight(requestFloat());
                break;
            } catch (WrongFieldException | EmptyFieldException e) {
                System.out.println(e.getMessage());
            }
        }

        return adminGroup;
    }

    /**
     * Get StudyGroup by fields from CLI
     * Method will ask each field. If input value is incorrect method will ask to enter it again.
     *
     * @param studyGroup object to add fields
     */

    public static void requestStudyGroup(StudyGroup studyGroup) {
        //request coordinates
        studyGroup.setCoordinates(requestCoordinates());

        //request name
        while (true) {
            try {
                System.out.print("Введите название группы: ");
                studyGroup.setName(requestString());
                break;
            } catch (EmptyFieldException e) {
                System.out.println(e.getMessage());
            }
        }

        // request studentsCount
        while (true) {
            try {
                System.out.print("Введите количество студентов: ");
                studyGroup.setStudentsCount(requestLong());
                break;
            } catch (WrongFieldException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Количество студентов должно быть числом");
            } catch (NullPointerException e) {
                System.out.println("Количество студентов не может быть " + null);
            }
        }

        //request formOfEducation
        while (true) {
            try {
                System.out.print("Выберите форму обучения: ");
                studyGroup.setFormOfEducation(requestFormOfEducation());
                break;
            } catch (EmptyFieldException e) {
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Форма обучения должна быть одной из предложенных");
            }
        }

        //request semester
        while (true) {
            try {
                System.out.print("Выберите семестр: ");
                studyGroup.setSemesterEnum(requestSemester());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Семестр должен быть одним из предложенных");
            }
        }
        // request groupAdmin can be null
        studyGroup.setGroupAdmin(requestAdminGroup());


    }

}
