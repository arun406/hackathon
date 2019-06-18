package com.accelya.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OtherCharge {

    private ChargeDescription ChargeDescription;
    private SpecialService SpecialService;
    private ChargePayment ChargePayment;

}
