package com.accelya.model;

import lombok.Data;

@Data
public class Pet {
    private String type;
    private String name;
    private String bread;
    private Age age;
    private Value weight;
}
