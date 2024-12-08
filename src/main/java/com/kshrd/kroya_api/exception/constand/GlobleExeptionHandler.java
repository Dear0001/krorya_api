package com.kshrd.kroya_api.exception.constand;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

import static java.net.URI.create;

@ControllerAdvice
public class GlobleExeptionHandler {


    //Handle exception blank field
    @ExceptionHandler(FieldBlankExceptionHandler.class)
    ProblemDetail handleBlankField(FieldBlankExceptionHandler e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setProperty("time", LocalDateTime.now());
        return problemDetail;
    }


    // Handle exception not found
    @ExceptionHandler(NotFoundExceptionHandler.class)
    ProblemDetail handleNotFound(NotFoundExceptionHandler e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setType(create("localhost:8080/errors/not-found"));
        problemDetail.setTitle("Not Found");
        problemDetail.setProperty("Timestamp", LocalDateTime.now());
        return problemDetail;
    }
    // Handle Exception UnauthorizedException

    @ExceptionHandler(UnauthorizedException.class)
    ProblemDetail handleUnauthorizedException(UnauthorizedException unauthorizedException) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), unauthorizedException.getMessage());
        problemDetail.setType(create("localhost:8080/error/unauthorized"));
        return problemDetail;
    }

    @ExceptionHandler(PaymentRequired.class)
    ProblemDetail handlePaymentRequired(PaymentRequired paymentRequired) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.PAYMENT_REQUIRED, paymentRequired.getMessage());
        problemDetail.setTitle("Error Not Found!");
        problemDetail.setType(create("localhost:8080/error/paymentRequired"));
        return problemDetail;
    }

    @ExceptionHandler(LockedException.class)
    ProblemDetail handleLockException(LockedException lockedException) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(404), lockedException.getMessage());
        problemDetail.setTitle("Banned Account");
        problemDetail.setType(create("localhost:8080/error/lockedException"));
        return problemDetail;
    }

    @ExceptionHandler(InternalServerExeptionHandler.class)
    ProblemDetail handleInsernalServer(InternalServerExeptionHandler internalServerExeptionHandler) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, internalServerExeptionHandler.getMessage());
        problemDetail.setTitle("Internal Server Error!");
        problemDetail.setType(create("localhost:8080/error/internalServerError"));
        return problemDetail;
    }
}
