package com.whathadesign.x1_fbo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

/*
 * This handler will be passed to UsbService. Data received from serial port is displayed through this handler
 */
public class MyHandler extends Handler {

    // ----------------------------------------------------------------
    // ---------------------- Variables Globales ----------------------
    // ----------------------------------------------------------------

    private final WeakReference<Order_view> mActivity;
    private HexManager hexManager;
    public ArrayList<Byte> dataByte = new ArrayList();
    public int counter = 0;
    public int limitador = 10;
    public Context c;

    // ----------------------------------------------------------------
    // ----------------------- Constructor Clase ----------------------
    // ----------------------------------------------------------------

    public MyHandler(Order_view activity, Context context) {
        mActivity = new WeakReference<>(activity);
        c=context;
        hexManager = new HexManager();
    }

    // ----------------------------------------------------------------
    // --------------------- Metodos del Handler ----------------------
    // ----------------------------------------------------------------
    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case UsbService.MESSAGE_FROM_SERIAL_PORT:
                byte[] dataMsgByte = (byte[]) msg.obj;
                //String tempHex = String.format("%02X", dataPrueba[0]);

                //mActivity.get().display.append("Hexadeximal Value:"+tempHex + "\n");
                //mActivity.get().display.append("Hex Value Principal:"+dataPrueba.toString() + "\n");
                //byte[] dataMsgByte = ((String) msg.obj).getBytes();
                //dataByte.add(dataPrueba[0]);

                for (int i = 0; i < dataMsgByte.length; i++) {
                    String tempHex = String.format("%02X", dataMsgByte[i]);
                   // mActivity.get().display.append("Hexadeximal Value:"+tempHex + "\n");

                    if(tempHex.equals("7E")){
                        counter = 0;
                        limitador = 10;
                        dataByte = new ArrayList();
                    }
                    dataByte.add(dataMsgByte[i]);
                    counter++;

                    if(dataByte.size() == 6){
                        int BTCNT = dataByte.get(dataByte.size() - 1);
                        limitador += BTCNT - 2;
                    }

                }
                break;
            case UsbService.CTS_CHANGE:
                Toast.makeText(mActivity.get(), "CTS_CHANGE",Toast.LENGTH_LONG).show();
                break;
            case UsbService.DSR_CHANGE:
                Toast.makeText(mActivity.get(), "DSR_CHANGE",Toast.LENGTH_LONG).show();
                break;
        }

        if (counter == limitador-2){
        JSONObject json = hexManager.responseToJSON(dataByte);

        try {
            String cmdValue = json.getString("command");
            String systemValue = json.getString("system_status");
            String deliveryValue = json.getString("delivery_status");
            if(cmdValue.equals("2D") && !systemValue.equals("W&M")) {

                Float dataValue = Float.parseFloat(json.getString("Data"));
                int metro = Math.round(dataValue);
                Toast.makeText(c, "Qty dispensed: "+metro, Toast.LENGTH_LONG).show();

            }else   if(cmdValue.equals("1E") && !systemValue.equals("W&M")) {
                Float dataValue = Float.parseFloat(json.getString("Data"));
                int metro = Math.round(dataValue);
                mActivity.get().dd.dismiss();
                Static_variables.selected.getMetros().get(0).currentValue=metro;
                Intent a = new Intent(c, Fuel_feed.class);
                a.putExtra("status", "Fueling completed!");
                c.startActivity(a);
            }else {
                Static_variables.fuel("7");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

    // ----------------------------------------------------------------
    // ---------------------- Metodos Auxiliares ----------------------
    // ----------------------------------------------------------------

    public void displayVolume(){
        byte[] dataByteArray = new byte[dataByte.size()];

        for (int i = 0; i < dataByte.size(); i++) {
            dataByteArray[i] = dataByte.get(i);
        }

        byte[] volumen = Arrays.copyOfRange(dataByteArray, 10, 18);
        Double valueVolume = ByteBuffer.wrap(volumen).getDouble();

        //mActivity.get().display.append("Value Gross Gal: " + valueVolume + "\n");
        counter = 0;
        dataByte = new ArrayList<Byte>();
    }
}

