package com.accelya.controller;

import com.accelya.model.AirwayBillDTO;
import com.accelya.model.NotificationRequest;
import com.accelya.model.StatusMessage;
import com.accelya.model.StatusRequest;
import com.accelya.services.LOService;
import com.accelya.services.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    private Map<String, List<AirwayBillDTO>> loStore = new HashMap<>();
    private Map<String, List<StatusMessage>> statusUpdateStore = new HashMap<>();

    @Autowired
    private SubscriptionService subscriptionsService;

    @Autowired
    private LOService<AirwayBillDTO> loService;

    /**
     * Will receive the LO from Airline
     */

    @PostMapping("/{companyId}/los")
    @ResponseBody
    public void storeLogisticeObjectsFromParties(@PathVariable String companyId, @RequestBody NotificationRequest message) {

        logger.info(" Incoming Message: {} , companyId {} ", message, companyId);

        if (!loStore.containsKey(companyId)) {
            List messages = new ArrayList();
            messages.add(message.getLo());
            loStore.put(companyId, messages);
        } else {
            List messages = loStore.get(companyId);
            messages.add(message.getLo());
            loStore.put(companyId, messages);
        }

        // Notified Parties.

        logger.info(" Allowed Parties...");
        message.getParties().forEach(s -> {
            this.logger.info(" Party Company Id: " + s);
            this.subscriptionsService.getAll(s).forEach(subscription -> {
                this.logger.info(" Subscription End point:  {}, Key :  {}",
                        subscription.getSubscriptionEndpoint(), subscription.getKey());
                    this.loService.publishLOToParty(message.getLo(), subscription.getSubscriptionEndpoint(), subscription.getKey());
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


    @PostMapping("/{companyId}/los/{loId}")
    @ResponseBody
    public void storeLogisticStatus(@PathVariable String companyId, @PathVariable String loId, StatusRequest message) {
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
    }


}
