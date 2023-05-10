package exceptions;
/**
 * Exception that throws when value of field does not match to requirements
 */
public class WrongFieldException extends Exception{
    public WrongFieldException(String message){
        super(message);
    }

}
