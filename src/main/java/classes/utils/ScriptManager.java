package classes.utils;

import exceptions.EmptyFieldException;
import exceptions.NotEnoughLinesException;
import exceptions.WrongFieldException;
import classes.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;

/**
 * Class to reading objects from file
 */
public class ScriptManager {
    BufferedReader reader;

    public ScriptManager(BufferedReader reader) {
        this.reader = reader;
    }

    /**
     * getting float from file
     *
     * @return float
     * @throws IOException             file can't be reading
     * @throws NumberFormatException
     * @throws NoSuchElementException
     * @throws NotEnoughLinesException
     */
    public Float requestFloat() throws IOException, NumberFormatException, NoSuchElementException, NotEnoughLinesException {
        String line = reader.readLine();
        if (line == null) throw new NotEnoughLinesException("Не хватило строки с числом с плавающей точкой");
        if (line.length() == 0) return null;
        return Float.parseFloat(line);
    }

    public Long requestLong() throws IOException, NumberFormatException, NotEnoughLinesException {
        String line = reader.readLine();
        if (line == null) throw new NotEnoughLinesException("Не хватило строки с числом с плавающей точкой");
        if (line.length() == 0) return null;
        return Long.parseLong(line);
    }

    public String requestString() throws IOException, NotEnoughLinesException {
        String line = reader.readLine();
        if (line == null) throw new NotEnoughLinesException("Не хватило строки");
        return line;
    }

    public Date requestDate() throws NotEnoughLinesException, IOException {
        Date date;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);
        while (true) {
            try {
                date = dateFormat.parse((requestString()));
                return date;
            } catch (ParseException e) {
                continue;
            }
        }
    }

    private Semester requestSemester() throws NotEnoughLinesException, IOException {
        try {
            String option = requestString().strip().toLowerCase();
            if (option.length() == 0) return null;
            return Semester.valueOf(option.toUpperCase());
        } catch (IllegalArgumentException | NotEnoughLinesException e) {
            return null;
        }

    }

    private FormOfEducation requestFormOfEducation() throws NotEnoughLinesException, IOException {
        while (true) {
            try {
                String option = requestString().strip().toLowerCase();
                if (option.length() == 0) continue;
                return FormOfEducation.valueOf(option.toUpperCase());
            } catch (IllegalArgumentException e) {
                continue;
            }
        }
    }

    private Coordinates requestCoordinates() throws NotEnoughLinesException, IOException {
        Coordinates coordinates = new Coordinates();

        while (true) {
            try {
                coordinates.setX(requestFloat());
                break;
            } catch (WrongFieldException | NumberFormatException e) {
                continue;
            }
        }

        while (true) {
            try {
                coordinates.setY(requestFloat());
                break;
            } catch (NumberFormatException e) {
                continue;
            } catch (NotEnoughLinesException e) {
                coordinates.setY(0);
                break;
            }
        }
        return coordinates;
    }

    public Person requestAdminGroup() throws NotEnoughLinesException, IOException {
        Person adminGroup = new Person();

        try {
            String name = requestString().toLowerCase();
            if (name.isEmpty()) return null;
            adminGroup.setName(name.substring(0, 1).toUpperCase() + name.substring(1));

        } catch (EmptyFieldException e) {
            return null;
        }

        while (true) {
            try {
                adminGroup.setBirthday(requestDate());
                break;
            } catch (EmptyFieldException e) {
                continue;
            }
        }

        while (true) {
            try {
                adminGroup.setHeight(requestFloat());
                break;
            } catch (WrongFieldException | EmptyFieldException e) {
                continue;
            }
        }
        return adminGroup;
    }

    public void requestStudyGroup(StudyGroup studyGroup) throws NotEnoughLinesException, IOException {
        studyGroup.setCoordinates(requestCoordinates());

        while (true) {
            try {
                studyGroup.setName(requestString());
                break;
            } catch (EmptyFieldException e) {
                continue;
            }
        }

        while (true) {
            try {
                studyGroup.setStudentsCount(requestLong());
                break;
            } catch (WrongFieldException | NumberFormatException e) {
                continue;
            }
        }
        while (true) {
            try {
                studyGroup.setFormOfEducation(requestFormOfEducation());
                break;
            } catch (EmptyFieldException e) {
                continue;
            }
        }

        studyGroup.setSemesterEnum(requestSemester());
        studyGroup.setGroupAdmin(requestAdminGroup());

    }
}

