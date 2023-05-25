package classes.utils;

import classes.commands.AbstractCommand;
import classes.commands.Save;
import classes.shells.ArgsShell;
import classes.model.*;
import classes.shells.Request;
import classes.shells.Response;
import exceptions.NotEnoughArgumentsException;
import exceptions.WrongArgumentException;

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
        CollectionManager collectionManager = new CollectionManager();
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Соединение установлено: " + socket.getInetAddress());

            while (!socket.isClosed()) {
                Request request = (Request) inputStream.readObject();
                AbstractCommand command = request.getCommand();
                ArgsShell inputData = request.getArguments();
                Response outputData = command.execute(collectionManager, inputData);

                outputStream.writeObject(outputData);
                outputStream.flush();
            }
        } catch (IOException e) {
            collectionManager.save();
            System.out.println("Соединение разорвано, хранилище сохранено в файл");
            System.out.println("Ожидаю нового подключения");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

