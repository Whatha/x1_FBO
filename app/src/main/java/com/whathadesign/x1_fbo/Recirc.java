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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mindorks.placeholderview.PlaceHolderView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.whathadesign.x1_fbo.Static_variables.selected;

public class Recirc extends AppCompatActivity {
    private Activity actividad;
    EditText meter1start,meter1end,meter2start,meter2end;
    TextView meter1total,meter2total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recirc);

        actividad=this;

        Static_variables.set_down_info_bar(this.findViewById(android.R.id.content));
        Static_variables.setupDrawer(this,"New Recirc");

        meter1start=(EditText)findViewById(R.id.meter1start);
        meter2start=(EditText)findViewById(R.id.meter2start);
        meter1end=(EditText)findViewById(R.id.meter1end);
        meter2end=(EditText)findViewById(R.id.meter2end);
        meter1total=(TextView)findViewById(R.id.meter1total);
        meter2total=(TextView)findViewById(R.id.meter2total);
        meter1start.setText(String.valueOf(selected.metros.get(0).currentValue));
        if(selected.metros.size()>1)
            meter2start.setText(String.valueOf(selected.metros.get(1).currentValue));
        meter1total.setText("0");
        meter2total.setText("0");
        LinearLayout l=(LinearLayout)findViewById(R.id.removable);
        if(selected.getMetros().size()==1){
            l.setVisibility(View.INVISIBLE);
        }
        TextWatcher a= new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(meter1start.getText().length()>0 && meter1end.getText().length()>0) {
                    meter1total.setText(Static_variables.calculoAuto(meter1start.getText().toString(), meter1end.getText().toString()));
                }else{
                    meter1total.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        TextWatcher b= new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(meter2start.getText().length()>1 && meter2end.getText().length()>1) {
                    meter2total.setText(Static_variables.calculoAuto(meter2start.getText().toString(), meter2end.getText().toString()));
                }else{
                    meter2total.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        meter1start.addTextChangedListener(a);
        meter2start.addTextChangedListener(b);
        meter1end.addTextChangedListener(a);
        meter2end.addTextChangedListener(b);


    }
    public void onTransferContinue(View v){
            if(meter1total.getText().equals("0") && meter2total.getText().equals("0")){
                Toast.makeText(getApplicationContext(),"Please, update one or two meters",Toast.LENGTH_SHORT).show();
            }else if(meter2total.getText().equals("0")){
                recirc(meter1start.getText().toString(),meter1end.getText().toString(),true, selected.getMetros().get(0).id);
            } else if(meter1total.getText().equals("0")){
                recirc(meter2start.getText().toString(),meter2end.getText().toString(),true, selected.getMetros().get(1).id);
            }else{
                recirc(meter1start.getText().toString(),meter1end.getText().toString(),false, selected.getMetros().get(0).id);
                recirc(meter2start.getText().toString(),meter2end.getText().toString(),true, selected.getMetros().get(1).id);
            }
    }
    public void onTransferCancel(View v){
        Static_variables.cancelDialog a= new Static_variables.cancelDialog(this,"Cancel Recirc?",TruckLog.class);
        a.show();
    }
ProgressDialog dd;
    private void recirc(final String meterStart, final String meterEnd,final boolean completed, final int meterID){

        try {
            dd = new ProgressDialog(this);
            dd.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparente)));
            dd.show();
            dd.setContentView(R.layout.dialogo_progreso);
            RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, Static_variables.Apis + "recirculation", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //This code is executed if the server responds, whether or not the response contains data.
                    //The String 'response' contains the server's response.
                    if(completed) {
                        Intent a = new Intent(getApplicationContext(), TruckLog.class);
                        a.putExtra("status", "Recirc Completed!");
                        startActivity(a);
                        dd.dismiss();

                    }
                    System.out.println("UPDATED");
                }
            }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                @Override
                public void onErrorResponse(VolleyError error) {
                    //This code is executed if there is an error.
                    error.printStackTrace();
                }
            }) {
                protected Map<String, String> getParams() {
                    Map<String, String> MyData = new HashMap<String, String>();
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    String date = df.format(Calendar.getInstance().getTime());
                    System.out.println("Fecha"+date);
                    MyData.put("operationDate", date);
                    MyData.put("warehouseId", String.valueOf(selected.id));
                    MyData.put("meterId", String.valueOf(meterID));

                    MyData.put("startingMeter", meterStart);
                    MyData.put("endingMeter", meterEnd);
                    MyData.put("ItemId","1");
                    return MyData;
                }

                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap();
                    headers.put("Authorization", "bearer " + Static_variables.Token);
                    return headers;
                }
            };
            MyRequestQueue.add(MyStringRequest);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("EMOSIDO ENGAÑADO");
        }

    }

}
