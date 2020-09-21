package ru.knastnt.veeroute_test;


import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.knastnt.veeroute_test.exceptions.NotFoundException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Order(Ordered.HIGHEST_PRECEDENCE + 100)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity noFoundErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        return ResponseEntity.badRequest().build();
    }

}
