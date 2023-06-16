package classes.utils;

import classes.commands.AbstractCommand;
import classes.commands.Save;
import classes.dataBase.UserData;
import classes.dataBase.UsersManager;
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
    CollectionManager collectionManager;

    public ClientSession(Socket socket, CollectionManager collectionManager) {
        this.socket = socket;
        this.collectionManager = collectionManager;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Соединение установлено: " + socket.getInetAddress());
            String user_login;
            while (!socket.isClosed()) {
                Response response = new Response();
                UserData user = (UserData) inputStream.readObject();
                if (user.isRegistrated()) {
                    if (UsersManager.checkUser(user)) {
                        response.setData("Вы успешно авторизованы");
                        response.setLastResponse();
                        user_login = user.getLogin();
                        outputStream.writeObject(response);
                        break;
                    }
                    else{
                        response.setData("Такой пользователь не найден");
                        outputStream.writeObject(response);
                    }
                }
                else {
                    if (UsersManager.addUser(user)){
                        response.setData("Вы успешно зарегистрированы и авторизованы");
                        response.setLastResponse();
                        user_login = user.getLogin();
                        outputStream.writeObject(response);
                        break;
                    }
                    else{
                        response.setData("Пользователь с таким логином уже существует");
                        outputStream.writeObject(response);
                    }
                }
                outputStream.flush();
            }
            outputStream.flush();

            while (!socket.isClosed()) {
                Request request = (Request) inputStream.readObject();
                AbstractCommand command = request.getCommand();
                ArgsShell inputData = request.getArguments();
                Response outputData = command.execute(collectionManager, inputData);

                outputStream.writeObject(outputData);
                outputStream.flush();
            }
            new Save().execute(collectionManager, new ArgsShell());
        } catch (IOException e) {
            collectionManager.save();
            System.out.println("Соединение разорвано, коллекция сохранена в файл");
            System.out.println("Ожидаю нового подключения");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

