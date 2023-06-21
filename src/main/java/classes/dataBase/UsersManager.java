package classes.dataBase;

import java.sql.*;

public class UsersManager extends DBManager {
    private final static String createUserTable = """
            CREATE TABLE IF NOT EXISTS  users(
                login VARCHAR(30) NOT NULL CHECK(login <> '') PRIMARY KEY,
                password TEXT NOT NULL
            );""";

    public static void init() {
        execute(createUserTable);
    }

    public static boolean addUser(UserData userData) {
        if (checkUser(userData)) return false;
        String command = "INSERT INTO users (login, password) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (Connection dbConnection = getDBConnection();
             PreparedStatement statement = dbConnection.prepareStatement(command)) {
            statement.setString(1, userData.getLogin());
            statement.setString(2, userData.getHashedPassword());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean checkUser(UserData userData) {
        try (Connection dbConnection = getDBConnection();
             PreparedStatement require = dbConnection.prepareStatement("SELECT count(*) FROM users WHERE login = ? AND password = ?")) {
            require.setString(1, userData.getLogin());
            require.setString(2, userData.getHashedPassword());
            ResultSet result = require.executeQuery();
            result.next();
            if (result.getInt(1) != 0) return true;
        } catch (SQLException e) {
            System.out.println("Что-то пошло не так");
        }
        return false;
    }

    public static boolean checkLogin(String login) {
        try (Connection dbConnection = getDBConnection();
             PreparedStatement require = dbConnection.prepareStatement("SELECT count(*) FROM users WHERE login = ? ")) {
            require.setString(1, login);
            ResultSet result = require.executeQuery();
            result.next();
            System.out.println(result.getInt(1));
            if (result.getInt(1) != 0) return true;
        } catch (SQLException e) {
            System.out.println("Что-то пошло не так");
        }
        return false;
    }
}
