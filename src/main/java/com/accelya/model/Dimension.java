package com.accelya.model;

import lombok.Data;

@Data
public class Dimension {
    private String length;
    private String width;
    private String height;
    private Unit unit;
}
