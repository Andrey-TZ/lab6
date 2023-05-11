package classes.utils;

import classes.commands.AbstractCommand;
import classes.shells.ArgsShell;
import classes.model.*;
import classes.shells.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSession implements Runnable {
    Socket socket;

    public ClientSession(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Соединение установлено: " + socket.getInetAddress());

            while (!socket.isClosed()) {
                Object inputObject = inputStream.readObject();
                if (inputObject instanceof AbstractCommand command) {
                    System.out.println(command);
                    Response outputData;
                    ArgsShell inputData = (ArgsShell) inputStream.readObject();
                    CollectionManager collectionManager = new CollectionManager();
//                    command.execute(collectionManager, inputData, outputData);

                }

                outputStream.flush();

            }
        } catch (IOException e) {
            System.out.println("Соединение разорвано, ожидаю нового подключения");
            System.out.println("Хранилище сохранено в файл");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
