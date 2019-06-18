package com.accelya.services;

import com.accelya.model.AirwayBillDTO;
import com.accelya.model.BaseDTO;
import com.accelya.model.subscription.LOSubscriptionRequest;
import com.accelya.repository.LORepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class LOService<T> {

    private static final Logger logger = LoggerFactory.getLogger(LOService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LORepository repository;


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

    public void publish(BaseDTO<AirwayBillDTO> airwayBillLO) {

        subscriptionService.getSubscriptionsByType(airwayBillLO.getType())
                .forEach(s -> {
                    LOSubscriptionRequest<BaseDTO<AirwayBillDTO>> obj = new LOSubscriptionRequest<>(airwayBillLO, s.getKey());
                    HttpEntity<LOSubscriptionRequest<BaseDTO<AirwayBillDTO>>> request = new HttpEntity<>(obj);
                    restTemplate.postForObject(s.getSubscriptionEndpoint(), request, AirwayBillDTO.class);
                    logger.info(" Sending the LO to " + s.getSubscriptionEndpoint());
                });
    }
}
