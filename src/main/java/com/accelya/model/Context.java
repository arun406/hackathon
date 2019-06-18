package com.accelya.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Context {

    @JsonProperty("@vocab")
    private String vocab;

}
