package com.example.uloha2.exception;

import com.example.uloha2.response.ResponseFactory;
import com.example.uloha2.response.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final ResponseFactory response;

    public GlobalExceptionHandler(ResponseFactory response) {
        this.response = response;
    }

    // Validačné chyby
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseWrapper<Void>> handleValidation(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> Map.of(
                        "field", error.getField(),
                        "message", error.getDefaultMessage()
                ))
                .toList();

        return response.error(HttpStatus.BAD_REQUEST, "Validačné chyby", errors);
    }

    // Objekt nenájdený - genericky pre všetky entity
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ResponseWrapper<Void>> handleObjectNotFound(ObjectNotFoundException ex) {
        return response.error(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Email už existuje
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ResponseWrapper<Void>> handleEmailExists(EmailAlreadyExistsException ex) {
        List<Map<String, String>> errors = List.of(
                Map.of("field", "email", "message", ex.getMessage())
        );
        return response.error(HttpStatus.CONFLICT, "Email už existuje", errors);
    }

    // Nesprávny typ parametra, napr. /students/abc namiesto číselného ID
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseWrapper<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = "Parameter '" + ex.getName() + "' má neplatnú hodnotu";
        return response.error(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ResponseWrapper<Void>> handleNoResource(NoResourceFoundException ex) {
        return response.error(HttpStatus.NOT_FOUND, "Požadovaný endpoint neexistuje");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseWrapper<Void>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return response.error(HttpStatus.METHOD_NOT_ALLOWED, "HTTP metóda nie je podporovaná");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseWrapper<Void>> handleInvalidJson(HttpMessageNotReadableException ex) {
        return response.error(HttpStatus.BAD_REQUEST, "Neplatné JSON dáta");
    }

    // Ostatné chyby - posledna
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseWrapper<Void>> handleGeneral(Exception ex) {
        return response.error(HttpStatus.INTERNAL_SERVER_ERROR, "Nastala neočakávaná chyba");
    }
}