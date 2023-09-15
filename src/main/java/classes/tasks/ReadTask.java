package classes.tasks;

import classes.commands.AbstractCommand;
import classes.dataBase.UserData;
import classes.shells.ArgsShell;
import classes.shells.Request;
import classes.utils.ClientSession;
import classes.utils.CollectionManager;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import static java.lang.Thread.currentThread;


public class ReadTask implements Runnable {
    ClientSession clientSession;
    private final ExecutorService executeService;

    public ReadTask(ClientSession clientSession, ExecutorService executeService) {
        this.clientSession = clientSession;
        this.executeService = executeService;


    }

    @Override
    public void run() {

        while (!clientSession.socket.isClosed()) {
            try {
                Request request = (Request) clientSession.inputStream.readObject();
                AbstractCommand command = request.getCommand();
                ArgsShell inputData = request.getArguments();
                UserData user = request.getUser();
                System.out.println("read finished");
                System.out.println("Выполняется в потоке " + currentThread().getName());
                executeService.execute(new ExecuteTask(clientSession, command, inputData, user));
            }
            catch (IOException e) {
                System.out.println("Соединение разорвано");
                System.out.println("Ожидаю нового подключения");
                break;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
