package com.accelya.model.subscription;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class LOSubscriptionRequest<T> {

    private T lo;
    private String subscriptionKey;
    private String type;


    public LOSubscriptionRequest(T lo, String subscriptionKey, String type) {
        this.lo = lo;
        this.subscriptionKey = subscriptionKey;
        this.type = type;
    }
}
