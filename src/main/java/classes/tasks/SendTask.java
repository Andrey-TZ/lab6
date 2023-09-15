package classes.tasks;

import classes.shells.Response;
import classes.utils.ClientSession;

import java.io.IOException;

import static java.lang.Thread.currentThread;

public class SendTask implements Runnable{
    ClientSession clientSession;
    Response response;

    public SendTask(ClientSession clientSession, Response response){
        this.clientSession = clientSession;
        this.response = response;
    }
    @Override
    public void run() {
        try {
            clientSession.outputStream.writeObject(response);
            clientSession.outputStream.flush();
            System.out.println("Выполняется в потоке " + currentThread().getName());
            System.out.println("Send task is finished");
        } catch (IOException e) {
            System.out.println("Не удалось отправить ответ");
        }
    }
}
