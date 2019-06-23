package com.accelya.model;

import lombok.Data;

@Data
public class Flight {
    private Airport from;
    private Airport to;
    private String date;
    private String carrier;
    private String number;
}
