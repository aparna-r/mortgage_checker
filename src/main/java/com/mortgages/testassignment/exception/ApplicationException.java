package com.mortgages.testassignment.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private final ErrorDetail errorCode;

    public ApplicationException(ErrorDetail errorCode) {
        this(errorCode, "");
    }

    public ApplicationException(ErrorDetail errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }
}
