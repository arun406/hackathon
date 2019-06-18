package com.accelya.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class ChargeDeclaration {

    private String WeightValuationChargePaymen;
    private String OtherChargesPayment;
    private Carriage Carriage;
    private Customs Customs;
    private Insurance Insurance;
}
