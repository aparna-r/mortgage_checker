package com.mortgages.testassignment.exception;


import com.mortgages.testassignment.exception.ApplicationException.MaturityPeriodNotFoundException;
import com.mortgages.testassignment.model.RequestResponseModel.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("unknown error!!", ex);
        return ResponseEntity.internalServerError().body(new ErrorResponse(ErrorDetail.UNKNOWN_ERROR));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException ex) {
        log.debug("method argument invalid!!", ex);
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(new ErrorResponse(
                ErrorDetail.INVALID_REQUEST.getErrorCode(),
                String.format("%s: %s", ErrorDetail.INVALID_REQUEST.getMessage(), errors)));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(MaturityPeriodNotFoundException ex) {
        log.debug(ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getErrorCode()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(ApplicationException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.internalServerError().body(new ErrorResponse(ex.getErrorCode()));
    }
}
