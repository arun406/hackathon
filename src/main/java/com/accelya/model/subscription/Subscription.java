package com.accelya.model.subscription;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Calendar;

@Data
@ToString
@Document
public class Subscription {

    @Id
    private String id;
    private String subscriptionEndpoint;
    private String key;
    private String documentType;
    private String companyId;
    private boolean active = true;
    private Calendar createdAt;
    private Calendar updatedAt;

}
