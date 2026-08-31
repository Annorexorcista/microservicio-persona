package com.bootcamp.persona.domain.exception;

public class EnrollmentConflictException extends RuntimeException {
    private final DomainErrorCode code;
    public EnrollmentConflictException(DomainErrorCode code) { super(code.getMessage()); this.code = code; }
    public DomainErrorCode getCode() { return code; }
}
