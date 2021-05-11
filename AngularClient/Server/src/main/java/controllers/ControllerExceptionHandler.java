package controllers;

import domain.Validators.ValidatorException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.DateTimeException;
import java.time.format.DateTimeParseException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({HttpMessageNotReadableException.class,
            DateTimeParseException.class, DateTimeException.class})
    public ResponseEntity<Object> handleAppBadRequest(Exception e) {
        return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DataAccessException.class, ValidatorException.class, RuntimeException.class})
    public ResponseEntity<Object> handleAppInternalError(Exception e) {
        return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
