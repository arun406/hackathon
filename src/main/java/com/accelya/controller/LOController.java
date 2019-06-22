package com.accelya.controller;


import com.accelya.model.AirwayBillDTO;
import com.accelya.model.BaseDTO;
import com.accelya.model.StatusMessage;
import com.accelya.services.LOService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

@RestController
public class LOController {

    private static final Logger logger = LoggerFactory.getLogger(LOController.class);

    public static final String AIRWAY_BILL = "AirwayBill";


    @Autowired
    private ObjectMapper objectMapper;

    private String baseUrl;

    @Autowired
    private LOService<AirwayBillDTO> service;


    @PostMapping("/companies/{companyId}/los")
    @ResponseBody
    public BaseDTO<?> createLO(@PathVariable("companyId") String companyId, @RequestBody String lo) {
        this.baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();

        try {
            JsonNode node = objectMapper.readTree(lo);

            String type = (node.get("@type") != null && node.get("@type").textValue() != null) ? node.get("@type").textValue() : null;
            String id = (node.get("@id") != null && node.get("@id").textValue() != null) ? node.get("@id").textValue() : UUID.randomUUID().toString();
            String url = (node.get("@url") != null && node.get("@url").textValue() != null) ? node.get("@url").textValue() : (baseUrl + "/" + id);

            if ("AirwayBill".equalsIgnoreCase(type)) {

                AirwayBillDTO airwayBillDTO = objectMapper.readValue(lo, AirwayBillDTO.class);
                airwayBillDTO.setId(id);
                airwayBillDTO.setUrl(url);
                BaseDTO<AirwayBillDTO> airwayBillLO = new BaseDTO<>(airwayBillDTO, type, url, companyId, id);
                airwayBillLO.setCreatedAt(new GregorianCalendar());

                // Save LO into DB
                BaseDTO<AirwayBillDTO> responseLO = service.createLO(airwayBillLO);

                service.publishLOTOHub(responseLO);
//                service.publish(airwayBillLO);

                // Send Notification to WhatsApp. TODO

                return responseLO;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @GetMapping("/companies/{companyId}/los")
    @ResponseBody
    public List<BaseDTO> getLOs(@PathVariable("companyId") String companyId) {
        return service.getLOs(companyId);
    }

    @GetMapping("/companies/{companyId}/los/{loId}")
    @ResponseBody
    public BaseDTO getLO(@PathVariable("companyId") String companyId, @PathVariable("loId") String loId) {
        return service.getLO(companyId, loId);
    }

    @GetMapping("/{loId}")
    @ResponseBody
    public BaseDTO getLO(@PathVariable("loId") String loId) {
        return service.getLO(null, loId);
    }


    @DeleteMapping("/companies/{companyId}/los/{loId}")
    @ResponseBody
    public void deleteLO(@PathVariable("companyId") String companyId, @PathVariable("loId") String loId) {
        service.deleteLO(companyId, loId);
    }


    @PatchMapping("/companies/{companyId}/los/{loId}")
    @ResponseBody
    public BaseDTO<?> updateLO(@PathVariable("companyId") String companyId, @RequestBody String lo, @PathVariable("loId") String loId) {
        this.baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();

        try {
            JsonNode node = objectMapper.readTree(lo);

            String type = node.get("@type") != null ? node.get("@type").textValue() : null;
            String id = node.get("@id") != null ? node.get("@id").textValue() : UUID.randomUUID().toString();
            String url = node.get("@url") != null ? node.get("@url").textValue() : (baseUrl + "/" + companyId + "/" + id);

            if (AIRWAY_BILL.equalsIgnoreCase(type)) {

                AirwayBillDTO airwayBillDTO = objectMapper.readValue(lo, AirwayBillDTO.class);
                BaseDTO<AirwayBillDTO> airwayBillLO = new BaseDTO<>(airwayBillDTO, type, url, companyId, id);
                airwayBillLO.setCreatedAt(new GregorianCalendar());

                // Save LO
                return service.updateLO(airwayBillLO, loId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @PostMapping("/companies/{companyId}/los/{loId}/status")
    @ResponseBody
    public void createStatus(@PathVariable String companyId, @PathVariable String loId, @RequestBody StatusMessage status) {
        this.service.addStatusToLo(companyId, loId, status);

    }
}
