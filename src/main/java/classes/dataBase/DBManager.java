package classes.dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {
    private final static String createStudyGroupTable = """
            CREATE TABLE IF NOT EXISTS study_groups(
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



    public static void main(String[] args){
        execute(createAdminGroupTable);
        execute(createSemesterTable);
        execute(createFormOfEducationTable);
        UsersManager.init();
        execute(createStudyGroupTable);
    }

    static Connection getDBConnection(){
        Connection dbConnection = null;
        try{
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
        try(Connection dbConnection = getDBConnection(); Statement stmnt = dbConnection.createStatement()){
            stmnt.execute(command);
            System.out.println("Команда отправлена!");
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
