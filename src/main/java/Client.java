import classes.shells.Request;
import classes.shells.Response;
import classes.utils.CommandManager;
import com.google.gson.internal.bind.util.ISO8601Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class Client {
    private static int connectAttemptsCount = 0;
    private static final int maxConnectAttemptsCount = 15;

    public static void main(String[] args) throws InterruptedException {
        int port = 5678;
        String host = "localhost";
        connect(host, port);
    }

    private static void connect(String host, int port) throws InterruptedException {
        System.out.println("Подключение к серверу...");
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            System.out.println("Подключение к серверу произошло успешно");
            while (true) {
                Request request = CommandManager.start();
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
