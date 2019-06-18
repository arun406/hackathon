package com.accelya.repository;

import com.accelya.model.company.Company;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends MongoRepository<Company, String> {

    Optional<Company> findByCompanyId(String companyId);

    void deleteByCompanyId(String companyId);

}
