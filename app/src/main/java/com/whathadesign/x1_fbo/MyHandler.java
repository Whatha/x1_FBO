package com.whathadesign.x1_fbo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*
 * This handler will be passed to UsbService. Data received from serial port is displayed through this handler
 */
public class MyHandler extends Handler {

    // ----------------------------------------------------------------
    // ---------------------- Variables Globales ----------------------
    // ----------------------------------------------------------------

    private final Order_view mActivity;
    private HexManager hexManager;
    public ArrayList<Byte> dataByte = new ArrayList();
    public int counter = 0;
    public int limitador = 10;
    public Context c;
    public Activity act;

    // ----------------------------------------------------------------
    // ----------------------- Constructor Clase ----------------------
    // ----------------------------------------------------------------

    public MyHandler(Order_view activity, Context context) {
        mActivity = activity;
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
                Toast.makeText(mActivity, "CTS_CHANGE",Toast.LENGTH_LONG).show();
                break;
            case UsbService.DSR_CHANGE:
                Toast.makeText(mActivity, "DSR_CHANGE",Toast.LENGTH_LONG).show();
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
                 metro = Math.round(dataValue);
                Toast.makeText(c, "Qty dispensed: "+metro, Toast.LENGTH_LONG).show();

            }else   if(cmdValue.equals("1E") && !systemValue.equals("W&M")) {
                Float dataValue = Float.parseFloat(json.getString("Data"));
                int metro = Math.round(dataValue);
                UpdateMeters(Static_variables.selected,metro,0);

            }else {
                Static_variables.fuel("7");
                Static_variables.fuel("1");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

public int metro;
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

    private void UpdateMeters(final Truck t, final int newCurrent1, final int newCurrent2) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(c);


        JSONArray cosito = new JSONArray();
        JSONObject MyData = new JSONObject();

        try {
            MyData.put("ItemId", t.masterItemId);
            MyData.put("Id", t.getId());
            MyData.put("NewBalance", t.getBalance());
            MyData.put("operationDate", Static_variables.todayDate());
//Add the data you'd like to send to the server.
            JSONArray Meters = new JSONArray();
            JSONObject meter1 = new JSONObject();
            JSONObject meter2 = new JSONObject();
            try {
                meter1.put("meterId", t.getMetros().get(0).id);
                meter1.put("CurrentValue", t.getMetros().get(0).currentValue);
                meter1.put("NewCurrentValue", newCurrent1);

                Meters.put(meter1);


                if (t.getMetros().size() > 1) {
                    meter2.put("meterId", t.getMetros().get(1).id);
                    meter2.put("CurrentValue", t.getMetros().get(1).currentValue);
                    meter2.put("NewCurrentValue", newCurrent2);

                    Meters.put(meter2);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MyData.put("Meters", Meters); //Add the data you'd like to send to the server.
            cosito.put(MyData);
            System.out.println("LEFIN" + cosito);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Fuel_feed.CustomDos req = new Fuel_feed.CustomDos(Request.Method.POST, Static_variables.Apis + "addOpenVerifiedTruck", cosito,
                new Response.Listener<JSONArray>() {

                    @Override

                    public void onResponse(JSONArray response) {
                        Truck a = Static_variables.selected;
                        a.getMetros().get(0).currentValue = newCurrent1;
                        if (a.getMetros().size() > 1) {
                        a.getMetros().get(1).currentValue = newCurrent2;
                    }
                        mActivity.dd.dismiss();
                        mActivity.dialog = new Order_view.CustomDialog(mActivity, metro, 0, Static_variables.order.tailNbr.toString(), Static_variables.order.fsii,newCurrent1,newCurrent2);
                        mActivity.dialog.show();
                }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        mActivity.dd.dismiss();

                        Toast.makeText(c, "Error!", Toast.LENGTH_SHORT).show();

                        System.out.println("ERROR CARAJO");
                    }
                }) {

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("Authorization", "bearer " + Static_variables.Token);
                return headers;
            }
        };


        // Adding request to request queue
        MyRequestQueue.add(req);
    }
}

