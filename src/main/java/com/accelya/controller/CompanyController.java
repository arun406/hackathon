package com.accelya.controller;

import com.accelya.model.company.Company;
import com.accelya.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CompanyController {

    @Autowired
    private CompanyService service;

    @PostMapping("/companies")
    @ResponseBody
    public Company create(@RequestBody Company company) {
        return this.service.create(company);
    }


    @GetMapping("/companies/{companyId}")
    @ResponseBody
    public Company get(@PathVariable String companyId) {
        return this.service.get(companyId);
    }

    @PatchMapping("/companies/{companyId}")
    @ResponseBody
    public Company update(@RequestBody Company company, @PathVariable String companyId) {
        return this.service.update(company, companyId);
    }

    @DeleteMapping("/companies/{companyId}")
    @ResponseBody
    public void delete(@PathVariable String companyId) {
        this.service.delete(companyId);
    }

}
