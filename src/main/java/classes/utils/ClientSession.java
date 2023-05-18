package classes.utils;

import classes.commands.AbstractCommand;
import classes.commands.Save;
import classes.shells.ArgsShell;
import classes.model.*;
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
                Object inputObject = inputStream.readObject();
                AbstractCommand command = (AbstractCommand) inputObject;
                System.out.println(command);
                ArgsShell inputData = (ArgsShell) inputStream.readObject();
                Response outputData = command.execute(collectionManager, inputData);
                System.out.println(outputData);

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

