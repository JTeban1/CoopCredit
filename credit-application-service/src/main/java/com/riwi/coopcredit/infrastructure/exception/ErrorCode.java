package com.riwi.coopcredit.infrastructure.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    AFFILIATE_NOT_FOUND(HttpStatus.NOT_FOUND),
    AFFILIATE_ALREADY_EXISTS(HttpStatus.CONFLICT),
    AFFILIATE_NOT_ACTIVE(HttpStatus.BAD_REQUEST),
    CREDIT_APPLICATION_NOT_FOUND(HttpStatus.NOT_FOUND),
    INSUFFICIENT_SALARY(HttpStatus.BAD_REQUEST),
    INVALID_CREDIT_AMOUNT(HttpStatus.BAD_REQUEST),
    INVALID_TERM(HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN),
    RISK_EVALUATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT);

    private final HttpStatus status;

    ErrorCode(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
