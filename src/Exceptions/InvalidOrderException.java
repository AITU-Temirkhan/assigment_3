package Exceptions;

public class InvalidOrderException extends Exception {
    public InvalidOrderException(String message) {
        super(message);
    }
}