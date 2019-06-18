package com.accelya.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class Collect {
    private String TotalWeightCharges;

    private String TotalValuationCharges;

    private String TotalTaxes;

    private String TotalOtherChargesDueAgent;

    private String TotalOtherChargesDueCarrier;

    private String TotalCollectCharges;

    private InDestinationCurrency InDestinationCurrency;
}
