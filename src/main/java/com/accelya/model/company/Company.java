package com.accelya.model.company;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@Document
public class Company {

    @Id
    private String id;
    private String contactName;
    private String contactEmail;
    private String companyType;
    private String companyName;
    private String companyId;
    private String companyEndpoint;
    private String companyImage;
    private String companyDescription;
    private String companyPin;
    private boolean active;

}
