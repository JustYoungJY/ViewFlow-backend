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

    @ExceptionHandler(AccessDeniedException.class)
    public ErrorDTO handleAccessDeniedException(RuntimeException exception) {
        return new ErrorDTO(exception.getMessage(), HttpStatus.FORBIDDEN.value(), LocalDateTime.now());
    }

    @ExceptionHandler(AlreadyLikedException.class)
    public ErrorDTO handleAlreadyLikedException(RuntimeException exception) {
        return new ErrorDTO(exception.getMessage(), HttpStatus.CONFLICT.value(), LocalDateTime.now());
    }

    @ExceptionHandler({
            CompilationNotFoundException.class,
            MediaNotFoundException.class,
            FavoriteNotFoundException.class,
    })
    public ErrorDTO handleNotFoundException(RuntimeException exception) {
        return new ErrorDTO(exception.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorDTO handleIllegalArgumentException(RuntimeException exception) {
        return new ErrorDTO(exception.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
    }
}
