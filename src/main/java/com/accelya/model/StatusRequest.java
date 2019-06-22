package com.accelya.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StatusRequest {


    private String key;
    private String companyId;
    private List<String> parties;
    private StatusMessage message;

}
