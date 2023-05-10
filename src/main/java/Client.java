import classes.utils.CommandManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args){
        int port = 5678;
        //Обработка аргумента командной строки, если не получилось открыть такой файл или аргумент не был передан


        try(Socket socket = new Socket("localhost", port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            CommandManager commandManager = new CommandManager();
            commandManager.start(out);
        } catch (IOException e) {
            System.out.println("Соединение разорвано");
        }

    }
}
