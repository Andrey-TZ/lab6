package classes.shells;

import java.io.Serializable;

public class ArgsShell implements Serializable {
    private Object[] arguments;

    public ArgsShell(){
        this.arguments = new Object[]{};
    }
    public ArgsShell(Object[]arguments){
        this.arguments = arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public Object[] getArguments(){
        return arguments;
    }

}
