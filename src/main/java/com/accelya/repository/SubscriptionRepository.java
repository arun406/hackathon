package com.accelya.repository;

import com.accelya.model.subscription.Subscription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends MongoRepository<Subscription, String> {
    List<Subscription> findByCompanyId(String companyId);

    void deleteByCompanyId(String companyId);

    Optional<Subscription> findByCompanyIdAndId(String companyId, String id);

    List<Subscription> findByDocumentType(String documentType);
}
