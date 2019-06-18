package com.accelya.model;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class ChargePayment {

    private String ByTime;
    private String ByItemQuantity;
    private String Amount;
    private String DueParty;
    private String PrepaidCollectIndicator;

}
