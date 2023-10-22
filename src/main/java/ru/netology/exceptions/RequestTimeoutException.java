package ru.netology.exceptions;

public class RequestTimeoutException extends RuntimeException{
    public RequestTimeoutException(String message) {
        super(message);
    }
}
