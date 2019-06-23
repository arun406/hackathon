package com.accelya.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.twilio.rest.api.v2010.account.Message;

import com.accelya.services.WhatsAppNotifierService;


@RestController
public class WhatsAppController{

  @Autowired
  private WhatsAppNotifierService whatsAppService;

  /**
   * Controller to map url with the service method to send WhatsApp message
   * to a given number configured in the sandbox
   *
   * @param text the message to be sent to the user
   * @param phoneNumber the phone number of the sanbox registed user
   * @return the Twilio Message object
   */
  @PostMapping("/notification/sendWhatsAppMessage")
  public Message sendWhatsAppMessage(@RequestParam String text, @RequestParam String phoneNumber) {
    return this.whatsAppService.sendWhatsAppMessage(text, phoneNumber);
  }

  /**
   * Controller to map url with the service method to send WhatsApp QR code
   * to a given number configured in the sandbox
   *
   * @param data the data to be sent to the user as a QR code
   * @param phoneNumber the phone number of the sanbox registed user
   * @return the Twilio Message object
   */
  @PostMapping("/notification/sendWhatsAppQRCode")
  public Message sendWhatsAppQRCode(@RequestParam String data, @RequestParam String phoneNumber) {
    return this.whatsAppService.sendWhatsAppQRCode(data, phoneNumber);
  }

  /**
   * Controller to map url with the service method to send WhatsApp QR code
   * to a given number configured in the sandbox
   *
   * @param documentURL The url link to the document
   * @param phoneNumber the phone number of the sanbox registed user
   * @return the Twilio Message object
   */
  @PostMapping("/notification/sendDocumentOnWhatsApp")
  public Message sendDocumentOnWhatsApp(@RequestParam String documentURL, @RequestParam String phoneNumber) {
    return this.whatsAppService.sendDocumentOnWhatsApp(documentURL, phoneNumber);
  }

}
