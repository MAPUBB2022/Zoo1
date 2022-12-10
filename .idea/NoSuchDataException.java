package utils;

public class NoSuchDataException extends NumberFormatException{

    public NoSuchDataException(){};
    public NoSuchDataException(String message){
        super(message);
    }
}
