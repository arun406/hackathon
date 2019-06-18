package com.accelya.services;

import com.accelya.model.subscription.Subscription;
import com.accelya.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {
    @Autowired
    private SubscriptionRepository repository;

    public Subscription create(String companyId, Subscription subscription) {
        subscription.setCompanyId(companyId);
        subscription.setCreatedAt(new GregorianCalendar());
        subscription.setActive(true);
        return this.repository.save(subscription);
    }

    public Subscription update(String companyId, Subscription subscription, String id) {
        subscription.setCompanyId(companyId);
        subscription.setId(id);
        subscription.setUpdatedAt(new GregorianCalendar());
        Optional<Subscription> optional = repository.findByCompanyIdAndId(companyId, id);

        if (optional.isPresent()) {
            subscription.setId(optional.get().getId());
        }
        return repository.save(subscription);
    }

    public void delete(String companyId) {
        this.repository.deleteByCompanyId(companyId);
    }

//    public Subscription get(String companyId, String id) {
//
//        Optional<Subscription> optional = this.repository.findById(id);
//        if (optional.isPresent()) {
//            return optional.get();
//        }
//        return null;
//    }


    public List<Subscription> getAll(String companyId) {
        return this.repository.findByCompanyId(companyId);
    }

    public List<Subscription> getSubscriptionsByType(String documentType) {
        return this.repository.findByDocumentType(documentType);
    }
}
