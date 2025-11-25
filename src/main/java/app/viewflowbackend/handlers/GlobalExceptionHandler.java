package app.viewflowbackend.handlers;

import app.viewflowbackend.DTO.error.ErrorDTO;
import app.viewflowbackend.DTO.error.ValidationDetailDTO;
import app.viewflowbackend.exceptions.alreadyExists.AlreadyLikedException;
import app.viewflowbackend.exceptions.alreadyExists.EmailAlreadyExistsException;
import app.viewflowbackend.exceptions.alreadyExists.TagAlreadyExistsException;
import app.viewflowbackend.exceptions.alreadyExists.UsernameAlreadyExistsException;
import app.viewflowbackend.exceptions.auth.InvalidPasswordException;
import app.viewflowbackend.exceptions.auth.InvalidRefreshTokenException;
import app.viewflowbackend.exceptions.auth.PermissionDeniedException;
import app.viewflowbackend.exceptions.notFound.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

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

    @ExceptionHandler(PermissionDeniedException.class)
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
            TagNotFoundException.class,
            BadgeNotFoundException.class,
            MediaBadgeNotFoundException.class
    })
    public ErrorDTO handleNotFoundException(RuntimeException exception) {
        return new ErrorDTO(exception.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorDTO handleIllegalArgumentException(RuntimeException exception) {
        return new ErrorDTO(exception.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
    }

    @ExceptionHandler(TagAlreadyExistsException.class)
    public ErrorDTO handleAlreadyExistsException(RuntimeException exception) {
        return new ErrorDTO(exception.getMessage(), HttpStatus.CONFLICT.value(), LocalDateTime.now());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ErrorDTO handleMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        return new ErrorDTO(
                exception.getMessage(), HttpStatus.METHOD_NOT_ALLOWED.value(), LocalDateTime.now()
        );
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationDetailDTO>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        List<ValidationDetailDTO> dto = exception.getBindingResult().getAllErrors()
                .stream().map(error ->
                {
                    String field = "Object";
                    if (error instanceof FieldError) {
                        field = ((FieldError) error).getField();
                    }
                    String message = error.getDefaultMessage();
                    return new ValidationDetailDTO(field, message);
                }).toList();

        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }
}
