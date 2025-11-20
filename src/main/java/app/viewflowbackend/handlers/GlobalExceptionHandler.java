package app.viewflowbackend.handlers;

import app.viewflowbackend.DTO.ErrorDTO;
import app.viewflowbackend.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            InvalidPasswordException.class,
            EmailAlreadyExistsException.class,
            UsernameAlreadyExistsException.class,
            UserNotFoundException.class
    })
    public ErrorDTO handleAuthenticationException(RuntimeException exception) {
        return new ErrorDTO(exception.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ErrorDTO handleInvalidRefreshTokenException(RuntimeException exception) {
        return new ErrorDTO(exception.getMessage(), HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now());
    }
}
