package classes.tasks;

import classes.commands.AbstractCommand;
import classes.dataBase.UserData;
import classes.shells.ArgsShell;
import classes.shells.Response;
import classes.utils.ClientSession;

import static java.lang.Thread.currentThread;

public class ExecuteTask implements Runnable{
    ClientSession clientSession;
    AbstractCommand command;
    ArgsShell args;
    UserData user;
    public ExecuteTask(ClientSession clientSession, AbstractCommand command, ArgsShell args, UserData user){
        this.clientSession = clientSession;
        this.command = command;
        this.args = args;
        this.user = user;
    }


    @Override
    public void run() {
        Response response = command.execute(clientSession.collectionManager, args, user);
        System.out.println("Выполняется в потоке " + currentThread().getName());
        System.out.println("execution is finished");
        clientSession.sendService.execute(new SendTask(clientSession, response));
    }
}
