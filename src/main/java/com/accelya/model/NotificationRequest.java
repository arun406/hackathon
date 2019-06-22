package com.accelya.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@ToString
public class NotificationRequest<T> {

    @JsonProperty("subscriptionKey")
    private String key;
    private List<String> parties; // companyId who can access this object
    private T lo;   // can be a AirwayBillDTO or NotificationLO logistic objects/

    public NotificationRequest(String key, List<String> parties, T lo) {
        this.key = key;
        this.parties = parties;
        this.lo = lo;
    }

}
