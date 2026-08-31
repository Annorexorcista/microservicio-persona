package com.bootcamp.persona.infrastructure.adapters.driven.r2dbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDate;

@Table("person_bootcamp")
public class PersonBootcampEntity {
    @Id private Long id;
    private Long personId;
    private Long bootcampId;
    private LocalDate launchDate;
    private Integer durationDays;
    private LocalDate enrolledAt;
    public PersonBootcampEntity() {}
    public PersonBootcampEntity(Long id, Long personId, Long bootcampId, LocalDate launchDate, Integer durationDays, LocalDate enrolledAt) { this.id=id;this.personId=personId;this.bootcampId=bootcampId;this.launchDate=launchDate;this.durationDays=durationDays;this.enrolledAt=enrolledAt; }
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public Long getPersonId(){return personId;} public void setPersonId(Long v){personId=v;}
    public Long getBootcampId(){return bootcampId;} public void setBootcampId(Long v){bootcampId=v;}
    public LocalDate getLaunchDate(){return launchDate;} public void setLaunchDate(LocalDate v){launchDate=v;}
    public Integer getDurationDays(){return durationDays;} public void setDurationDays(Integer v){durationDays=v;}
    public LocalDate getEnrolledAt(){return enrolledAt;} public void setEnrolledAt(LocalDate v){enrolledAt=v;}
}
