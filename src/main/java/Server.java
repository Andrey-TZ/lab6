import classes.utils.CLIManager;
import classes.utils.ClientSession;

import javax.naming.NoPermissionException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Server {
    public static void main(String[] args) {
        int port = 5678;
//        Path path;
//        if (args.length > 0) {
//            path = Paths.get(args[1]);
//            try {
//                if (!Files.exists(path)) throw new FileNotFoundException("Файл \"" + path + "\" не найден");
//                if (!Files.isReadable(path)) throw new NoPermissionException("Нет получается прочитать " + path);
//                if (!Files.isWritable(path)) throw new NoPermissionException("Не получается записать данные в " + path);
//
//            } catch (NoPermissionException | FileNotFoundException e) {
//                System.out.println(e.getMessage());
//                path = CLIManager.requestFilePath();
//            }
//        } else {
//            path = CLIManager.requestFilePath();
//        }
        while (true) {
            try (ServerSocket server = new ServerSocket(port)) {
                server.setReuseAddress(true);
                Socket clientSocket = server.accept();
                ClientSession clientSession = new ClientSession(clientSocket);
                Thread clientSessionThread = new Thread(clientSession);
                clientSessionThread.start();
            } catch (IOException e) {
                System.out.println("Соединение разорвано, ожидаю нового подключения");
            }
        }
    }
}
