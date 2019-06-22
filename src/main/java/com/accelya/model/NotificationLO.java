package com.accelya.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NotificationLO {

    @JsonProperty("@context")
    private Context context;

    @JsonProperty("@id")
    private String id;

    @JsonProperty("@type")
    private String type;

    @JsonProperty("@url")
    private String url;


    private String code;
    private String loId;
    private String text;
    private List<String> documents = new ArrayList<>();
    private String timestamp;

}
