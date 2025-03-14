package com.mortgages.testassignment.exception;

import lombok.Getter;

@Getter
public enum ErrorDetail {
    UNKNOWN_ERROR(100, "unknown error"),
    INVALID_REQUEST(101, "invalid request"),
    MATURITY_PERIOD_NOT_FOUND(102, "maturity period not found");

    private final int errorCode;
    private final String message;

    ErrorDetail(int code, String message) {
        this.errorCode = code;
        this.message = message;
    }
}
