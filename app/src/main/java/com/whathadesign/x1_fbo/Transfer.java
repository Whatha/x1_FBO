package com.whathadesign.x1_fbo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.mindorks.placeholderview.PlaceHolderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Transfer extends AppCompatActivity {
    private Activity actividad;

    Spinner spinnerDos, spinner;
    TextView meterStart, meterEnd, total;
    Truck selected;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        actividad = this;
        Static_variables.set_down_info_bar(this.findViewById(android.R.id.content));
        Static_variables.setupDrawer(this, "New Transfer");
        RequestQueue ExampleRequestQueue = Volley.newRequestQueue(this);
        ExampleRequestQueue.add(jsonArrayRequest);
        spinner = (Spinner) findViewById(R.id.transfer_spinner_uno);
        spinnerDos = (Spinner) findViewById(R.id.transfer_spinner_dos);

        String[] letraDos = {"J2 - Meter 1", "J2 - Meter 2", "J3 - Meter 1", "J3 - Meter 2", "J4 - Meter 1", "J4 - Meter 2", "J5 - Meter 1", "J5 - Meter 2"};
        String[] letra = new String[Static_variables.selected.getMetros().size()];
        for (int i = 0; i < letra.length; i++) {
            letra[i] = Static_variables.selected.getMetros().get(i).name;
        }


        final TextView from_balance, from_capacity;
        final ProgressBar from_progress;
        from_balance = (TextView) findViewById(R.id.from_balance);
        from_capacity = (TextView) findViewById(R.id.from_capacity);
        from_progress = (ProgressBar) findViewById(R.id.from_progress);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                update(from_balance, from_capacity, from_progress, Static_variables.selected.getBalance(), Static_variables.selected.getCapacity());
                meterStart.setText(String.valueOf(Static_variables.selected.metros.get(i).currentValue));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        meterStart = (TextView) findViewById(R.id.dialog_meters_uno);
        meterEnd = (TextView) findViewById(R.id.dialog_meters_dos);
        total = (TextView) findViewById(R.id.total);

        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, letra));
        spinnerDos.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, letraDos));

        meterStart.setText(String.valueOf(Static_variables.selected.metros.get(0).currentValue));

        total.setText("0");

        TextWatcher a = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (meterStart.getText().length() > 0 && meterEnd.getText().length() > 0) {
                    total.setText(Static_variables.calculoAuto(meterStart.getText().toString(), meterEnd.getText().toString()));
                } else {
                    total.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };


        meterStart.addTextChangedListener(a);
        meterEnd.addTextChangedListener(a);
    }

    public void onTransferContinue(View v) {
        if(total.getText().equals("0")){
            Toast.makeText(getApplicationContext(),"Put the ending meter",Toast.LENGTH_SHORT).show();
        }else {
            completeJson();
        }
    }

    public void onTransferCancel(View v) {
        Static_variables.cancelDialog a= new Static_variables.cancelDialog(this,"¿Cancel new Fueling?",TruckLog.class);
        a.show();
    }

    ArrayList<Truck> tanks;

    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
            Request.Method.GET,
            Static_variables.Apis + "truckFeed",
            null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    // Do something with response
                    //mTextView.setText(response.toString());

                    // Process the JSON
                    try {
                        tanks = new ArrayList<>();
                        // Loop through the array elements
                        for (int i = 0; i < response.length(); i++) {
                            // Get current json object
                            JSONObject truck = response.getJSONObject(i);
                            int id = truck.getInt("id");
                            int balance = truck.getInt("balance");
                            boolean canDispense = truck.getBoolean("canDispense");
                            boolean canDefuel = truck.getBoolean("canDefuel");
                            String code = truck.getString("code");
                            int capacity = truck.getInt("capacity");
                            int eWarehouseType = truck.getInt("eWarehouseType");
                            int itemId = truck.getInt("itemId");
                            String itemName = truck.getString("itemName");
                            String name = truck.getString("name");
                            String lastOperationDate = truck.getString("lastOperationDate");
                            boolean verified =truck.getBoolean("verified");

                            String date = Static_variables.parseDate(lastOperationDate);
                            JSONArray metros = truck.getJSONArray("meters");
                            ArrayList<Truck.meter> meters = new ArrayList<>();
                            for (int j = 0; j < metros.length(); j++) {
                                JSONObject metrico = metros.getJSONObject(j);
                                int meterid = metrico.getInt("id");
                                String meterName = metrico.getString("name");
                                int meterCurrentValue = metrico.getInt("currentValue");
                                meters.add(new Truck.meter(meterid, meterName, meterCurrentValue));
                            }
                            if (eWarehouseType == 1 || eWarehouseType == 2 && id != Static_variables.selected.id && Static_variables.selected.itemName.equals(itemName)) {
                                tanks.add(new Truck(id, balance, canDispense, canDefuel,verified, code, capacity, eWarehouseType, itemId, itemName, name, date, meters));
                            }
                            // Get the current student (json object) data
                            // Display the formatted json data in text view
                            //mTextView.append(firstName +" " + lastName +"\nAge : " + age);
                            //mTextView.append("\n\n");
                            System.out.println("NEW TANK" + i);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        cargar();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Do something when error occurred
                    error.printStackTrace();
                }
            }) {
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap();
            headers.put("Authorization", "bearer " + Static_variables.Token);
            return headers;
        }
    };

    public void cargar() {
        int a = 0;
        for (int i = 0; i < tanks.size(); i++) {
            for (int j = 0; j < tanks.get(i).getMetros().size(); j++) {
                a += 1;
            }
        }
        String[] letraDos = new String[a];
        int b = 0;
        for (int i = 0; i < tanks.size(); i++) {
            for (int j = 0; j < tanks.get(i).getMetros().size(); j++) {

                letraDos[b] =tanks.get(i).getMetros().get(j).name;
                b += 1;
            }
        }


        spinnerDos.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, letraDos));
        final TextView to_balance, to_capacity;
        final ProgressBar to_progress;
        to_balance = (TextView) findViewById(R.id.to_balance);
        to_capacity = (TextView) findViewById(R.id.to_capacity);
        to_progress = (ProgressBar) findViewById(R.id.to_progress);
        spinnerDos.setOnItemSelectedListener(new  AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int metro;
                for (int h = 0; h < tanks.size(); h++) {
                    for (int j = 0; j < tanks.get(h).getMetros().size(); j++) {
                        if (spinnerDos.getSelectedItem().toString().equals(tanks.get(h).getMetros().get(j).name)) {
                            selected = tanks.get(h);
                            metro = j;
                        }
                    }
                }
                update(to_balance, to_capacity, to_progress, selected.getBalance(), selected.getCapacity());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void update(TextView a, TextView b, ProgressBar c, int balance, int capacity) {
        a.setText(String.valueOf(balance));
        b.setText(String.valueOf(capacity));
        c.setMax(capacity);
        c.setProgress(balance);
    }
    ProgressDialog dd;

    private void completeJson() {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        dd = new ProgressDialog(this);
        dd.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparente)));
        dd.show();
        dd.setContentView(R.layout.dialogo_progreso);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String d = df.format(Calendar.getInstance().getTime());
        String date = d;
        JSONObject MyData = new JSONObject();

        try {
            MyData.put("operationDate", date);
            Truck tank = null;
            int metro;
            for (int i = 0; i < tanks.size(); i++) {
                for (int j = 0; j < tanks.get(i).getMetros().size(); j++) {
                    if (spinnerDos.getSelectedItem().toString().equals(tanks.get(i).getMetros().get(j).name)) {
                        tank = tanks.get(i);
                        metro = j;
                    }
                }
            }
            MyData.put("warehouseFromId", Static_variables.selected.id);
            MyData.put("warehouseFromMeterId", Static_variables.selected.getMetros().get(spinner.getSelectedItemPosition()).id);
            MyData.put("warehouseFromStartingMeter", meterStart.getText());
            MyData.put("warehouseFromEndingMeter", meterEnd.getText());
            MyData.put("itemId", 1);
            MyData.put("warehouseToId", tank.id);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST, Static_variables.Apis + "topOff", MyData,
                new Response.Listener<JSONObject>() {

                    @Override

                    public void onResponse(JSONObject response) {
                        Intent a = new Intent(getApplicationContext(), TruckLog.class);
                        a.putExtra("status", "Transfer Completed!");
                        startActivity(a);
                        dd.dismiss();
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("Authorization", "bearer " + Static_variables.Token);
                return headers;
            }
        };
            // Adding request to request queue
        MyRequestQueue.add(strReq);
}
}