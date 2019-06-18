package com.accelya.services;

import com.accelya.model.company.Company;
import com.accelya.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository repository;

    public Company create(Company company) {
        company.setActive(true);
        return this.repository.save(company);
    }

    public Company update(Company company, String companyId) {

        company.setCompanyId(companyId);
        Optional<Company> optional = repository.findByCompanyId(companyId);

        if (optional.isPresent()) {
            company.setId(optional.get().getId());
        }
        return repository.save(company);
    }

    public void delete(String companyId) {
        this.repository.deleteByCompanyId(companyId);
    }

    public Company get(String companyId) {

        Optional<Company> optional = this.repository.findByCompanyId(companyId);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }


    public List<Company> getAll() {
        return this.repository.findAll();
    }
}
