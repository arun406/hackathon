package com.accelya.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;


import java.net.URI;
import java.util.List;
import java.util.ArrayList;

import java.net.URISyntaxException;


@Service
public class WhatsAppNotifierService {

  /** The account id */
  private static final String ACCOUNT_SID = "ACf542013df4280d7db7a34b2d2a735ffc";

  /** The authentication token for twilio sandbox  */
  private static final String AUTH_TOKEN = "ad8de721a6cd0147135c7227941cf93b";

  /** The QR code generator URL */
  private static final String QR_CODE_GEN_URL = "https://api.qrserver.com/v1/create-qr-code/?size=250x250&data=";


  /**
   * Method to send a string message to a given sandbox registered WhatsApp number
   * @param text the text of the message to sent
   * @param phoneNumber the phone number to send the message to
   * @return Twilio message object
   */
  public Message sendWhatsAppMessage (String text, String phoneNumber) {

    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    Message message = Message.creator(
        new PhoneNumber("whatsapp:+" + phoneNumber), // to twilio sandbox registered phone number
        new PhoneNumber("whatsapp:+14155238886"), // from Twilio phone number
        text)
    .create();
    System.out.println(message.getSid());

    return message;

  }

  /**
   * Method to send a QR code equivalent for given data to
   * sandbox registered WhatsApp number
   *
   * @param data the data whose equivalent QR code is sent
   * @param phoneNumber the phone number to send the message to
   * @return the Twilio message object
   */
  public Message sendWhatsAppQRCode (String data, String phoneNumber) {

    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

    List<URI> mediaURL = new ArrayList<URI>();

    String qrCodeURL = getQRCodeURL(data);

    System.out.println("qrCodeURL: " + qrCodeURL);

    try {
      mediaURL.add(new URI(qrCodeURL));
    } catch (URISyntaxException ex) {
      System.out.println("Invalid URI syntax");
    }

    Message message = Message.creator(
        new PhoneNumber("whatsapp:+" + phoneNumber), // to twilio sandbox registered phone number
        new PhoneNumber("whatsapp:+14155238886"), // from Twilio phone number
        mediaURL)
    .create();

    System.out.println(message.getSid());

    return message;

  }

  /**
   * Method to send a QR code equivalent for given data to
   * sandbox registered WhatsApp number
   *
   * @param documentURL the url link to the document
   * @param phoneNumber the phone number to send the message to
   * @return the Twilio message object
   */
  public Message sendDocumentOnWhatsApp (String documentURL, String phoneNumber) {

    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

    List<URI> mediaURL = new ArrayList<URI>();

    try {
      mediaURL.add(new URI(documentURL));
    } catch (URISyntaxException ex) {
      System.out.println("Invalid URI syntax");
    }

    Message message = Message.creator(
        new PhoneNumber("whatsapp:+" + phoneNumber), // to twilio sandbox registered phone number
        new PhoneNumber("whatsapp:+14155238886"), // from Twilio phone number
        mediaURL)
    .create();

    System.out.println(message.getSid());

    return message;

  }


  /**
   * Private method to construct url for an API request.
   * whose response will be a QR code
   *
   * @param data The data to be appended to the QR API URL request
   * @return constructed QR code API URL
   */
  private String getQRCodeURL(String data) {
    StringBuilder sb = new StringBuilder(QR_CODE_GEN_URL);
    sb.append(data);
    return sb.toString();
  }

}
