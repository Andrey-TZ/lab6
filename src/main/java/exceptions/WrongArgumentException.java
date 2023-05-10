package exceptions;

/**
 * Exception that throws when value from command line does not match to requirements
 */
public class WrongArgumentException extends Throwable{
    public WrongArgumentException(String message){
        super(message);
    }
}
