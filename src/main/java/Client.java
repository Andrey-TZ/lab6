import classes.UserAuthorisationHandler;
import classes.dataBase.UserData;
import classes.shells.Request;
import classes.shells.Response;
import classes.utils.CommandManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {
    private static int connectAttemptsCount = 0;
    private static final int maxConnectAttemptsCount = 15;
    private static UserData user;

    public static void main(String[] args) throws InterruptedException {
        int port = 5678;
        String host = "helios.cs.ifmo.ru";
        connect(host, port);
    }

    private static void connect(String host, int port) throws InterruptedException {
        System.out.println("Подключение к серверу...");
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            System.out.println("Подключение к серверу произошло успешно");
            System.out.println("Чтобы продолжить работу, необходимо авторизоваться");
            while (true) {
                UserData userData = UserAuthorisationHandler.authorise();
                out.writeObject(userData);
                out.flush();
                Response response = (Response) in.readObject();
                response.showData();
                // Если получилось авторизоваться
                if (response.isLastResponse()) {
                    user = userData;
                    break;
                }
            }


            while (true) {
                Request request = CommandManager.start();
                request.setUser(user);
                out.writeObject(request);
                out.flush();

                Response response = (Response) in.readObject();
                response.showData();
                if (response.isLastResponse())
                    System.exit(0);
            }

        } catch (UnknownHostException e) {
            System.out.println("Неизвестный хост: " + host);
        } catch (SocketException e) {
            if (connectAttemptsCount == 0)
                System.out.println("Сервер не отвечает");
            if (connectAttemptsCount <= maxConnectAttemptsCount) {
                connectAttemptsCount += 1;
                Thread.sleep(1000);
                connect(host, port);
            } else System.out.println("Не удаётся установить соединение");
        } catch (IOException e) {
            System.out.println("Ошибка соединения");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}
