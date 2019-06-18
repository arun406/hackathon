package com.accelya.controller;

import com.accelya.model.subscription.Subscription;
import com.accelya.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SubscriptionController {

    @Autowired
    private SubscriptionService service;

    @PostMapping("/companies/{companyId}/subscriptions")
    @ResponseBody
    public Subscription create(@PathVariable String companyId, @RequestBody Subscription subscription) {
        return this.service.create(companyId, subscription);
    }

    @GetMapping("/companies/{companyId}/subscriptions")
    @ResponseBody
    public List<Subscription> getAll(@PathVariable String companyId) {
        return this.service.getAll(companyId);
    }

    @PatchMapping("/companies/{companyId}/subscriptions/{subscriptionId}")
    @ResponseBody
    public Subscription update(@PathVariable String companyId, @RequestBody Subscription subscription, @PathVariable String subscriptionId) {
        return this.service.update(companyId, subscription, subscriptionId);
    }

    @DeleteMapping("/companies/{companyId}/subscriptions")
    @ResponseBody
    public void delete(@PathVariable String companyId) {
        this.service.delete(companyId);
    }


}
