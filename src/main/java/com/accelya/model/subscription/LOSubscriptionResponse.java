package com.accelya.model.subscription;

import lombok.Data;

@Data
public class LOSubscriptionResponse {

    private String message;

    public LOSubscriptionResponse(String message) {
        this.message = message;
    }
}
