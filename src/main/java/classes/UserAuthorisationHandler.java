package classes;

import classes.dataBase.UserData;
import java.util.Scanner;

public class UserAuthorisationHandler {
    private static final Scanner scanner = new Scanner(System.in);
    public static UserData authorise(){
        while(true){
            System.out.print("Введите 1, если вы уже зарегистрированы, или 0, если хотите зарегистрироваться: ");
            String variant = scanner.nextLine();
            try {
                int var = Integer.parseInt(variant);

                if (var == 0){
                    return register();
                }
                else if(var == 1){
                    return logIn();
                }
            }
            catch(NumberFormatException e){
                System.out.println("Надо ввести число!");
            }
        }
    }
    private static UserData logIn(){
        String login, password;
        while (true) {
            System.out.print("Введите логин: ");
            login = scanner.nextLine();

            if (login.length() <= 30 && login.length()>1){
                break;
            }
            System.out.println("Неверный логин. Максимальная длина логина - 30, а минимальная - 2");
        }
        while(true){
            System.out.print("Введите пароль: ");
            password = scanner.nextLine();
            if (password.length() >= 6){
                break;
            }
            System.out.println("Неподходящий пароль. Минимальная длина - 6 символов");
        }
        return new UserData(login, password, true);
    }

    private static UserData register(){
        String login, password, double_password;
        while (true) {
            System.out.print("Введите логин: ");
            login = scanner.nextLine();
            if (login.length() <= 30 && login.length()>1){
                break;
            }
            System.out.println("Максимальная длина логина - 30, а минимальная - 2");
        }
        while(true){
            System.out.print("Введите пароль: ");
            password = scanner.nextLine();
            if (password.length() >= 6){
                break;
            }
            System.out.println("Неподходящий пароль. Минимальная длина - 6 символов");
        }
        do {
            System.out.print("Повторите пароль: ");
            double_password = scanner.nextLine();
        }
        while (!double_password.equals(password));
        return new UserData(login, password, false);

    }
}
