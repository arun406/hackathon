package com.accelya.repository;

import com.accelya.model.BaseDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LORepository extends MongoRepository<BaseDTO, String> {


    List<BaseDTO> findByCompanyId(String companyId);

    Optional<BaseDTO> findByLoId(String loId);

    void deleteByLoId(String loId);
}
