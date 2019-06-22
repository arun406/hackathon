package com.accelya.repository;

import com.accelya.model.NotificationLO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationLORepository extends MongoRepository<NotificationLO, String>{

    List<NotificationLO> findByLoId(String notificationLOId);
}
