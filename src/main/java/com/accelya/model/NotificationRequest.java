package com.accelya.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@ToString
public class NotificationRequest {

    @JsonProperty("subscriptionKey")
    private String key;
    private List<String> parties; // companyId
    private BaseDTO<AirwayBillDTO> lo;

    public NotificationRequest(String key, List<String> parties, BaseDTO<AirwayBillDTO> lo) {
        this.key = key;
        this.parties = parties;
        this.lo = lo;
    }
}
