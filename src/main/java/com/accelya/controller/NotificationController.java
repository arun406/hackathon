package com.accelya.controller;

import com.accelya.model.AirwayBillDTO;
import com.accelya.model.BaseDTO;
import com.accelya.model.NotificationRequest;
import com.accelya.model.NotificationLO;
import com.accelya.services.LOService;
import com.accelya.services.SubscriptionService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    private Map<String, List<AirwayBillDTO>> loStore = new HashMap<>();
    private Map<String, List<NotificationLO>> statusUpdateStore = new HashMap<>();

    @Autowired
    private SubscriptionService subscriptionsService;

    @Autowired
    private LOService<AirwayBillDTO> loService;

    @Autowired
    private ObjectMapper objectMapper;


    /**
     * Will receive the LO from Airline
     */

    @PostMapping("/{companyId}/los")
    @ResponseBody
    public void storeLogisticeObjectsFromParties(@PathVariable String companyId, @RequestBody String message) throws IOException {

        logger.info(" Incoming Message: {} , companyId {} ", message, companyId);
        JsonNode node = objectMapper.readTree(message);

        String subscriptionKeyInRequest = (node.get("subscriptionKey") != null && node.get("subscriptionKey").textValue() != null)
                ? node.get("subscriptionKey").textValue()
                : null;
        String loType ;
        JsonNode loTypeNode = node.get("lo").get("type");
        if(loTypeNode != null){
            loType = loTypeNode.textValue();
        }else {
            loType = node.get("lo").get("@type").textValue();
        }

//        String loType = (loNode.get("@type") != null && loNode.get("@type").textValue() != null)
//                ? loNode.get("@type").textValue()
//                : null;
        NotificationRequest<?> request = null;

        if ("AirwayBill".equalsIgnoreCase(loType)) {

            request = objectMapper
                    .readValue(message, new TypeReference<NotificationRequest<BaseDTO<AirwayBillDTO>>>() {
                    });


        } else if ("Notification".equalsIgnoreCase(loType)) {
            request = objectMapper
                    .readValue(message, new TypeReference<NotificationRequest<NotificationLO>>() {
                    });

            logger.info(" Received LO Notification : " + request);

        }
        // Get the
//        if (!loStore.containsKey(companyId)) {
//            List messages = new ArrayList();
//            messages.add(request.getLo());
//            loStore.put(companyId, messages);
//        } else {
//            List messages = loStore.get(companyId);
//            messages.add(request.getLo());
//            loStore.put(companyId, messages);
//        }

        // Notified Parties.

        logger.info(" Allowed Parties...");
        NotificationRequest<?> finalRequest = request;

        request.getParties().forEach(partyId -> {
            this.logger.info(" Party Company Id: " + partyId);
            this.subscriptionsService.getAll(partyId).forEach(subscription -> {
                this.logger.info(" Subscription End point:  {}, Key :  {}",
                        subscription.getSubscriptionEndpoint(), subscription.getKey());

                List<String> parties = new ArrayList<>();
                parties.add(partyId);

                NotificationRequest nr = new NotificationRequest(subscription.getKey(), parties, finalRequest.getLo());
                this.loService.publishLOToParty(nr,subscription.getSubscriptionEndpoint());
            });
        });

        logger.info(" Messages inserted to Store.... ");
    }


    @GetMapping("/{companyId}/los")
    @ResponseBody
    public List<AirwayBillDTO> fetchLogisticObjects(@PathVariable String companyId) {
        logger.info("Company Id {}", companyId);
        logger.info(" Store Size {}", loStore.size());

        return loStore.get(companyId);
    }

    @GetMapping("/{companyId}/los/{loId}")
    @ResponseBody
    public AirwayBillDTO fetchLogisticObject(@PathVariable String companyId, @PathVariable String loId) {
        logger.info("Company Id {}", companyId);
        logger.info(" Store Size {}", loStore.size());
        Optional<AirwayBillDTO> optional = loStore.get(companyId).stream().filter(lo -> lo.getId().equals(loId)).findFirst();

        if (optional.isPresent()) {
            logger.info(" LO is present returning.....");
            return optional.get();
        }
        return null;
    }


    /*@PostMapping("/{companyId}/los/{loId}")
    @ResponseBody
    public void storeLogisticStatus(@PathVariable String companyId, @PathVariable String
            loId, LONotificationRequest message) {
        logger.info(" Incoming Message: " + message);
        String key = companyId + "#" + loId;


        if (!statusUpdateStore.containsKey(key)) {
            List statuses = new ArrayList();
            statuses.add(message.getMessage());
            statusUpdateStore.put(key, statuses);
        } else {
            List statuses = loStore.get(companyId);
            statuses.add(message.getMessage());
            statusUpdateStore.put(key, statuses);
        }

        // Notified Parties.
        logger.info(" Allowed Parties...");
        message.getParties().forEach(s -> logger.info(s));


        logger.info(" StatusMessages inserted to Store.... ");
    }*/


}
