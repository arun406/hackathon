package com.accelya.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class InDestinationCurrency {
    private String TotalCharges;

    private String CollectionCharge;

    private String GrandTotalCollectCharges;

}
