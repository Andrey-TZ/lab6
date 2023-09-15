import classes.commands.AbstractCommand;
import classes.shells.Response;
import classes.tasks.AuthoriseTask;
import classes.utils.ClientSession;
import classes.utils.CollectionManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.*;


public class Server {
    static final ExecutorService executeService = Executors.newFixedThreadPool(2);
    static final ExecutorService sendService = Executors.newFixedThreadPool(2);
    static final ExecutorService readService = Executors.newFixedThreadPool(2);
    static BlockingDeque<Response> responsesDeque = new LinkedBlockingDeque<>();
    static BlockingDeque<AbstractCommand> commandsDeque = new LinkedBlockingDeque<>();


    public static void main(String[] args) throws SQLException {
        int port = 5678;
        CollectionManager collectionManager = new CollectionManager();
        while (true) {
            try (ServerSocket server = new ServerSocket(port)) {
                Socket clientSocket = server.accept();
                System.out.println("Произошло подключение по сокету " + clientSocket);
                ClientSession clientSession = new ClientSession(clientSocket, collectionManager, readService, executeService, sendService);

                Thread clientSessionThread = new Thread(clientSession);
                clientSessionThread.start();



            } catch (IOException e) {
                System.out.println("Соединение разорвано, ожидаю нового подключения");
            }
        }
    }
}
