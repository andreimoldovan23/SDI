package controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    private static final String badRequest = "BAD REQUEST";

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleAppBadRequest(Exception e) {
        log.trace("Error while processing request data: {}", e.getMessage());
        return new ResponseEntity<>(badRequest, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleAppNotFound(Exception e) {
        log.trace("Error while searching user: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

}
