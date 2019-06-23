package com.accelya.controller;


import com.accelya.model.AirwayBillDTO;
import com.accelya.model.BaseDTO;
import com.accelya.model.NotificationLO;
import com.accelya.model.NotificationRequest;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class MySubscriptionController {

    private static final Logger logger = LoggerFactory.getLogger(MySubscriptionController.class);
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

            String loType = (node.get("@type") != null && node.get("@type").textValue() != null)
                    ? node.get("@type").textValue()
                    : null;

            if(StringUtils.isEmpty(loType)) {
                if(node.get("lo") != null ){
                    loType =  node.get("lo").get("@type").textValue();
                }
            }

            if (subscriptionKeyInRequest == null || !subscriptionKeyInRequest.equals(subscriptionKey)) {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }

            if ("AirwayBill".equalsIgnoreCase(loType)) {

                NotificationRequest<BaseDTO<AirwayBillDTO>> airwayBillLO = objectMapper
                        .readValue(subscribedLORequest, new TypeReference<NotificationRequest<BaseDTO<AirwayBillDTO>>>() {
                        });

                logger.info(" Receive LO: " + airwayBillLO.getLo());
                // LO will be saved to GHA database. And flow completes Here.
                service.createLO(airwayBillLO.getLo());

            } else if ("Notification".equalsIgnoreCase(loType)) {
                NotificationRequest<NotificationLO> notificationLO = objectMapper
                        .readValue(subscribedLORequest, new TypeReference<NotificationRequest<NotificationLO>>() {
                        });

                logger.info(" Received LO Notification : " + notificationLO.getLo());
                // LO will be saved to GHA database. And flow completes Here.

                // Fetch the LO and Update the notification Information.
                BaseDTO<AirwayBillDTO> lo = this.service.getLO(null, notificationLO.getLo().getLoId());
                AirwayBillDTO airwayBillDTO = lo.getLogisticsObject();

                // Update the LO by adding new notification.
                airwayBillDTO.getUpdates().add(notificationLO.getLo());
                service.updateLO(lo, airwayBillDTO.getId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(new LOSubscriptionResponse("Logistics object notification successful!"), HttpStatus.OK);
    }

}
