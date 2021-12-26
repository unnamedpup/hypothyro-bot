package com.github.hypothyro.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class Patient {
    @Id
    private Long id;
    private String name;
    private Long dateOfBirthday;
    private Integer gender;
    private Integer weight;
    @Builder.Default
    private Boolean isPregnant = false;
    private Double pretreatment;
    private String pretreatmentDrug;
    private String operation;
    private Long dateOperation;
    private Double treatment;
    private String treatmentDrug;
    private String pathologyName;
    private Boolean canBePregnant;

    private Integer thsResult;
    private Long thsDate;

    @Builder.Default
    private Double upthslev = 4.0;

    @Builder.Default
    private Double lowthslev=0.35;

    @Builder.Default
    private Double checkinterval = 2.0;
}
