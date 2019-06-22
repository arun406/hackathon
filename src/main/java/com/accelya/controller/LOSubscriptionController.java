package com.accelya.controller;


import com.accelya.model.AirwayBillDTO;
import com.accelya.model.BaseDTO;
import com.accelya.model.subscription.LOSubscriptionRequest;
import com.accelya.model.subscription.LOSubscriptionResponse;
import com.accelya.services.LOService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class LOSubscriptionController {

    private static final Logger logger = LoggerFactory.getLogger(LOSubscriptionController.class);
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${app.subscriptionKey}")
    private String subscriptionKey;

    @Autowired
    private LOService<AirwayBillDTO> service;

    @PostMapping("/mySubscriptions")
    @ResponseBody
    public ResponseEntity<?> receiveLO(@RequestBody String subscribedLORequest) {

        JsonNode node = null;

        try {
            node = objectMapper.readTree(subscribedLORequest);
            String subscriptionKeyInRequest = (node.get("subscriptionKey") != null && node.get("subscriptionKey").textValue() != null)
                    ? node.get("subscriptionKey").textValue()
                    : null;

            if (subscriptionKeyInRequest == null || !subscriptionKeyInRequest.equals(subscriptionKey)) {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }

            LOSubscriptionRequest<BaseDTO<AirwayBillDTO>> airwayBillLO = objectMapper
                    .readValue(subscribedLORequest, new TypeReference<LOSubscriptionRequest<BaseDTO<AirwayBillDTO>>>() {
                    });

            logger.info(" Receive LO: " + airwayBillLO.getLo());

            // LO will be saved to GHA database. And flow completes Here.
            service.createLO(airwayBillLO.getLo());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(new LOSubscriptionResponse("Logistics object notification successful!"), HttpStatus.OK);
    }

}
