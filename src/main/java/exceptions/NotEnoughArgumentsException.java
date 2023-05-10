package exceptions;
/**
 * Exception that throws when arguments count doest not match command requirements.
 */
public class NotEnoughArgumentsException extends Throwable {
    public NotEnoughArgumentsException(String message){
        super(message);
    }
}
