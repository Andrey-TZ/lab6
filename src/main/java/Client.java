import classes.shells.Request;
import classes.shells.Response;
import classes.utils.CommandManager;

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
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            CommandManager commandManager = new CommandManager();
            while (true) {
                Request request = commandManager.start();
                out.writeObject(request);
                out.flush();

                Response response = (Response) in.readObject();
                response.showData();
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
            System.out.println(Arrays.toString(e.getStackTrace()));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}
