package com.accelya.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StatusMessage {

    private String loId;
    private String statusCode;
    private String message;
    private List<String> attachmentURI;
    private String timestamp;
}
