package com.kshrd.kroya_api.exception;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", statusCode.value());
        body.put("error", ex.getFieldError().getDefaultMessage());
        return new ResponseEntity<>(body, statusCode);
    }

    @ExceptionHandler(FieldEmptyExceptionHandler.class)
    public ResponseEntity<ProblemDetail> handleFieldEmptyException(FieldEmptyExceptionHandler exceptionHandler) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exceptionHandler.getMessage());
        problemDetail.setTitle("Field Is Empty Exception");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setType(URI.create("localhost:8080/errors/bad-request"));
        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundExceptionHandler.class)
    public ResponseEntity<ProblemDetail> handleNotFoundException(NotFoundExceptionHandler exceptionHandler) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exceptionHandler.getMessage());
        problemDetail.setTitle("Not Found Exception");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setType(URI.create("localhost:8080/errors/not-found"));
        return new ResponseEntity<>(problemDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateFieldExceptionHandler.class)
    public ResponseEntity<ProblemDetail> handleDuplicationException(DuplicateFieldExceptionHandler exceptionHandler) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exceptionHandler.getMessage());
        problemDetail.setTitle("Field Duplicate Exception");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setType(URI.create("localhost:8080/errors/field-duplication"));
        return new ResponseEntity<>(problemDetail, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserDuplicateExceptionHandler.class)
    public ResponseEntity<ProblemDetail> handleUserDuplicationException(UserDuplicateExceptionHandler exceptionHandler) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exceptionHandler.getMessage());
        problemDetail.setTitle("User Duplicate Exception");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setType(URI.create("localhost:8080/errors/user-duplication"));
        return new ResponseEntity<>(problemDetail, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidValueExceptionHandler.class)
    public ResponseEntity<ProblemDetail> handleInvalidException(InvalidValueExceptionHandler exceptionHandler) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exceptionHandler.getMessage());
        problemDetail.setTitle("Invalid Exception");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setType(URI.create("localhost:8080/errors/invalid-value"));
        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }

    // Handle exception BadRequest
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ProblemDetail> handleBadRequest(BadRequestException badRequestException) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, badRequestException.getMessage());
        problemDetail.setTitle("Bad Request Exception");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setType(URI.create("localhost:8080/errors/bad-request"));
        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }

    // Handle exception Forbidden
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ProblemDetail> handleForbidden(ForbiddenException forbiddenException) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, forbiddenException.getMessage());
        problemDetail.setTitle("Forbidden Exception");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setType(URI.create("localhost:8080/errors/forbidden"));
        return new ResponseEntity<>(problemDetail, HttpStatus.FORBIDDEN);
    }

    // Handle IllegalStateException
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ProblemDetail> handleIllegalStateException(IllegalStateException illegalStateException) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, illegalStateException.getMessage());
        problemDetail.setTitle("Illegal State Exception");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setType(URI.create("localhost:8080/errors/illegal-state"));
        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }


    // Handle Invalid Date Format
    @ExceptionHandler(InvalidDateFormatException.class)
    public ResponseEntity<ProblemDetail> handleInvalidDateFormatException(InvalidDateFormatException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problemDetail.setTitle("Invalid Date Format Exception");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setType(URI.create("localhost:8080/errors/invalid-date-format"));
        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }

    // Handle Cannot Select Future Date
    @ExceptionHandler(FutureDateException.class)
    public ResponseEntity<ProblemDetail> handleFutureDateException(FutureDateException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problemDetail.setTitle("Future Date Exception");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setType(URI.create("localhost:8080/errors/future-date"));
        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }

}