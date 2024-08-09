package com.intela.zimcredai.ErrorHandling;

import com.fasterxml.jackson.core.JsonParseException;
import com.intela.zimcredai.Exception.ResourceNotFoundException;
import com.intela.zimcredai.RequestResponseModels.ApiResponse;
import com.intela.zimcredai.RequestResponseModels.ErrorDetail;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(ResourceNotFoundException exception) {
        logger.error("Resource not found: {}", exception.getMessage());
        ErrorDetail errorDetail = new ErrorDetail(
                "Resource Not Found",
                List.of(exception.getMessage()),  // Wrap message in a list
                HttpStatus.NOT_FOUND.value()
        );
        ApiResponse<List<String>> errorResponse = new ApiResponse<>(errorDetail, null, null);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        logger.error("Username not found: {}", exception.getMessage());
        ErrorDetail errorDetail = new ErrorDetail(
                "User Not Found",
                List.of(exception.getMessage()),  // Wrap message in a list
                HttpStatus.NOT_FOUND.value()
        );
        ApiResponse<List<String>> errorResponse = new ApiResponse<>(errorDetail, null, null);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntimeException(RuntimeException exception) {
        logger.error("Runtime exception: {}", exception.getMessage());
        ErrorDetail errorDetail = new ErrorDetail(
                "Internal Server Error",
                List.of(exception.getMessage()),  // Wrap message in a list
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        ApiResponse<List<String>> errorResponse = new ApiResponse<>(errorDetail, null, null);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Validation handling
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        logger.error("Validation error: {}", exception.getMessage());

        // Collect validation error messages into a list
        List<String> errorMessages = exception.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        // Create an error detail with a list of error messages
        ErrorDetail errorDetail = new ErrorDetail(
                "Validation Error",
                errorMessages,  // Use the list of error messages
                HttpStatus.BAD_REQUEST.value()
        );

        // Create an ApiResponse with the ErrorDetail
        ApiResponse<List<String>> errorResponse = new ApiResponse<>(errorDetail, null, null);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolationException(ConstraintViolationException exception) {
        logger.error("Constraint violation: {}", exception.getMessage());

        // Collect constraint violation messages into a list
        List<String> errorMessages = exception.getConstraintViolations().stream()
                .map(violation -> violation.getMessage())
                .collect(Collectors.toList());

        // Create an error detail with a list of error messages
        ErrorDetail errorDetail = new ErrorDetail(
                "Constraint Violation",
                errorMessages,  // Use the list of error messages
                HttpStatus.BAD_REQUEST.value()
        );

        // Create an ApiResponse with the ErrorDetail
        ApiResponse<List<String>> errorResponse = new ApiResponse<>(errorDetail, null, null);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<ApiResponse<?>> handleJsonParseException(JsonParseException exception) {
        logger.error("JSON parse error: {}", exception.getMessage());

        ErrorDetail errorDetail = new ErrorDetail(
                "Invalid Input",
                List.of("Cannot parse JSON input: " + exception.getMessage()),  // Convert message to a single-element list
                HttpStatus.BAD_REQUEST.value()
        );
        ApiResponse<Object> errorResponse = new ApiResponse<>(errorDetail, null, null);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}

