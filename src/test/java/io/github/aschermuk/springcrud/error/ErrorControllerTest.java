package io.github.aschermuk.springcrud.error;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.NoHandlerFoundException;

class ErrorControllerTest {

    private final ErrorController errorController = new ErrorController();

    @Test
    void handleNoHandlerFoundException() {
        NoHandlerFoundException exception = new NoHandlerFoundException("GET", "/api/resource", null);

        ResponseEntity<ErrorDTO> responseEntity = errorController.handleNoHandlerFound(exception);

        assertEquals(NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Deeplink doesn't exist. " + exception.getLocalizedMessage(), requireNonNull(responseEntity.getBody()).getMessage());
        assertEquals(NOT_FOUND.value(), responseEntity.getStatusCode().value());
    }

    @Test
    void handleEntityNotFoundException() {
        EntityNotFoundException exception = new EntityNotFoundException("Entity not found");

        ResponseEntity<ErrorDTO> responseEntity = errorController.handleNoHandlerFound(exception);

        assertEquals(NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Entity doesn't exist. " + exception.getLocalizedMessage(), requireNonNull(responseEntity.getBody()).getMessage());
        assertEquals(NOT_FOUND.value(),  responseEntity.getStatusCode().value());
    }

    @Test
    void handleBadRequestException() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid input");

        ResponseEntity<ErrorDTO> responseEntity = errorController.handleBadRequest(exception);

        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid input data. " + exception.getLocalizedMessage(), requireNonNull(responseEntity.getBody()).getMessage());
        assertEquals(BAD_REQUEST.value(),  responseEntity.getStatusCode().value());
    }

    @Test
    void handleValidationException() {
        ValidationException exception = new ValidationException("Validation failed");

        ResponseEntity<ErrorDTO> responseEntity = errorController.handleBadRequest(exception);

        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid input data. " + exception.getLocalizedMessage(), requireNonNull(responseEntity.getBody()).getMessage());
        assertEquals(BAD_REQUEST.value(),  responseEntity.getStatusCode().value());
    }

    @Test
    void handleAllExceptions() {
        Exception exception = new Exception("Internal Server Error");

        ResponseEntity<ErrorDTO> responseEntity = errorController.handleAll(exception);

        assertEquals(INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Internal error. Contact a developer." + exception.getLocalizedMessage(), requireNonNull(responseEntity.getBody()).getMessage());
        assertEquals(INTERNAL_SERVER_ERROR.value(),  responseEntity.getStatusCode().value());
    }
}

