package com.whathadesign.x1_fbo;

import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

public class HexManager {
    public HashMap<Integer, String> errors;
    public HashMap<Integer, String> delivery_status;
    public HashMap<Integer, String> system_status;


    public HexManager(){

        errors = new HashMap();
        errors.put(0, "0x00 - NO ERROR");
        errors.put(1, "0x01 - COMMAND NOT RECOGNIZED");
        errors.put(2, "0x02 - DS READ - DBL VALUE is NaN");
        errors.put(3, "0x03 - DS READ - RETURNED INVALID NUMERIC DATA VALUE");
        errors.put(4, "0x04 - DS READ - ENUM VALUE NOT RECOGNIZED");
        errors.put(5, "0x05 - DS READ - VARIABLE NOT FOUND OR READ ERROR");
        errors.put(6, "0x06 - DS WRITE - ERROR DURING INTERNAL DATA WRITE OPERATION");
        errors.put(10, "0x0A - RESERVED");
        errors.put(11, "0x0B - SYSTEM RETURNED INVALID DELIVERY STATE");
        errors.put(15, "0x0F - TIMER NOT DEFINED OR ZERO TIMEOUT TARGET");
        errors.put(20, "0x14 - SECONDARY ENUM VALUE OUT OF RANGE");
        errors.put(21, "0x15 - INCORRECT BOOLEAN VALUE FORMAT");
        errors.put(27, "0x1B - RESERVED");
        errors.put(31, "0x1F - ERROR DURING PRODUCT TABLE LOADING");
        errors.put(32, "0x20 - PRODUCT TABLE NOT LOADED IN MEMORY");
        errors.put(33, "0x21 - PRODUCT TABLE INDEX OUT OF BOUNDS");
        errors.put(34, "0x22 - PRODUCT ID NOT FOUND IN THE PRODUCT TABLE");
        errors.put(35, "0x23 - NO ACTIVE SHIFT");
        errors.put(36, "0x24 - LAST DELIVERY TICKET STATUS NOT OK");
        errors.put(48, "0x30 - ERROR DURING DIRECT DELIVERY INIT");
        errors.put(49, "0x31 - ERROR DURING PRESET DELIVERY INIT");
        errors.put(50, "0x32 - DELIVERY NOT CONFIGURED");
        errors.put(51, "0x33 - START DELIVERY TYPE DOES NOT MATCH THE CONFIGURED DELIVERY TYPE");
        errors.put(59, "0x3B - PRESET DELIVERY IS NOT ALLOWED");
        errors.put(60, "0x3C - REQUESTED NET PRESET ON NONCOMPENSATED PRODUCT");
        errors.put(61, "0x3D - PRICE PRESET NOT ALLOWED ON ZERO PRICE PRODUCT");
        errors.put(62, "0x3E - REQUESTED PRESET AMOUNT OR VOLUME IS ZERO, NEGATIVE OR TOO SMALL");
        errors.put(63, "0x3F - INCORRECT INPUT NUMERIC VALUE");
        errors.put(64, "0x40 - INCORRECT INPUT DATA LENGTH");
        errors.put(65, "0x41 - ENUM VALUE OUT OF RANGE");
        errors.put(66, "0x42 - PRODUCT_ID OUT OF RANGE (1000-9999)");
        errors.put(67, "0x43 - PRODUCT_ID NOT FOUND OR NOT ACTIVE");
        errors.put(68, "0x44 - NO ACTIVE PRODUCTS");
        errors.put(69, "0x45 - ERROR DURING PRODUCT DATA LOADING");
        errors.put(70, "0x46 - ERROR DURING PRODUCTS CONFIGURATION READING");
        errors.put(71, "0x47 - TOTALIZER VALUE FOR THIS PRODUCT ID CANNOT BE FOUND");
        errors.put(72, "0x48 - INVALID TIME VALUE");
        errors.put(73, "0x49 - INVALID DATE VALUE");
        errors.put(80, "0x50 - ETICKETS TABLE EMPTY OR NO INITIALIZED");
        errors.put(81, "0x51 - ETICKETS TABLE INDEX OUT OF BOUNDS");
        errors.put(82, "0x52 - ETICKET NOT FOUND");
        errors.put(83, "0x53 - ERROR WHILE LOADING ETICKET");
        errors.put(84, "0x54 - ETICKET DATA NOT LOADED");
        errors.put(85, "0x55 - ETICKET BLOCKNR OUT OF BOUNDS");
        errors.put(86, "0x56 - ETICKET DATA CRC ERROR");
        errors.put(96, "0x60 - OPERATION CANNOT BE EXECUTED IN CURRENT SYSTEM MODE");
        errors.put(97, "0x61 - CALIBRATION BOLT NOT IN PLACE");
        errors.put(98, "0x62 - CALIBRATION BOLT IN PLACE");
        errors.put(101, "0x65 - TEMPERATURE SENSOR READING ERROR");
        errors.put(102, "0x66 - TEMPERATURE SENSOR CALIBRATION FAILED");
        errors.put(126, "0x7E - RESERVED");
        errors.put(128, "0x80 - INVALID COMMAND DEFINITION");
        errors.put(129, "0x81 - DS ERROR - ERROR DURING DS READING");
        errors.put(130, "0x82 - DS READ - DOUBLE RETURNED NON NUMERIC VALUE");
        errors.put(131, "0x83 - ENUM DEFINITION MISSING FROM TCS3000 RI CONFIGURATION");
        errors.put(132, "0x84 - ERROR IN ENUM DEFINITION IN TCS3000");
        errors.put(160, "0xA0 - DS ERROR - ERROR DURING DS WRITING");
        errors.put(161, "0xA1 - COMMAND ERROR - THE SYSTEM RETURNED ERROR DURING COMMAND EXECUTION");
        errors.put(162, "0xA2 - SYSTEM ERROR - TIMEOUT DURING COMMAND EXECUTION");
        errors.put(163, "0xA3 - SYSTEM ERROR - INCORRECT INTERNAL COMMAND ACKNOWLEDGMENT");
        errors.put(180, "0xB4 - GTKT LINE INDEX OUT OF BOUNDS");
        errors.put(181, "0xB5 - GTKT NOT IN WRITE MODE");
        errors.put(182, "0xB6 - INCORRECT GTKT LINE LENGTH");
        errors.put(183, "0xB7 - ERROR WHILE SETTING GTKT LINE");
        errors.put(185, "0xB9 - ZERO DIMENTION IN GTKT DATA");
        errors.put(186, "0xBA - GTKT CHECKSUM ERROR");
        errors.put(187, "0xBB - ERROR WHILE SAVING GTKT");
        errors.put(246, "0xF6 - SHUTDOWN NOT INITIATED OR 10s TIMEOUT EXPIRED");
        errors.put(247, "0xF7 - REBOOT NOT INITIATED OR 10s TIMEOUT EXPIRED");
        errors.put(250, "0xFA - EXTENDED ERRORCODE (SEE STATUS BYTE FOR CODE)");
        errors.put(252, "0xFC - INTERFACE BUSY");
        errors.put(253, "0xFD - SYSTEM BUSY");
        errors.put(254, "0xFE - SYSTEM SHUTDOWN IN PROGRESS");
        errors.put(255, "0xFF - ENUM VALUE OUT OF RANGE");


        delivery_status = new HashMap();

        delivery_status.put(0, "ERROR");
        delivery_status.put(1, "DELIVERY IDLE");
        delivery_status.put(2, "DELIVERY ACTIVE");
        delivery_status.put(3, "AIR DETECTED");
        delivery_status.put(4, "DELIVERY PAUSED");
        delivery_status.put(5, "DELIVERY STOPPED");
        delivery_status.put(6, "TICKET PENDING");
        delivery_status.put(7, "PRINTING");


        system_status = new HashMap();

        system_status.put(0, "ERROR");
        system_status.put(1, "SYSTEM IDLE");
        system_status.put(2, "W&M");
        system_status.put(3, "DELIVERY ACTIVE");
        system_status.put(4, "SYSTEM BUSY");




    }

    public JSONObject responseToJSON(ArrayList<Byte> answerTCS) {
        JSONObject response = new JSONObject();
        int BTCNT = (int)answerTCS.get(5);

        try{
            response.put("header",String.format("%02X", answerTCS.get(0)));
            response.put("dest",String.format("%02X", answerTCS.get(1)));
            response.put("source",String.format("%02X", answerTCS.get(2)));
            response.put("flagA",String.format("%02X", answerTCS.get(3)));
            response.put("flagB",String.format("%02X", answerTCS.get(3)));
            response.put("flagC",String.format("%02X", answerTCS.get(3)));
            response.put("command",String.format("%02X", answerTCS.get(4)));
            response.put("BTCNT",BTCNT);
            response.put("error",errors.get((int) answerTCS.get(6)));

            String[] status = String.format("%02X", answerTCS.get(7)).split("");

            response.put("delivery_status",delivery_status.get(Integer.parseInt(status[2])));
            response.put("system_status",system_status.get(Integer.parseInt(status[1])));


            if(BTCNT > 2){
                byte[] tempArray = new byte[BTCNT - 2];

                for (int i = 0; i < BTCNT-2; i++) {
                    tempArray[i] = answerTCS.get(i + 8);
                }

                Double valueVolume = ByteBuffer.wrap(tempArray).getDouble();

                response.put("Data",valueVolume);
            }

            else{
                response.put("Data", "");
            }
        }

        catch (Exception e) {
            response = new JSONObject();
        }


        return response;
    }
}
