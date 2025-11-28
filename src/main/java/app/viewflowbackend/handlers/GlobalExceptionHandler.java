package app.viewflowbackend.handlers;

import app.viewflowbackend.DTO.error.ErrorDTO;
import app.viewflowbackend.DTO.error.ValidationDetailDTO;
import app.viewflowbackend.exceptions.alreadyExists.AlreadyLikedException;
import app.viewflowbackend.exceptions.alreadyExists.EmailAlreadyExistsException;
import app.viewflowbackend.exceptions.alreadyExists.TagAlreadyExistsException;
import app.viewflowbackend.exceptions.alreadyExists.UsernameAlreadyExistsException;
import app.viewflowbackend.exceptions.api.InvalidResponseFormatException;
import app.viewflowbackend.exceptions.auth.InvalidPasswordException;
import app.viewflowbackend.exceptions.auth.InvalidRefreshTokenException;
import app.viewflowbackend.exceptions.auth.PermissionDeniedException;
import app.viewflowbackend.exceptions.notFound.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
            UserNotFoundException.class,
            UsernameNotFoundException.class,
    })
    public ResponseEntity<ErrorDTO> handleAuthenticationException(RuntimeException exception) {
        ErrorDTO errorDTO = new ErrorDTO(exception.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ErrorDTO> handleInvalidRefreshTokenException(RuntimeException exception) {
        ErrorDTO errorDTO = new ErrorDTO(exception.getMessage(), HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now());
        return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<ErrorDTO> handleAccessDeniedException(RuntimeException exception) {
        ErrorDTO errorDTO = new ErrorDTO(exception.getMessage(), HttpStatus.FORBIDDEN.value(), LocalDateTime.now());
        return new ResponseEntity<>(errorDTO, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AlreadyLikedException.class)
    public ResponseEntity<ErrorDTO> handleAlreadyLikedException(RuntimeException exception) {
        ErrorDTO errorDTO = new ErrorDTO(exception.getMessage(), HttpStatus.CONFLICT.value(), LocalDateTime.now());
        return new ResponseEntity<>(errorDTO, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({
            CompilationNotFoundException.class,
            MediaNotFoundException.class,
            FavoriteNotFoundException.class,
            TagNotFoundException.class,
            BadgeNotFoundException.class,
            MediaBadgeNotFoundException.class
    })
    public ResponseEntity<ErrorDTO> handleNotFoundException(RuntimeException exception) {
        ErrorDTO errorDTO = new ErrorDTO(exception.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDTO> handleIllegalArgumentException(RuntimeException exception) {
        ErrorDTO errorDTO = new ErrorDTO(exception.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TagAlreadyExistsException.class)
    public ResponseEntity<ErrorDTO> handleAlreadyExistsException(RuntimeException exception) {
        ErrorDTO errorDTO = new ErrorDTO(exception.getMessage(), HttpStatus.CONFLICT.value(), LocalDateTime.now());
        return new ResponseEntity<>(errorDTO, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorDTO> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        ErrorDTO errorDTO = new ErrorDTO(
                exception.getMessage(), HttpStatus.METHOD_NOT_ALLOWED.value(), LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDTO, HttpStatus.METHOD_NOT_ALLOWED);
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

    @ExceptionHandler(InvalidResponseFormatException.class)
    public ResponseEntity<ErrorDTO> handleAPIExceptions(InvalidResponseFormatException exception) {
        return new ResponseEntity<>(new ErrorDTO(exception.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }
}
