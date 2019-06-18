package com.accelya.model.subscription;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LOSubscriptionRequest<T> {

    private T lo;
    private String subscriptionKey;

    public LOSubscriptionRequest() {

    }

    public LOSubscriptionRequest(T lo, String subscriptionKey) {
        this.lo = lo;
        this.subscriptionKey = subscriptionKey;
    }
}
