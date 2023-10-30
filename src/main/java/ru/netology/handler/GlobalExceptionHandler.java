package ru.netology.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.netology.exceptions.ErrorResponse;
import ru.netology.exceptions.IdNotFoundException;
import ru.netology.exceptions.CodeExpiredException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder messages = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();
            messages.append(errorMessage).append("\n");
        });
        ErrorResponse response = new ErrorResponse(messages.toString(), 400);
        return new ResponseEntity<>(response, HttpStatus.valueOf(400));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(Exception ex) {
        String errorMessage = String.format("%s %s", "An unexpected error occurred:", ex.getMessage());
        ErrorResponse response = new ErrorResponse(errorMessage, 500);
        return new ResponseEntity<>(response, HttpStatus.valueOf(500));
    }

    @ExceptionHandler(CodeExpiredException.class)
    public ResponseEntity<ErrorResponse> handleCodeExpiredException(CodeExpiredException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), 400);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(400));
    }

    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleIdNotFoundException(IdNotFoundException ex) {
        String errorMessage = String.format("%s %s", "Operation id not found:", ex.getMessage());
        ErrorResponse response = new ErrorResponse(errorMessage, 400);
        return new ResponseEntity<>(response, HttpStatus.valueOf(400));
    }
}
