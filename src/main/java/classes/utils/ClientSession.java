package classes.utils;

import classes.commands.AbstractCommand;
import classes.commands.Save;
import classes.dataBase.UserData;
import classes.dataBase.UsersManager;
import classes.shells.ArgsShell;
import classes.model.*;
import classes.shells.Request;
import classes.shells.Response;
import classes.tasks.ReadTask;
import exceptions.NotEnoughArgumentsException;
import exceptions.WrongArgumentException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import java.util.concurrent.*;

public class ClientSession implements Runnable {
    public Socket socket;
    public final CollectionManager collectionManager;
    public final ExecutorService executeService;
    public final ExecutorService sendService;
    private final ExecutorService readService;
    public ObjectInputStream inputStream;
    public ObjectOutputStream outputStream;

    public ClientSession(Socket socket, CollectionManager collectionManager, ExecutorService readService, ExecutorService executeService, ExecutorService sendService) {
        this.socket = socket;
        this.collectionManager = collectionManager;
        this.readService = readService;
        this.executeService = executeService;
        this.sendService = sendService;

    }

    @Override
    public void run() {
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Соединение установлено: " + socket.getInetAddress());

            //Блок авторизации
            while (!socket.isClosed()) {
                UserData user = (UserData) inputStream.readObject();
                Response response = authorise(user);
                outputStream.writeObject(response);
                if (response.isLastResponse()) break;
                outputStream.flush();
            }
            outputStream.flush();

            readService.execute(new ReadTask(this, executeService));
        } catch (IOException e) {
            System.out.println("Соединение разорвано, коллекция сохранена в файл");
            System.out.println("Ожидаю нового подключения");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Response authorise(UserData user) {
        Response response = new Response();
        if (user.isRegistrated()) {
            if (UsersManager.checkUser(user)) {
                response.setData("Вы успешно авторизованы");
                response.setLastResponse();
            } else {
                response.setData("Такой пользователь не найден");
            }

        } else {
            if (UsersManager.checkLogin(user.getLogin())) {
                response.setData("Пользователь с таким логином уже существует");
            } else if (UsersManager.addUser(user)) {
                response.setData("Вы успешно зарегистрированы и авторизованы");
                response.setLastResponse();
            } else {
                response.setData("Пользователь с таким логином уже существует");
            }
        }
        return response;
    }
}

