package com.accelya.controller;


import com.accelya.model.AirwayBillDTO;
import com.accelya.model.BaseDTO;
import com.accelya.model.LOWrapper;
import com.accelya.model.NotificationLO;
import com.accelya.services.LOService;
import com.accelya.services.NotificationLOService;
import com.accelya.services.WhatsAppNotifierService;
import com.example.filedemo.service.FileStorageService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

@RestController
public class LOController {

    private static final String shipperPhoneNumber = "918056243803";

    private static final Logger logger = LoggerFactory.getLogger(LOController.class);

    @Autowired
    private WhatsAppNotifierService whatsAppService;

    @Autowired
    public static final String AIRWAY_BILL = "AirwayBill";

    @Autowired
    private ObjectMapper objectMapper;

    private String baseUrl;

    @Autowired
    private LOService<AirwayBillDTO> service;


    @Autowired
    private NotificationLOService notificationLOService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping(value = "/companies/{companyId}/los", consumes = {"multipart/form-data"})
    @ResponseBody
    public ResponseEntity<?> createLO(@PathVariable("companyId") String companyId, @ModelAttribute LOWrapper model) {

        this.baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        String fileDownloadUri = null;

        try {
            String loText = model.getLo();
            JsonNode node = objectMapper.readTree(loText);

            String type = (node.get("@type") != null && node.get("@type").textValue() != null) ? node.get("@type").textValue() : null;


            if(StringUtils.isEmpty(type)){
                type = (node.get("type") != null && node.get("type").textValue() != null) ? node.get("type").textValue() : null;
            }

            String id = (node.get("@id") != null && node.get("@id").textValue() != null) ? node.get("@id").textValue() : UUID.randomUUID().toString();
            String url = (node.get("@url") != null && node.get("@url").textValue() != null) ? node.get("@url").textValue() : (baseUrl + "/" + id);


            // Save the attachments
            if (model.getFile() != null) {
                String fileName = this.fileStorageService.storeFile(model.getFile());
                fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path(id +"/")
                        .path(fileName)
                        .toUriString();
            }

            if ("AirwayBill".equalsIgnoreCase(type)) {

                AirwayBillDTO airwayBillDTO = objectMapper.readValue(loText, AirwayBillDTO.class);
                airwayBillDTO.setId(id);
                airwayBillDTO.setUrl(url);
                BaseDTO<AirwayBillDTO> lo = new BaseDTO<>(airwayBillDTO, type, url, companyId, id);
                lo.setCreatedAt(new GregorianCalendar());
                airwayBillDTO.getDocumentList().add(fileDownloadUri);
                // Save LO into DB
                BaseDTO<AirwayBillDTO> responseLO = service.createLO(lo);

                // Publish to HUB
                service.publishLOTOHub(responseLO);

                /* Send Notification to shipper WhatsApp configured number. */
                String message = constructShipperWhatsAppMessage(airwayBillDTO.getShipper(), airwayBillDTO.getPet().getName(), "AA 6132 LHR - JFK");
                whatsAppService.sendWhatsAppMessage(message, shipperPhoneNumber);

                return new ResponseEntity<>(responseLO, HttpStatus.OK);
            } else if ("Notification".equalsIgnoreCase(type)) {

                NotificationLO notificationLO = objectMapper.readValue(loText, NotificationLO.class);
                notificationLO.setId(id);
                notificationLO.setUrl(url);
                notificationLO.getDocuments().add(fileDownloadUri);

                NotificationLO responseLO = notificationLOService.createLO(notificationLO);

                // save and push to HUB
                notificationLOService.publishLOTOHub(notificationLO);

                whatsAppService.sendWhatsAppMessage(notificationLO.getText(), "917045500062");

                sendWhatsAppQRCode(notificationLO, "917045500062");
                whatsAppService.sendWhatsAppMessage(notificationLO.getText(), "971529773913");
                sendWhatsAppQRCode(notificationLO, "971529773913");
//                whatsAppService.sendWhatsAppMessage(notificationLO.getText(), airwayBillDTO.);
                return new ResponseEntity<>(responseLO, HttpStatus.OK);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param shipperName
     * @param petName
     * @param flight
     * @return the whatsapp message to be sent to the shipper as a string
     */
    private String constructShipperWhatsAppMessage(
            String shipperName, String petName, String flight) {

        StringBuilder sb = new StringBuilder();
        sb.append("Dear ");
        sb.append(shipperName);
        sb.append(" your pet (");
        sb.append(petName);
        sb.append(") has been booked to travel on: ");
        sb.append(flight);

        return sb.toString();
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
    public void createStatus(@PathVariable String companyId, @PathVariable String loId, @RequestBody NotificationLO status) {
        this.service.addStatusToLo(companyId, loId, status);

    }

    private void sendWhatsAppQRCode(NotificationLO notificationLO, String phoneNumber) {

        if(notificationLO.getCode().equals("BKGCFM") || notificationLO.getCode().equals("TENDER")) {

            StringBuilder sb = new StringBuilder("http://localhost:4200/track/");
            sb.append(notificationLO.getLoId());

            whatsAppService.sendWhatsAppQRCode(sb.toString(), phoneNumber);
        }

    }
}
