package ru.netology.exceptions;

public class CodeExpiredException extends RuntimeException{
    public CodeExpiredException(String message) {
        super(message);
    }
}
