package classes.dataBase;

import classes.model.Person;

import java.io.Reader;
import java.sql.*;

public class DBManager {
    private final static String createStudyGroupTable = """
            CREATE TABLE IF NOT EXISTS study_groups(
                key BIGINT NOT NULL UNIQUE CHECK(key > 0),
                group_id BIGSERIAL PRIMARY KEY,
                name VARCHAR(20) NOT NULL,
                coordinateX FLOAT NOT NULL CHECK(coordinateX >= - 478),
                coordinateY FLOAT,
                creation_date DATE NOT NULL,
                students_count BIGINT NOT NULL CHECK(students_count > 0),
                form_of_education_id INTEGER REFERENCES forms_of_education(form_id),
                semester_id INTEGER DEFAULT NULL REFERENCES semesters(semester_id),
                admin_id INTEGER DEFAULT NULL UNIQUE REFERENCES admins(admin_id),
                login VARCHAR(30) NOT NULL REFERENCES users(login)
            );""";
    private final static String createAdminGroupTable = """
            CREATE TABLE IF NOT EXISTS admins(
                admin_id BIGSERIAL PRIMARY KEY,
                name VARCHAR(20) NOT NULL,
                birthday DATE NOT NULL,
                height FLOAT DEFAULT NULL CHECK(height > 0)
            );""";
    private final static String createFormOfEducationTable = """
            CREATE TABLE IF NOT EXISTS forms_of_education(
                form_id INTEGER PRIMARY KEY,
                form VARCHAR(30) NOT NULL
            );
            INSERT INTO forms_of_education(form_id, form) VALUES(1, 'DISTANCE_EDUCATION'),
            (2, 'FULL_TIME_EDUCATION'), (3, 'EVENING_CLASSES')
            On CONFLICT(form_id) DO NOTHING;""";

    private final static String createSemesterTable = """
            CREATE TABLE IF NOT EXISTS semesters(
             	semester_id INTEGER PRIMARY KEY,
            	semester VARCHAR(30) NOT NULL
            );
            INSERT INTO semesters(semester_id, semester) VALUES(1, 'SECOND'),
            (2, 'FOURTH'), (3, 'FIFTH'), (4, 'SIXTH')
            On CONFLICT(semester_id) DO NOTHING;
            """;


    public static void main(String[] args) {
        execute(createAdminGroupTable);
        execute(createSemesterTable);
        execute(createFormOfEducationTable);
        UsersManager.init();
        execute(createStudyGroupTable);
    }

    static Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/studs";
            String login = "postgres";
            String password = "vaderV07";
            dbConnection = DriverManager.getConnection(url, login, password);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dbConnection;
    }

    public static void execute(String command) {
        try (Connection dbConnection = getDBConnection(); Statement statement = dbConnection.createStatement()) {
            statement.execute(command);
            System.out.println("Команда отправлена!");
        } catch (SQLException e) {
            System.out.println("База данных не отвечает");
        }
    }

    public static ResultSet executeQuery(String command) {
        try {
            Connection dbConnection = getDBConnection();
            Statement statement = dbConnection.createStatement();
            return statement.executeQuery(command);
        } catch (SQLException e) {
            System.out.println("База данных не отвечает");
        }
        return null;
    }

    public static ResultSet getAdmin(int id) {
        try {
            Connection dbConnection = getDBConnection();
            PreparedStatement statement = dbConnection.prepareStatement("SELECT * FROM admins WHERE admin_id = ?");
            statement.setObject(1, id);
            return statement.executeQuery();
        } catch (SQLException e) {
            System.out.println("База данных не отвечает");
        }
        return null;

    }

    public static ResultSet getFormOfEducation(int id) {
        try {
            Connection dbConnection = getDBConnection();
            PreparedStatement statement = dbConnection.prepareStatement("SELECT * FROM forms_of_education WHERE form_id = ?");
            statement.setInt(1, id);
            return statement.executeQuery();
        } catch (SQLException e) {
            System.out.println("База данных не отвечает");
        }
        return null;
    }


    public static ResultSet getSemester(int id) {
        try {Connection dbConnection = getDBConnection();
        PreparedStatement statement = dbConnection.prepareStatement("SELECT * FROM semesters WHERE semester_id = ?");
        statement.setInt(1, id);
        return statement.executeQuery();
    } catch(
    SQLException e)

    {
        System.out.println("База данных не отвечает");
    }
        return null;
}


    public static ResultSet getLogin(int id) {
        try {
            Connection dbConnection = getDBConnection();
            PreparedStatement statement = dbConnection.prepareStatement("SELECT user FROM semesters WHERE semester_id = ?");
            statement.setInt(1, id);
            return statement.executeQuery();
        } catch (SQLException e) {
            System.out.println("База данных не отвечает");
        }
        return null;
    }


    public static Integer updAdmin(int id, Person admin) {
        try (Connection dbConnection = getDBConnection();
             PreparedStatement statement = dbConnection.prepareStatement("UPDATE admins SET name = ?, birthday = ?, height = ? WHERE admin_id = ?")) {
            statement.setString(1, admin.getName());
            statement.setDate(2, (Date) admin.getBirthdayDate());
            statement.setObject(3, admin.getHeight());
            statement.setInt(4, id);
            return statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("База данных не отвечает");
        }
        return 0;
    }

    public static Integer insertAdmin(Person admin) {
        try (
                Connection dbConnection = getDBConnection();
                PreparedStatement statement = dbConnection.prepareStatement("Insert INTO admins (name, birthday, height) VALUES(?, ?, ?) returning admin_id");
        ) {
            statement.setString(1, admin.getName());
            statement.setDate(2, Date.valueOf(admin.getBirthday()));
            statement.setObject(3, admin.getHeight());
            ResultSet res = statement.executeQuery();
            res.next();
            return res.getInt(1);
        } catch (SQLException e) {
            System.out.println("База данных не отвечает");
        }
        return null;
    }

    public static Integer getFormOfEducationID(String form) {
        try (
                Connection dbConnection = getDBConnection();
                PreparedStatement statement = dbConnection.prepareStatement("SELECT form_id FROM forms_of_education WHERE form = ?");
        ) {
            statement.setString(1, form);
            ResultSet res = statement.executeQuery();
            res.next();
            Integer id = res.getInt(1);
            return id;
        } catch (SQLException e) {
            System.out.println("База данных не отвечает");
            throw new RuntimeException(e);
        }
    }

    public static Integer getSemesterID(String semester) {
        try (
                Connection dbConnection = getDBConnection();
                PreparedStatement statement = dbConnection.prepareStatement("SELECT semester_id FROM semesters WHERE semester = ?");
        ) {
            statement.setString(1, semester);
            ResultSet res = statement.executeQuery();
            res.next();
            return res.getInt(1);
        } catch (SQLException e) {
            System.out.println("База данных не отвечает");
            throw new RuntimeException();
        }
    }

    public static boolean updateStudyGroup(int group_id, String name, float coordinateX, float coordinateY, Date
            creationDate, Long studentsCount, int form, Integer semester, Integer admin) {
        try (
                Connection dbConnection = getDBConnection();
                PreparedStatement statement = dbConnection.prepareStatement("UPDATE study_groups SET name = ?, coordinatex = ?, coordinatey = ?, creation_date = ?, students_count = ?, form_of_education_id = ?, semester_id = ?, admin_id = ?  WHERE group_id = ?");
        ) {
            statement.setString(1, name);
            statement.setFloat(2, coordinateX);
            statement.setFloat(3, coordinateY);
            statement.setDate(4, creationDate);
            statement.setLong(5, studentsCount);
            statement.setInt(6, form);
            statement.setObject(7, semester);
            statement.setObject(8, admin);
            statement.setInt(9, group_id);
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            System.out.println("База данных не отвечает");

        }
        return false;
    }

    public static int insertStudyGroup(int key, String name, float coordinateX, float coordinateY, Date
            creationDate, Long studentsCount, int form, Integer semester, Integer admin, String login) {
        try (
                Connection dbConnection = getDBConnection();
                PreparedStatement statement = dbConnection.prepareStatement("INSERT INTO study_groups (key, name, coordinatex, coordinatey, creation_date, students_count, form_of_education_id, semester_id, admin_id, login) VALUES  (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning semester_id");
        ) {
            statement.setInt(1, key);
            statement.setString(2, name);
            statement.setFloat(3, coordinateX);
            statement.setFloat(4, coordinateY);
            statement.setDate(5, creationDate);
            statement.setLong(6, studentsCount);
            statement.setInt(7, form);
            statement.setObject(8, semester);
            statement.setObject(9, admin);
            statement.setString(10, login);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            System.out.println("База данных не отвечает");
        }
        return 0;
    }

    public static boolean removeStudyGroup(int id) {
        try (Connection dbConnection = getDBConnection()) {
            PreparedStatement statement = dbConnection.prepareStatement("DELETE FROM studs.public.study_groups WHERE studs.public.study_groups.group_id = ?");
            statement.setInt(1, id);
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static Integer removeLowerKeys(int key, String login) {
        try (Connection dbConnection = getDBConnection();
             PreparedStatement statement1 = dbConnection.prepareStatement("""
                     Delete From studs.public.admins WHERE admin_id in (SELECT admin_id FROM studs.public.study_groups WHERE studs.public.study_groups.key < ? AND login = ?);
                                          
                     """);
             PreparedStatement statement2 = dbConnection.prepareStatement("DELETE FROM studs.public.study_groups WHERE studs.public.study_groups.key < ? and login = ?;")) {
            statement1.setInt(1, key);
            statement1.setString(2, login);
            statement2.setInt(1, key);
            statement2.setString(2, login);
            statement1.executeUpdate();
            return statement2.executeUpdate();
        } catch (SQLException e) {
            return 0;
        }
    }

}
