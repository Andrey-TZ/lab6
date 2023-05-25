import classes.utils.ClientSession;
import classes.utils.CollectionManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    public static void main(String[] args) {
        int port = 5678;
        CollectionManager collectionManager = new CollectionManager();
        while (true) {
            try (ServerSocket server = new ServerSocket(port)) {
                Socket clientSocket = server.accept();
                ClientSession clientSession = new ClientSession(clientSocket, collectionManager);
//                CommandExecutor commandExecutor = new CommandExecutor(collectionManager);
//                Thread commandExecutorThread = new Thread(commandExecutor);
//                commandExecutorThread.start();
                clientSession.run();
//                Thread clientSessionThread = new Thread(clientSession);
//                clientSessionThread.start();
            } catch (IOException e) {
                System.out.println("Соединение разорвано, ожидаю нового подключения");
                collectionManager.save();
            }
        }
    }
}
