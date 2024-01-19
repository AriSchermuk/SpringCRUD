package io.github.aschermuk.springcrud.error;


import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
@Log4j2
public class ErrorController {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorDTO> handleNoHandlerFound(NoHandlerFoundException e) {
        log.error("Not found: ", e);
        return new ResponseEntity<>(new ErrorDTO("Deeplink doesn't exist. " + e.getLocalizedMessage(), NOT_FOUND.value()), NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNoHandlerFound(EntityNotFoundException e) {
        log.error("Not found: ", e);
        return new ResponseEntity<>(new ErrorDTO("Entity doesn't exist. " + e.getLocalizedMessage(), NOT_FOUND.value()), NOT_FOUND);
    }

    @ExceptionHandler({IllegalArgumentException.class, ValidationException.class})
    public ResponseEntity<ErrorDTO> handleBadRequest(Exception e) {
        log.error("Bad request: ", e);
        return new ResponseEntity<>(new ErrorDTO("Invalid input data. " + e.getLocalizedMessage(), BAD_REQUEST.value()), BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleAll(Exception e) {
        log.error("Internal Server Error: ", e);
        return new ResponseEntity<>(new ErrorDTO("Internal error. Contact a developer." + e.getLocalizedMessage(), INTERNAL_SERVER_ERROR.value()), INTERNAL_SERVER_ERROR);
    }

}
