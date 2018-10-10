package com.whathadesign.x1_fbo;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mindorks.placeholderview.PlaceHolderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class TruckLog extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context myContext;
    private Activity act;
    Button yesterday, today;
    RequestQueue ExampleRequestQueue;
    ArrayList<Log> logs=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_log);
        findViewById(R.id.truck_log_main).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });
        RelativeLayout layout;
        layout = (RelativeLayout) findViewById(R.id.toast_layout);
        layout.setVisibility(View.GONE);
        myContext = getApplicationContext();
        act=this;
        yesterday = (Button) findViewById(R.id.yesterday_btn);
        today = (Button) findViewById(R.id.today_btn);

        Static_variables.set_down_info_bar(this.findViewById(android.R.id.content));
        Static_variables.setupDrawer(this,"Truck Log");
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_feed);

        mRecyclerView.setHasFixedSize(true);
        ExampleRequestQueue = Volley.newRequestQueue(this);
        ExampleRequestQueue.add(jsonArrayRequest);
        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(myContext, 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
     /*   mAdapter = new Fuel_adapter(orders, myContext, this);
        mRecyclerView.setAdapter(mAdapter);*/

        String mensaje= getIntent().getStringExtra("status");
        // customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (mensaje== null) {
        }else{

            toastsito(mensaje);
        }
        initializeData();

    }

    public void today_click(View v) {
        today.setBackgroundResource(R.drawable.bottom_line);
        yesterday.setBackgroundColor(getResources().getColor(R.color.grisclaro));

        today.setTextColor(getResources().getColor(R.color.colorPrimary));
        yesterday.setTextColor(getResources().getColor(R.color.grisletra));
        ExampleRequestQueue.add(jsonArrayRequest);

    }

    public void yesterday_click(View v) {
        today.setBackgroundColor(getResources().getColor(R.color.grisclaro));
        yesterday.setBackgroundResource(R.drawable.bottom_line);

        today.setTextColor(getResources().getColor(R.color.grisletra));
        yesterday.setTextColor(getResources().getColor(R.color.colorPrimary));

        ExampleRequestQueue.add(yesterdayArrayRequest);
    }

    private void initializeData(){

        mAdapter = new Log_adapter(logs, myContext, this);
        mRecyclerView.setAdapter(mAdapter);
    }



    Boolean visible = false;

    public void toastsito(String mensaje) {
        final ViewGroup transitionsContainer = (ViewGroup) findViewById(R.id.truck_log_main);
        final RelativeLayout layout;
        layout = (RelativeLayout) findViewById(R.id.toast_layout);
        TextView toast_text= (TextView)findViewById(R.id.toast_text);
        toast_text.setText(mensaje);
        layout.setVisibility(View.GONE);
        /*layout.setScaleY(2);
        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        layout.startAnimation(slide_up);*/
        transicioni(transitionsContainer, layout);
        CountDownTimer a = new CountDownTimer(2000, 100) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                transicioni(transitionsContainer, layout);
            }
        }.start();
    }

    public void transicioni(ViewGroup transitionsContainer, View v) {
        Transition t = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            t = new Slide(Gravity.BOTTOM);
        }
        t.setDuration(500);
        t.setInterpolator(new FastOutSlowInInterpolator());
        t.setStartDelay(500);
        TransitionManager.beginDelayedTransition(transitionsContainer, t);

        visible = !visible;
        v.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    String fechaUno="2018-04-15";
    String fechaDos="2018-05-2";
    ProgressDialog dd;
    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
            Request.Method.GET,
            Static_variables.Apis + "operationTruck/"+Static_variables.selected.id,
            null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    // Do something with response
                    //mTextView.setText(response.toString());
                    // Process the JSON
                    try {
                        dd = new ProgressDialog(act);
                        dd.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparente)));
                        dd.show();
                        dd.setContentView(R.layout.dialogo_progreso);

                        logs = new ArrayList<>();

                        // Loop through the array elements
                        for (int i = 0; i < response.length(); i++) {
                            // Get current json object
                            JSONObject log = response.getJSONObject(i);
                            int id = log.getInt("id");
                            String operationDate = log.getString("operationDate"), operationDescription = log.getString("operationDescription"), operationNbr=log.getString("operationNbr");
                            int operationType=log.getInt("operationType");
                            System.out.println("FECHINI: "+operationDate);
                            String tailNbr=log.getString("tailNbr");
                            String icao=log.getString("icao");

                            ArrayList<Log.OperationDetails> details=new ArrayList<>();
                            JSONArray metros=log.getJSONArray("operationDetails");
                            for (int j = 0; j < metros.length() ; j++) {
                                JSONObject metrico=metros.getJSONObject(j);
                                int details_id= metrico.getInt("id"), operationId=metrico.getInt("operationId"),eOperationDetailType=metrico.getInt("eOperationDetailType"),itemId=metrico.getInt("itemId"),warehouseId=metrico.getInt("warehouseId"),qty=metrico.getInt("qty");
                              String fueler=metrico.getString("fueler");
                                String tailAircraftId=metrico.getString("tailAircraftId"),meterName=metrico.getString("meterName"),meterId=metrico.getString("meterId"),startingMeter=metrico.getString("startingMeter"),endingMeter=metrico.getString("endingMeter"),companyId=metrico.getString("companyId");
                                details.add(new Log.OperationDetails(details_id,operationId,eOperationDetailType,itemId,warehouseId,meterId,qty,startingMeter,endingMeter,companyId,tailAircraftId,meterName,fueler));
                            }
                            System.out.println("LOG: id:"+id+" optDesc: "+operationDescription+" Opt #: "+operationNbr+ " Type: "+operationType+" details: "+details);
                            logs.add(new Log(id,operationDate,operationDescription,operationNbr,operationType,tailNbr,icao,details));

                            // Get the current student (json object) data
                            // Display the formatted json data in text view
                            //mTextView.append(firstName +" " + lastName +"\nAge : " + age);
                            //mTextView.append("\n\n");
                            System.out.println("Llego el "+id);
                        }
                        dd.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        dd.dismiss();

                    }finally {
                        initializeData();
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

    JsonArrayRequest yesterdayArrayRequest = new JsonArrayRequest(
            Request.Method.GET,
            Static_variables.Apis + "operationPenultimateTruck/"+Static_variables.selected.id,
            null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    // Do something with response
                    //mTextView.setText(response.toString());
                    // Process the JSON
                    try {
                        dd = new ProgressDialog(act);
                        dd.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparente)));
                        dd.show();
                        dd.setContentView(R.layout.dialogo_progreso);
                        logs = new ArrayList<>();

                        // Loop through the array elements
                        for (int i = 0; i < response.length(); i++) {
                            // Get current json object
                            JSONObject log = response.getJSONObject(i);
                            int id = log.getInt("id");
                            String operationDate = log.getString("operationDate"), operationDescription = log.getString("operationDescription"), operationNbr=log.getString("operationNbr");
                            int operationType=log.getInt("operationType");
                            System.out.println("FECHINI: "+operationDate);
                            String tailNbr=log.getString("tailNbr");
                            String icao=log.getString("icao");

                            ArrayList<Log.OperationDetails> details=new ArrayList<>();
                            JSONArray metros=log.getJSONArray("operationDetails");
                            for (int j = 0; j < metros.length() ; j++) {
                                JSONObject metrico=metros.getJSONObject(j);
                                int details_id= metrico.getInt("id"), operationId=metrico.getInt("operationId"),eOperationDetailType=metrico.getInt("eOperationDetailType"),itemId=metrico.getInt("itemId"),warehouseId=metrico.getInt("warehouseId"),qty=metrico.getInt("qty");
                                String fueler=metrico.getString("fueler");
                                String tailAircraftId=metrico.getString("tailAircraftId"),meterName=metrico.getString("meterName"),meterId=metrico.getString("meterId"),startingMeter=metrico.getString("startingMeter"),endingMeter=metrico.getString("endingMeter"),companyId=metrico.getString("companyId");
                                details.add(new Log.OperationDetails(details_id,operationId,eOperationDetailType,itemId,warehouseId,meterId,qty,startingMeter,endingMeter,companyId,tailAircraftId,meterName,fueler));

                            }
                            System.out.println("LOG: id:"+id+" optDesc: "+operationDescription+" Opt #: "+operationNbr+ " Type: "+operationType+" details: "+details);
                            logs.add(new Log(id,operationDate,operationDescription,operationNbr,operationType,tailNbr,icao,details));

                            // Get the current student (json object) data
                            // Display the formatted json data in text view
                            //mTextView.append(firstName +" " + lastName +"\nAge : " + age);
                            //mTextView.append("\n\n");
                            System.out.println("Llego el "+id);
                        }
                        dd.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dd.dismiss();

                    }finally {
                        initializeData();
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

}
