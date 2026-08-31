package com.bootcamp.persona.domain.exception;

public class InvalidPersonDataException extends RuntimeException {
    private final DomainErrorCode code;
    public InvalidPersonDataException(DomainErrorCode code) { super(code.getMessage()); this.code = code; }
    public DomainErrorCode getCode() { return code; }
}
