package com.whathadesign.x1_fbo;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Daniel on 02/05/2018.
 */

public class Truck {
    String description,code,itemName,name;
    String lastOperationDate;
    int id,eWarehouseType,itemId,masterItemId;
    boolean canDispense,canDefuel,verified;
    int balance, capacity;
    ArrayList<meter> metros;
    Truck(int id, int balance, boolean canDispense, boolean canDefuel, boolean verified,String code, int capacity, int eWarehouseType, int itemId, String itemName, String name, String lastOperationDate, ArrayList<meter> metros) {
        this.id=id;
        this.balance=balance;
        this.canDispense=canDispense;
        this.canDefuel=canDefuel;
        this.verified=verified;
        this.code=code;
        this.capacity=capacity;
        this.eWarehouseType=eWarehouseType;
        this.itemId=itemId;
        this.itemName=itemName;
        this.name=name;
        this.lastOperationDate=lastOperationDate;
        this.metros=metros;
    }

    Truck(int id, int balance, boolean canDispense, boolean canDefuel, boolean verified,String code, int capacity, int eWarehouseType, int itemId, int masterItemId, String itemName, String name, String lastOperationDate, ArrayList<meter> metros) {
        this.id=id;
        this.balance=balance;
        this.canDispense=canDispense;
        this.canDefuel=canDefuel;
        this.verified=verified;
        this.code=code;
        this.capacity=capacity;
        this.eWarehouseType=eWarehouseType;
        this.itemId=itemId;
        this.itemName=itemName;
        this.name=name;
        this.lastOperationDate=lastOperationDate;
        this.metros=metros;
        this.masterItemId=masterItemId;
    }

    public static class meter{
        int id,currentValue;
        String name;
        meter(int id, String name, int currentValue){
            this.id=id;
            this.name=name;
            this.currentValue=currentValue;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int geteWarehouseType() {
        return eWarehouseType;
    }

    public void seteWarehouseType(int eWarehouseType) {
        this.eWarehouseType = eWarehouseType;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public boolean isCanDispense() {
        return canDispense;
    }

    public void setCanDispense(boolean canDispense) {
        this.canDispense = canDispense;
    }

    public boolean isCanDefuel() {
        return canDefuel;
    }

    public void setCanDefuel(boolean canDefuel) {
        this.canDefuel = canDefuel;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public ArrayList<meter> getMetros() {
        return metros;
    }

    public void setMetros(ArrayList<meter> metros) {
        this.metros = metros;
    }
}
