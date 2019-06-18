package com.accelya.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.ToString;

@Data
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
@ToString
public class Prepaid {
    private String TotalWeightCharges;

    private String TotalValuationCharges;

    private String TotalTaxes;

    private String TotalOtherChargesDueAgent;

    private String TotalOtherChargesDueCarrier;

    private String TotalPrepaidCharges;

}
