package exceptions;
/**
 * Exception that throws when value of field is empty, but can't be null
 */
public class EmptyFieldException extends Exception{
    public EmptyFieldException(String message){
        super(message);
    }
}
