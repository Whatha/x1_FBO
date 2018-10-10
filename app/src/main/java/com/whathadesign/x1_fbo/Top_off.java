package com.whathadesign.x1_fbo;

import android.app.Activity;
import android.app.Dialog;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mindorks.placeholderview.PlaceHolderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Top_off extends AppCompatActivity {

    private Activity actividad;
    EditText meterStart,meterEnd;
    TextView balance,capacity,total,tank;
    ProgressBar tankProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_off);

        actividad=this;

        meterStart=(EditText)findViewById(R.id.startingmeter);
        meterEnd=(EditText)findViewById(R.id.endingmeter);
        total=(TextView)findViewById(R.id.total);
        balance=(TextView)findViewById(R.id.tank_balance);
        capacity=(TextView)findViewById(R.id.tank_capacity);
        tankProgress =(ProgressBar)findViewById(R.id.tank_progress);
        tank=(TextView)findViewById(R.id.tank);

        Static_variables.set_down_info_bar(this.findViewById(android.R.id.content));
        Static_variables.setupDrawer(this,"New Top Off");
        Spinner spinner = (Spinner) findViewById(R.id.transfer_spinner_uno);

        String[] letra =new String[Static_variables.selected.getMetros().size()];
        for (int i = 0; i < letra.length; i++) {
            letra[i]=Static_variables.selected.getMetros().get(i).name;
        }
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, letra));

        balance.setText(String.valueOf(Static_variables.farm_selected.balance));
        capacity.setText(String.valueOf(Static_variables.farm_selected.capacity));
        tankProgress.setMax(Static_variables.farm_selected.capacity);
        tankProgress.setProgress(Static_variables.farm_selected.balance);
        tank.setText(Static_variables.farm_selected.name);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                meterStart.setText(String.valueOf(Static_variables.selected.metros.get(i).currentValue));
                sell=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
int sell;
    public void onTopoffContinue(View v){
        CustomDialog dialog= new CustomDialog(this, sell);
        dialog.show();

    }
    public void onTopoffCancel(View v){
        Static_variables.cancelDialog a= new Static_variables.cancelDialog(this,"¿Cancel Top off?",TruckLog.class);
        a.show();
    }


    private class CustomDialog extends Dialog implements
            android.view.View.OnClickListener {
        public Activity activity;
        public Button complete;
        String texto;
        int i;
        public CustomDialog(Activity activity,int i) {
            super(activity);
            this.activity = activity;
            this.i=i;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.top_off_dialog);
            TextView balance,capacity;
            TextView farm_name,st_m,en_m,opbalance,newbalance,totalg;
            capacity=(TextView)findViewById(R.id.tank_capacity);
            balance=(TextView)findViewById(R.id.tank_balance);
            farm_name=(TextView)findViewById(R.id.farm_name);
            st_m=(TextView)findViewById(R.id.st_m);
            en_m=(TextView)findViewById(R.id.en_m);
            opbalance=(TextView)findViewById(R.id.opbalance);
            newbalance=(TextView)findViewById(R.id.newbalance);
            totalg=(TextView)findViewById(R.id.total_gals);

            ProgressBar tankProgress;
            tankProgress =(ProgressBar)findViewById(R.id.tank_progress);


            balance.setText(String.valueOf(Static_variables.farm_selected.balance));
            capacity.setText(String.valueOf(Static_variables.farm_selected.capacity));
            tankProgress.setMax(Static_variables.farm_selected.capacity);
            tankProgress.setProgress(Static_variables.farm_selected.balance-Integer.parseInt(total.getText().toString()));
            farm_name.setText(Static_variables.farm_selected.name);
            opbalance.setText(String.valueOf(Static_variables.farm_selected.balance));
            newbalance.setText(String.valueOf(Static_variables.farm_selected.balance-Integer.parseInt(total.getText().toString())));
            st_m.setText(String.valueOf(Static_variables.selected.getMetros().get(i).currentValue));
            en_m.setText(meterEnd.getText().toString());
            totalg.setText(total.getText().toString());
            Button complete= (Button) findViewById(R.id.ticket_complete);
            Button cancel= (Button) findViewById(R.id.ticket_cancel);
            cancel.setOnClickListener(this);
            complete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ticket_complete:
                    completeJson();
                    break;
                case R.id.ticket_cancel:
                    dismiss();
                    break;
                default:
                    break;
            }
        }
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
            MyData.put("warehouseFromId", Static_variables.farm_selected.id);
            MyData.put("warehouseFromMeterId", Static_variables.farm_selected.getMetros().get(0).id);
            MyData.put("warehouseFromStartingMeter", meterStart.getText());
            MyData.put("warehouseFromEndingMeter", meterEnd.getText());
            MyData.put("itemId", 1);
            MyData.put("warehouseToId", Static_variables.selected.id);

            System.out.println("DATOS: "+MyData);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST, Static_variables.Apis + "TopOff", MyData,
                new Response.Listener<JSONObject>() {

                    @Override

                    public void onResponse(JSONObject response) {
                        Intent a= new Intent(getApplicationContext(),TruckLog.class);
                        a.putExtra("status","Topoff Completed!");
                        startActivity(a);
                        dd.dismiss();
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_SHORT).show();
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
