package exceptions;

/**
 * Exception that throws when executeScript can't be finished correctly because of not enough lines in file
 */
public class NotEnoughLinesException extends Exception{
    public NotEnoughLinesException(String message){
        super(message);
    }
}
