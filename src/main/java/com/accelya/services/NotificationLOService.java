package com.accelya.services;

import com.accelya.model.NotificationLO;
import com.accelya.model.NotificationRequest;
import com.accelya.repository.NotificationLORepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationLOService {

    private static final Logger logger = LoggerFactory.getLogger(LOService.class);
    @Value("${app.hub.uri}")
    private String hubURI;

    @Value("${app.companyId}")
    private String companyId;

    @Value("${app.subscriptionKey}")
    private String subscriptionKey;

    @Value("${app.partyId}")
    private String partyId;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NotificationLORepository repository;


    public NotificationLO createLO(NotificationLO lo) {
        return this.repository.save(lo);
    }

    public List<NotificationLO> getLOs(String loId) {
        return this.repository.findByLoId(loId);
    }

    public NotificationLO getLO(String notLoId) {
        Optional<NotificationLO> optional = this.repository.findById(notLoId);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    /**
     * @param lo
     */
    public void publishLOTOHub(NotificationLO lo) {
        List<String> parties = new ArrayList<>();
        parties.add(partyId);

        NotificationRequest<NotificationLO> nr = new NotificationRequest(subscriptionKey, parties, lo);
        HttpEntity<NotificationRequest> request = new HttpEntity<>(nr);
        this.restTemplate.postForObject(hubURI + "/" + companyId + "/los", request, String.class);
        this.logger.info(" Message published to Logistic Server Hub ");
    }
}
