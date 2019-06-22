package com.accelya.services;

import com.accelya.model.AirwayBillDTO;
import com.accelya.model.BaseDTO;
import com.accelya.model.NotificationRequest;
import com.accelya.model.StatusMessage;
import com.accelya.model.subscription.LOSubscriptionRequest;
import com.accelya.repository.LORepository;
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
public class LOService<T> {

    private static final Logger logger = LoggerFactory.getLogger(LOService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LORepository repository;

    @Value("${app.hub.uri}")
    private String hubURI;

    @Value("${app.companyId}")
    private String companyId;

    @Value("${app.key}")
    private String key;

    @Value("${app.partyId}")
    private String partyId;

    @Autowired
    private SubscriptionService subscriptionService;

    /**
     * @param logisticObject
     * @return
     */
    public BaseDTO<T> createLO(BaseDTO<T> logisticObject) {
        return repository.save(logisticObject);
    }

    public List<BaseDTO> getLOs(String companyId) {
        return repository.findByCompanyId(companyId);
    }


    public BaseDTO getLO(String companyId, String loId) {
        Optional<BaseDTO> optional = repository.findByLoId(loId);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    public void deleteLO(String companyId, String loId) {
        repository.deleteByLoId(loId);
    }

    public BaseDTO<?> updateLO(BaseDTO<T> airwayBillLO, String loId) {

        airwayBillLO.setLoId(loId);
        Optional<BaseDTO> optional = repository.findByLoId(loId);
        if (optional.isPresent()) {
            airwayBillLO.setId(optional.get().getId());
        }
        return repository.save(airwayBillLO);
    }

    /**
     * @param airwayBillLO
     */
    public void publishLOToParty(BaseDTO<AirwayBillDTO> airwayBillLO) {

        subscriptionService.getSubscriptionsByType(airwayBillLO.getType())
                .forEach(s -> {
                    LOSubscriptionRequest<BaseDTO<AirwayBillDTO>> obj = new LOSubscriptionRequest<>(airwayBillLO, s.getKey(), "AirwayBill");
                    HttpEntity<LOSubscriptionRequest<BaseDTO<AirwayBillDTO>>> request = new HttpEntity<>(obj);
                    restTemplate.postForObject(s.getSubscriptionEndpoint(), request, AirwayBillDTO.class);
                    logger.info(" Sending the LO to " + s.getSubscriptionEndpoint());
                });
    }

    /**
     * @param airwayBillDTO
     */
    public void publishLOTOHub(BaseDTO<AirwayBillDTO> airwayBillDTO) {
        List<String> parties = new ArrayList<>();
        parties.add(partyId);

        NotificationRequest nr = new NotificationRequest(key, parties, airwayBillDTO);
        HttpEntity<NotificationRequest> request = new HttpEntity<>(nr);
        this.restTemplate.postForObject(hubURI + "/" + companyId + "/los", request, String.class);
        this.logger.info(" Message published to Logistic Server Hub ");
    }


    /**
     * @param companyId
     * @param loId
     * @param status
     */
    public void addStatusToLo(String companyId, String loId, StatusMessage status) {
        logger.info(" company Id {}, loId {}, status {}", companyId, loId, status);

        BaseDTO lo = getLO(companyId, loId);
        AirwayBillDTO airwayBillDTO = (AirwayBillDTO) lo.getLogisticsObject();

        logger.info(airwayBillDTO.getUrl());
        airwayBillDTO.getUpdates().add(status);

        lo.setLogisticsObject(airwayBillDTO);
        this.repository.save(lo);

    }

    /**
     * @param lo
     * @param subscriptionEndpoint
     * @param key
     */
    public void publishLOToParty(BaseDTO<AirwayBillDTO> lo, String subscriptionEndpoint, String key) {
        List<String> parties = new ArrayList<>();
        parties.add(partyId);
        NotificationRequest nr = new NotificationRequest(key, parties, lo);
        HttpEntity<NotificationRequest> request = new HttpEntity<>(nr);
        this.restTemplate.postForObject(subscriptionEndpoint, request, String.class);
        this.logger.info(" Message published to Logistic Server Hub ");

    }
}
