package com.accelya.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Calendar;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document
public class BaseDTO<T> {

    @Id
    @JsonProperty("_id")
    private String id;

    private T logisticsObject;

    private String type;

    private String url;

    private String companyId;
    private String loId;
    private Calendar createdAt;
    private Calendar updatedAT;
    @JsonProperty("__v")
    private int version;

    public BaseDTO(T logisticsObject, String type, String url, String companyId, String loId) {
        this.companyId = companyId;
        this.type = type;
        this.url = url;
        this.logisticsObject = logisticsObject;
        this.loId = loId;
    }

}
