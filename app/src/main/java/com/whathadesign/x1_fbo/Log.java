package com.whathadesign.x1_fbo;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Daniel on 08/05/2018.
 */

public class Log {
    int id;
    String operationDate;
    String operationDescription, operationNbr, tailNbr, icao;
    int  operationType;
    ArrayList<OperationDetails> details;

    public Log(int id, String operationDate, String operationDescription, String operationNbr, int operationType, String tailNbr,String icao, ArrayList<OperationDetails> details) {
        this.id = id;
        this.operationDate = operationDate;
        this.operationDescription = operationDescription;
        this.operationNbr = operationNbr;
        this.operationType = operationType;
        this.tailNbr=tailNbr;
        this.icao=icao;
        this.details = details;
    }

    public static class OperationDetails{
        int id, operationId,eOperationDetailType,itemId,warehouseId,qty;
        String tailAircraftId,meterName,meterId,startingMeter,endingMeter,companyId, fueler;

        public OperationDetails(int id, int operationId, int eOperationDetailType, int itemId, int warehouseId, String meterId, int qty, String startingMeter, String endingMeter, String companyId, String tailAircraftId, String meterName,String fueler) {
            this.id = id;
            this.operationId = operationId;
            this.eOperationDetailType = eOperationDetailType;
            this.itemId = itemId;
            this.warehouseId = warehouseId;
            this.meterId = meterId;
            this.qty = qty;
            this.startingMeter = startingMeter;
            this.endingMeter = endingMeter;
            this.companyId = companyId;
            this.tailAircraftId = tailAircraftId;
            this.meterName = meterName;
            this.fueler=fueler;
        }
    }
}