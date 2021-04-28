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

    private static final String BAD_REQUEST = "We couldn't process the data you sent us";
    private static final String INTERNAL_ERROR = "The server encountered a problem";

    @ExceptionHandler({HttpMessageNotReadableException.class,
            DateTimeParseException.class, DateTimeException.class})
    public ResponseEntity<Object> handleAppBadRequest() {
        return new ResponseEntity<>(BAD_REQUEST, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DataAccessException.class, ValidatorException.class, RuntimeException.class})
    public ResponseEntity<Object> handleAppInternalError() {
        return new ResponseEntity<>(INTERNAL_ERROR, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
