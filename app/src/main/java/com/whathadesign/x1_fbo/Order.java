package com.whathadesign.x1_fbo;

import java.util.Date;

/**
 * Created by Daniel on 02/05/2018.
 */

public class Order {
    int requestDetailId,tailAircraftId,serviceId;
    String companyName,itemName,tailNbr,icao,fuelOn;
    String arrivalDate,departureDate,ArrivalEstimateDate,DepartureEstimateDate,qty,fum_Qty_Info;
    int eStatus;
    String notes,type;
    boolean fsii;
    String model,manufacturer;
    public Order(int requestDetailId, int tailAircraftId, int serviceId, String qty, String companyName, String itemName, String tailNbr, String icao, String arrivalDate, String departureDate, String arrivalEstimateDate, String departureEstimateDate, String fuelOn, int eStatus, Boolean fsii, String model, String manufacturer, String notes,String fum_Qty_Info) {
        this.requestDetailId = requestDetailId;
        this.tailAircraftId=tailAircraftId;
        this.serviceId = serviceId;
        this.qty = qty;
        this.companyName = companyName;
        this.itemName = itemName;
        this.tailNbr = tailNbr;
        this.icao = icao;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        ArrivalEstimateDate = arrivalEstimateDate;
        DepartureEstimateDate = departureEstimateDate;
        this.fuelOn = fuelOn;
        this.eStatus=eStatus;
        this.notes=notes;
        this.fsii=fsii;
        this.model=model;
        this.manufacturer=manufacturer;
        this.fum_Qty_Info=fum_Qty_Info;
    }

    public Order(String type,int requestDetailId, int tailAircraftId, int serviceId, String qty, String companyName, String itemName, String tailNbr, String icao, String arrivalDate, String departureDate, String arrivalEstimateDate, String departureEstimateDate, String fuelOn, int eStatus, Boolean fsii, String model, String manufacturer, String notes) {
        this.requestDetailId = requestDetailId;
        this.tailAircraftId=tailAircraftId;
        this.serviceId = serviceId;
        this.qty = qty;
        this.companyName = companyName;
        this.itemName = itemName;
        this.tailNbr = tailNbr;
        this.icao = icao;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        ArrivalEstimateDate = arrivalEstimateDate;
        DepartureEstimateDate = departureEstimateDate;
        this.fuelOn = fuelOn;
        this.eStatus=eStatus;
        this.notes=notes;
        this.fsii=fsii;
        this.model=model;
        this.manufacturer=manufacturer;
        this.type=type;
    }
}
