package com.accelya.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class CarrierSignature {

    private String SignatoryName;

    private String SignatoryCompany;

    private String Date;

    private String Signature;

    private String Location;
}
