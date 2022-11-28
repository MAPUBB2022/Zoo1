package utils;

public class NoMoreAvailableTicketsException extends Exception {
    public NoMoreAvailableTicketsException(String message){
        super(message);
    }
}
