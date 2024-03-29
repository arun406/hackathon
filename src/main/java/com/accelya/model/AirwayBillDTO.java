package com.accelya.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@Data
@ToString
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class AirwayBillDTO {


    private Pet pet;

    private int pieces;

    private Value weight;

    private boolean isCreateAvailable;
    private String createType;
    private Value createWeight;
    private Dimension dimensions;

    private Flight flight;

    private Services services;

    @JsonProperty("@context")
    private Context context;

    @JsonProperty("@type")
    private String type;

    @JsonProperty("@id")
    private String id;

    private String Url;

    private String AirWaybillNumber;

    private String Date;

    private String LoadTypeCode;

    private String FlagConsolDirectLC;

    private String AccountInformationCode;

    private String AccountInformationDescription;

    private String PaymentMethodCode;

    private String ServiceCode;

    private String AirlineProductIdentifier;

    private String AirlineProductName;

    private String ExternalReference;

    private String OriginDestination;

    private String Shipper;

    private String Consignee;

    private String FreightForwarder;

    private String OtherParty;

    private String Carrier;

    private String Routing;

    private String WaybillLine;

    private String WaybillLineTotals;

    private String Handling;

    private String CustomsInformation;

    private String SecurityInformation;

    private String Insurance;

    private ChargeDeclaration ChargeDeclaration;

    private List<OtherCharge> OtherCharge;

    private ChargeSummary ChargeSummary;

    private ShipperSignature ShipperSignature;

    private CarrierSignature CarrierSignature;

    private String TotalPieceCount;

    private String TotalSLAC;

    private String TotalULDCount;

    private String TotalNetWeight;

    private String TotalTareWeight;

    private String TotalGrossWeight;

    private String TotalChargeableWeight;

    private String TotalVolume;

    private String TotalChargeAmount;

    private List<String> documentList = new ArrayList<>();

    private List<NotificationLO> updates = new ArrayList<>();
}
