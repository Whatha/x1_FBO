package com.whathadesign.x1_fbo;

public class Tail {
    int tailAircraftId;
    String tailNbr,icao,modelName;
    int itemIde;
    String itemName,imgPath,companyName;

    public Tail(int tailAircraftId, String tailNbr, String icao, String modelName, int itemIde, String itemName, String imgPath,String companyName) {
        this.tailAircraftId = tailAircraftId;
        this.tailNbr = tailNbr;
        this.icao = icao;
        this.modelName = modelName;
        this.itemIde = itemIde;
        this.itemName = itemName;
        this.imgPath = imgPath;
        this.companyName=companyName;
    }
}
