import classes.utils.ClientSession;
import classes.utils.CollectionManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;


public class Server {
    public static void main(String[] args) throws SQLException {
        int port = 5678;
        CollectionManager collectionManager = new CollectionManager();
        while (true) {
            try (ServerSocket server = new ServerSocket(port)) {
                Socket clientSocket = server.accept();
                ClientSession clientSession = new ClientSession(clientSocket, collectionManager);
                Thread clientSessionThread = new Thread(clientSession);
                clientSessionThread.start();
            } catch (IOException e) {
                System.out.println("Соединение разорвано, ожидаю нового подключения");
            }
        }
    }
}
