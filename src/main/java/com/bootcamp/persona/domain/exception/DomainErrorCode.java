package com.bootcamp.persona.domain.exception;

public enum DomainErrorCode {
    PERSON_REQUIRED("La persona es obligatoria"), NAME_REQUIRED("El nombre es obligatorio"),
    NAME_TOO_LONG("El nombre excede 100 caracteres"), EMAIL_REQUIRED("El email es obligatorio"),
    EMAIL_INVALID("El email no tiene un formato válido"), BOOTCAMP_ID_INVALID("El bootcampId debe ser positivo"),
    BOOTCAMP_NOT_FOUND("El bootcamp no existe"), MAX_BOOTCAMPS_REACHED("El email ya tiene el máximo de 5 bootcamps"),
    DUPLICATE_ENROLLMENT("La persona ya está inscrita en este bootcamp"), OVERLAPPING_ENROLLMENT("Las fechas de bootcamps se solapan"),
    BOOTCAMP_SERVICE_UNAVAILABLE("El servicio de bootcamp no está disponible"), PERSISTENCE_CONFLICT("La inscripción ya existe");
    private final String message;
    DomainErrorCode(String message) { this.message = message; }
    public String getCode() { return name(); }
    public String getMessage() { return message; }
}
