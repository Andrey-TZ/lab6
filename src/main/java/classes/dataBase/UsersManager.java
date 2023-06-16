package classes.dataBase;

import java.sql.*;

public class UsersManager extends DBManager {
    private static final Connection connection = getDBConnection();
private final static String createUserTable = """
        CREATE TABLE IF NOT EXISTS  users(
            login VARCHAR(30) NOT NULL CHECK(login <> '') PRIMARY KEY,
            password TEXT NOT NULL
        );""";

    public static void init(){
        execute(createUserTable);
    }

    public static boolean addUser(UserData userData){
        if (checkUser(userData)) return false;
        String command = "INSERT INTO users (login, password) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try(PreparedStatement statement = connection.prepareStatement(command)){
            statement.setString(1, userData.getLogin());
            statement.setString(2, userData.getHashedPassword());
            statement.execute();
            return true;
        }
        catch (SQLException e){
            return false;
        }
    }
    public static boolean checkUser(UserData userData){
        String command = "SELECT count(*) FROM users WHERE login = ? AND password = ?";
        try(PreparedStatement require = connection.prepareStatement(command)){
            require.setString(1, userData.getLogin());
            require.setString(2, userData.getHashedPassword());
            ResultSet result = require.executeQuery();
            System.out.println(result.getArray(1));
            if (result.getInt(1) != 0) return true;
        } catch (SQLException e) {
            System.out.println("Что-то пошло не так");
        }
        return false;
    }
}
