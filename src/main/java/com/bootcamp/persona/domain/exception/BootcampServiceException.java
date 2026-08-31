package com.bootcamp.persona.domain.exception;

public class BootcampServiceException extends RuntimeException {
    private final DomainErrorCode code;
    public BootcampServiceException(DomainErrorCode code, Throwable cause) { super(code.getMessage(), cause); this.code = code; }
    public BootcampServiceException(DomainErrorCode code) { super(code.getMessage()); this.code = code; }
    public DomainErrorCode getCode() { return code; }
}
