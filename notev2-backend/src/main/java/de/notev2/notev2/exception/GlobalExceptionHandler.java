package de.notev2.notev2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import de.notev2.notev2.dto.AuthResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<AuthResponse> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new AuthResponse(null, ex.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<AuthResponse> handleUserExists(UserAlreadyExistsException ex) {
    return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new AuthResponse(null, ex.getMessage()));
    }

    @ExceptionHandler(EmptyFieldException.class)
    public ResponseEntity<AuthResponse> handleEmptyField(EmptyFieldException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new AuthResponse(null, ex.getMessage()));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<AuthResponse> handleWrongLogin(InvalidCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new AuthResponse(null, ex.getMessage()));
    }
}
