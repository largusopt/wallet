package org.example.exception;

public class AmountNotEnoughtException extends RuntimeException {
    public AmountNotEnoughtException(String message) {
        super(message);
    }
}
