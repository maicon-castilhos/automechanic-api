package com.automechanic.api.resources.exceptions;

import java.time.Instant;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.automechanic.api.services.exceptions.BusinessException;
import com.automechanic.api.services.exceptions.DatabaseException;
import com.automechanic.api.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ResourceExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return error(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        return error(HttpStatus.NOT_FOUND, "Not Found", "Registro nao encontrado.", request);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, DatabaseException.class})
    public ResponseEntity<StandardError> database(Exception ex, HttpServletRequest request) {
        return error(HttpStatus.CONFLICT, "Conflito de dados",
                "A operacao viola uma restricao de dados. Verifique valores unicos e registros vinculados.", request);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<StandardError> business(BusinessException ex, HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST, "Regra de negocio", ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> validation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(field -> field.getField() + ": " + field.getDefaultMessage())
                .distinct().sorted().collect(Collectors.joining("; "));
        return error(HttpStatus.BAD_REQUEST, "Dados invalidos", message, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<StandardError> constraint(ConstraintViolationException ex, HttpServletRequest request) {
        String message = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .sorted().collect(Collectors.joining("; "));
        return error(HttpStatus.BAD_REQUEST, "Dados invalidos", message, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardError> unreadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST, "Dados invalidos",
                "Corpo JSON invalido. Verifique tipos, datas e valores enumerados.", request);
    }

    private ResponseEntity<StandardError> error(HttpStatus status, String title, String message,
            HttpServletRequest request) {
        StandardError body = new StandardError(Instant.now(), status.value(), title, message, request.getRequestURI());
        return ResponseEntity.status(status).body(body);
    }
}
