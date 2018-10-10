package com.whathadesign.x1_fbo;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel on 19/04/2018.
 */

public class CloseDialog extends Dialog implements
        android.view.View.OnClickListener {
    public Activity activity;
    Context context;
    public Button complete;
    String texto;
    RequestQueue ExampleRequestQueue;
    ProgressDialog dialog;
    RelativeLayout hider1,hider2;
    String code,name;
    int eWarehouseType,capacity,endingbalance,fuelIn,fuelOut,fuelOpeningBalance;
    int openMeter []=new int [2];
    Dialog dialogo;
    public CloseDialog(Activity activity, Context context, String texto) {
        super(activity);
        this.activity = activity;
        this.context=context;
        this.texto = texto;
        ExampleRequestQueue = Volley.newRequestQueue(context);

    }
    RelativeLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialogo_progreso);

        dialogo=this;

        ExampleRequestQueue.add(jsonArrayRequest);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_truck_dialog_btn:

                break;
            default:
                break;
        }
        dismiss();
    }


    JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
            Request.Method.GET,
            Static_variables.Apis + "getCloseTruck/"+Static_variables.getSelected().id,
            null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // Do something with response
                    //mTextView.setText(response.toString());
                    // Process the JSON
                    try {
                        // Loop through the array elements
                            // Get current json object
                            System.out.println("COSOOOOOOOOOOO IDENT"+Static_variables.selected.id);
                            JSONObject truck = response;
                            code = truck.getString("code");
                            eWarehouseType = truck.getInt("eWarehouseType");
                            name=truck.getString("name");
                            capacity = truck.getInt("capacity");
                            endingbalance=truck.getInt("endingBalance");
                            fuelIn=truck.getInt("fuelIn");
                            fuelOut=truck.getInt("fuelOut");
                            fuelOpeningBalance=truck.getInt("fuelOpeningBalance");

                        JSONArray metros=truck.getJSONArray("meters");
                        for (int j = 0; j < metros.length() ; j++) {
                            JSONObject metrico=metros.getJSONObject(j);
                            JSONObject met=metrico.getJSONObject("meter");
                            int openValue=met.getInt("openVerifiedValue");
                            System.out.println(j+ " OPENING VALUE"+openValue+" holi");
                            if(j<2){
                                openMeter[j] = openValue;
                            }
                        }

                            // Get the current student (json object) data
                            // Display the formatted json data in text view
                            //mTextView.append(firstName +" " + lastName +"\nAge : " + age);
                            //mTextView.append("\n\n");

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ExampleRequestQueue.add(jsonArrayRequest);

                    }finally {
                        setContentView(R.layout.custom_close_dialog);

                        layout = (RelativeLayout) findViewById(R.id.custom_close_dialog_main);

                        hider1=(RelativeLayout)findViewById(R.id.hider_1);
                        hider2=(RelativeLayout)findViewById(R.id.hider_2);
                        if(Static_variables.selected.getMetros().size()==1){
                            hider1.setVisibility(View.GONE);
                            hider2.setVisibility(View.GONE);

                        }
                        layout.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
                                if(getCurrentFocus()!=null)
                                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                return true;
                            }
                        });
                        Button close=(Button)findViewById(R.id.close_truck_dialog_btn);
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                EditText end1=(EditText)findViewById(R.id.dialog_cl_uno);
                                EditText end2=(EditText)findViewById(R.id.dialog_cl_dos);
                                if(end1.getText().length()<1 && Static_variables.selected.getMetros().size()!=1 ){
                                    if( end2.getText().length()<1 ) {
                                        Toast.makeText(context, "Please, put the ending meters", Toast.LENGTH_SHORT).show();
                                    }else
                                        {
                                        UpdateMeters(Static_variables.selected,Integer.parseInt(end1.getText().toString()),Integer.parseInt(end2.getText().toString()));
                                    }

                                }else{
                                    UpdateMeters(Static_variables.selected,Integer.parseInt(end1.getText().toString()),0);
                                }
                            }
                        });


                        TextView open=(TextView)findViewById(R.id.openingBalance);
                        TextView fuelInTx=(TextView)findViewById(R.id.fuelIn);
                        TextView fuelOutTx=(TextView)findViewById(R.id.fuelOut);
                        TextView ending=(TextView)findViewById(R.id.endingBalance);
                        TextView variance=(TextView)findViewById(R.id.variance);
                        TextView nameTx=(TextView)findViewById(R.id.close_dialog_text);

                        EditText m1=(EditText)findViewById(R.id.dialog_meters_uno);
                        EditText m2=(EditText)findViewById(R.id.dialog_meters_dos);

                        nameTx.setText(name);
                        open.setText(String.valueOf(fuelOpeningBalance));
                        fuelInTx.setText(String.valueOf(fuelIn));
                        fuelOutTx.setText(String.valueOf(fuelOut));
                        ending.setText(String.valueOf(endingbalance));
                        variance.setText(String.valueOf(fuelOpeningBalance-endingbalance));

                        m1.setText(String.valueOf(openMeter[0]));
                        if(Static_variables.selected.getMetros().size()>1)
                        m2.setText(String.valueOf(openMeter[1]));

                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Do something when error occurred
                    error.printStackTrace();
                }
            })
    {
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap();
            headers.put("Authorization", "bearer " + Static_variables.Token);
            return headers;
        }
    };

    private void UpdateMeters(final Truck t,final int newCurrent1, final int newCurrent2) {

        dialog = new ProgressDialog(activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(activity.getResources().getColor(R.color.transparente)));
        dialog.show();
        dialog.setContentView(R.layout.dialogo_progreso);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String d = df.format(Calendar.getInstance().getTime());
        String date = d;
        JSONArray cosito= new JSONArray();
        JSONObject MyData = new JSONObject();

        try {
            MyData.put("ItemId",t.masterItemId);
            MyData.put("Id",t.getId());
            MyData.put("NewBalance", endingbalance);
            MyData.put("operationDate", date);
//Add the data you'd like to send to the server.
            JSONArray Meters = new JSONArray();
            JSONObject meter1 = new JSONObject();
            JSONObject meter2 = new JSONObject();
            try {
                meter1.put("meterId", t.getMetros().get(0).id);
                meter1.put("CurrentValue", t.getMetros().get(0).currentValue);
                meter1.put("NewCurrentValue", newCurrent1);

                Meters.put(meter1);


                if(t.getMetros().size()>1){
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

        Fuel_feed.CustomDos req = new Fuel_feed.CustomDos(Request.Method.POST, Static_variables.Apis + "addCloseVerifiedTruck",  cosito,
                new Response.Listener<JSONArray>() {

                    @Override

                    public void onResponse(JSONArray response) {
                        dialog.dismiss();
                        Intent a = new Intent(context.getApplicationContext(), truck_feed.class);
                        activity.startActivity(a);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        error.printStackTrace();
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
        ExampleRequestQueue.add(req);
    }

}