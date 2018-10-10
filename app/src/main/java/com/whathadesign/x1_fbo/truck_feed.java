package com.whathadesign.x1_fbo;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class truck_feed extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context myContext;
    Button all, avgas, jeta;
    RequestQueue ExampleRequestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_feed);
        myContext = getApplicationContext();
        mRecyclerView = (RecyclerView) findViewById(R.id.truck_recycler);
        all = (Button) findViewById(R.id.all_btn);
        avgas = (Button) findViewById(R.id.avgas_btn);
        jeta = (Button) findViewById(R.id.jeta_btn);
        RelativeLayout logout=(RelativeLayout)findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a= new Intent(getApplicationContext(),Login.class);
                startActivity(a);
            }
        });
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        System.out.println("ANCHO: " + width + " ALTO: " + height);
        if (width > 800) {
            mLayoutManager = new GridLayoutManager(myContext, 2);
        } else {
            mLayoutManager = new GridLayoutManager(myContext, 1);
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        trucks = new ArrayList<Truck>();
        ExampleRequestQueue = Volley.newRequestQueue(this);
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy( 50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy( 50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ExampleRequestQueue.add(jsonArrayRequest);
        ExampleRequestQueue.add(jsonObjReq);
        ExampleRequestQueue.add(gerModels);

// Adding the request to the queue along with a unique string tag

        // specify an adapter (see also next example)
//camion_quemado();
        initializeData(0);

    }

    public void keepSeending(){

    }


    public void truck_feed_all(View v) {
        all.setBackgroundResource(R.drawable.left_tint);
        avgas.setBackgroundResource(R.drawable.right_border);
        jeta.setBackgroundResource(R.drawable.center_border);

        all.setTextColor(getResources().getColor(R.color.blanco));
        avgas.setTextColor(getResources().getColor(R.color.colorPrimary));
        jeta.setTextColor(getResources().getColor(R.color.colorPrimary));
        initializeData(0);
    }

    public void truck_feed_jeta(View v) {
        all.setBackgroundResource(R.drawable.left_border);
        avgas.setBackgroundResource(R.drawable.right_border);
        jeta.setBackgroundResource(R.drawable.center_tint);

        all.setTextColor(getResources().getColor(R.color.colorPrimary));
        avgas.setTextColor(getResources().getColor(R.color.colorPrimary));
        jeta.setTextColor(getResources().getColor(R.color.blanco));
        initializeData(1);

    }

    public void truck_feed_avgas(View v) {
        all.setBackgroundResource(R.drawable.left_border);
        avgas.setBackgroundResource(R.drawable.right_tint);
        jeta.setBackgroundResource(R.drawable.center_border);

        all.setTextColor(getResources().getColor(R.color.colorPrimary));
        avgas.setTextColor(getResources().getColor(R.color.blanco));
        jeta.setTextColor(getResources().getColor(R.color.colorPrimary));
        initializeData(2);

    }

    private List<Truck> trucks;

    // This method creates an ArrayList that has three Person objects
// Checkout the project associated with this tutorial on Github if
// you want to use the same images.
    private void initializeData(int filtro) {

        switch (filtro) {
            case 0:
                mAdapter = new truck_adapter(trucks, myContext, this);
                mRecyclerView.setAdapter(mAdapter);
                break;
            case 1:
                ArrayList<Truck> a = new ArrayList<Truck>();
                for (int i = 0; i < trucks.size(); i++) {
                    if (trucks.get(i).itemName.equals("Jet A")) {
                        a.add(trucks.get(i));
                    }
                }
                mAdapter = new truck_adapter(a, myContext, this);
                mRecyclerView.setAdapter(mAdapter);
                break;

            case 2:

                ArrayList<Truck> b = new ArrayList<Truck>();
                for (int i = 0; i < trucks.size(); i++) {
                    if (trucks.get(i).itemName.equals("Avgas")) {
                        b.add(trucks.get(i));
                    }
                }
                mAdapter = new truck_adapter(b, myContext, this);
                mRecyclerView.setAdapter(mAdapter);
                break;
        }
    }


    //URL of the request we are sending

    /*
     * JsonObjectRequest takes in five paramaters
     * Request Type - This specifies the type of the request eg: GET,POST
     * URL          - This String param specifies the Request URL
     * JSONObject   - This parameter takes in the POST parameters."null" in
     *                  case of GET request.
     * Listener     -This parameter takes in a implementation of Response.Listener()
     *                 interface which is invoked if the request is successful
     * Listener     -This parameter takes in a implementation of Error.Listener()
     *               interface which is invoked if any error is encountered while processing
     *               the request
     */
    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
            Static_variables.Apis + "userCredentials", null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JSONObject user = response;

                    try {
                        int id = user.getInt("id");
                        String firstName=user.getString("firstName");
                        String lastName=user.getString("lastName");
                        String email=user.getString("email");
                        String fbo=user.getString("fboName");

                        Static_variables.user=new User(id,firstName,lastName,email,fbo);
                        System.out.println("USUARIO"+id + firstName+ lastName+email);
                    } catch (JSONException e) {
                        System.out.println("ERROOOOOOOOOOR");
                        ExampleRequestQueue.add(jsonObjReq);
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    //Failure Callback
                    error.printStackTrace();
                }
            })

    {
        /** Passing some request headers* */
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap();
            headers.put("Authorization", "bearer " + Static_variables.Token);
            return headers;
        }
    };



    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
            Request.Method.GET,
            Static_variables.Apis + "truckFeed",
            null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    // Do something with response
                    //mTextView.setText(response.toString());
                    ProgressBar bar=(ProgressBar)findViewById(R.id.waiting);
                    bar.setVisibility(View.GONE);
                    // Process the JSON
                    try {
                        Static_variables.farm=new ArrayList<>();
                        // Loop through the array elements
                        for (int i = 0; i < response.length(); i++) {
                            // Get current json object
                            JSONObject truck = response.getJSONObject(i);
                            int id = truck.getInt("id");
                            int balance = truck.getInt("balance");
                            boolean canDispense = truck.getBoolean("canDispense");
                            boolean canDefuel = truck.getBoolean("canDefuel");
                            boolean verified =truck.getBoolean("verified");
                            String code = truck.getString("code");
                            int capacity = truck.getInt("capacity");
                            int eWarehouseType = truck.getInt("eWarehouseType");
                            int itemId = truck.getInt("itemId");
                            int masterItemId = truck.getInt("masterItemId");

                            String itemName = truck.getString("itemName");
                            String name = truck.getString("name");
                            String lastOperationDate = truck.getString("lastOperationDate");

                            String date= Static_variables.parseDate(lastOperationDate);
                            JSONArray metros=truck.getJSONArray("meters");
                            ArrayList<Truck.meter>meters = new ArrayList<>();
                            for (int j = 0; j < metros.length() ; j++) {
                                JSONObject metrico=metros.getJSONObject(j);
                                int meterid = metrico.getInt("id");
                                String meterName= metrico.getString("name");
                                int meterCurrentValue= metrico.getInt("currentValue");
                                meters.add(new Truck.meter(meterid,meterName,meterCurrentValue));
                            }
                           // trucks.add(new Truck(id, balance, canDispense, canDefuel, code, capacity, eWarehouseType, itemId, itemName, name, date, meters));

                            if(eWarehouseType==1 ) {
                                Static_variables.farm.add(new Truck(id, balance, canDispense, canDefuel,verified,code, capacity, eWarehouseType, itemId, itemName, name, date, meters));
                            }else
                                if(eWarehouseType==2 || eWarehouseType==3) {
                                    System.out.println("AÑADIDOOOOOO: "+id+" "+masterItemId+"Verified: "+verified);

                                    trucks.add(new Truck(id, balance, canDispense, canDefuel,verified, code, capacity, eWarehouseType, itemId,masterItemId, itemName, name, date, meters));
                            }
                            if(eWarehouseType==3) {
                                Static_variables.hydrants.add(new Truck(id, balance, canDispense, canDefuel,verified, code, capacity, eWarehouseType, itemId, itemName, name, date, meters));
                            }
                            // Get the current student (json object) data
                            // Display the formatted json data in text view
                            //mTextView.append(firstName +" " + lastName +"\nAge : " + age);
                            //mTextView.append("\n\n");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ExampleRequestQueue.add(jsonArrayRequest);

                    }finally {
                        initializeData(0);
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

    JsonArrayRequest gerModels = new JsonArrayRequest(

            Request.Method.GET,
            Static_variables.Apis + "GetAllModels",
            null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    ArrayList <model> models=new ArrayList<>();

                    // Do something with response
                    //mTextView.setText(response.toString());
                    ProgressBar bar=(ProgressBar)findViewById(R.id.waiting);
                    bar.setVisibility(View.GONE);
                    // Process the JSON
                    try {
                        // Loop through the array elements
                        for (int i = 0; i < response.length(); i++) {
                            // Get current json object
                            JSONObject truck = response.getJSONObject(i);
                            int id = truck.getInt("id");

                            String manufacturerModel = truck.getString("manufacturerModel");
                            String icao = truck.getString("icao");

                            models.add(new model(id,manufacturerModel,icao));
                            // Get the current student (json object) data
                            // Display the formatted json data in text view
                            //mTextView.append(firstName +" " + lastName +"\nAge : " + age);
                            //mTextView.append("\n\n");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ExampleRequestQueue.add(gerModels);

                    }finally {
                    Static_variables.models=models;
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

    public void camion_quemado(){
        ArrayList<Truck.meter>meters = new ArrayList<>();
        meters.add(new Truck.meter(0,"meter",2000));
        meters.add(new Truck.meter(0,"meter2",2034200));

        Static_variables.user=new User(0,"User","Name","username@x1fbo.com","FBO3");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
