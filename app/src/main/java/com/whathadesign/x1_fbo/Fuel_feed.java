package com.whathadesign.x1_fbo;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.mindorks.placeholderview.PlaceHolderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Fuel_feed extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context myContext;
    private Toolbar mTopToolbar;
    CustomDialog customDialog;
    private PlaceHolderView mGalleryView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_feed);
        findViewById(R.id.fuel_feed_layou).setOnTouchListener(new View.OnTouchListener() {
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
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_feed);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(myContext, 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        orders = new ArrayList<Order>();

        // specify an adapter (see also next example)

        Static_variables.set_down_info_bar(this.findViewById(android.R.id.content));
        Static_variables.setupDrawer(this, "Pending Fuelings");

        RequestQueue ExampleRequestQueue = Volley.newRequestQueue(this);
        ExampleRequestQueue.add(jsonArrayRequest);
        act = this;
        String mensaje = getIntent().getStringExtra("status");
        customDialog = new CustomDialog(this, Static_variables.getSelected().name);

        // customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (mensaje == null && !Static_variables.selected.verified) {
            customDialog.setCanceledOnTouchOutside(false);
            customDialog.setCancelable(false);
            customDialog.show();
            customDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);

        } else if (mensaje != null) {
            if (!mensaje.equals("holi")) {

                toastsito(mensaje);
            }
        }
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.blanco);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Esto se ejecuta cada vez que se realiza el gesto
                RequestQueue ExampleRequestQueue = Volley.newRequestQueue(myContext);
                ExampleRequestQueue.add(jsonArrayRequest);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "You can't go back", Toast.LENGTH_SHORT).show();
    }

    private List<Order> orders;

    // This method creates an ArrayList that has three Person objects
// Checkout the project associated with this tutorial on Github if
// you want to use the same images.

    public void actualize() {
        mAdapter = new Fuel_adapter(orders, myContext, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    public class CustomDialog extends Dialog implements
            android.view.View.OnClickListener {
        public Activity activity;
        public Button save, cancel;
        String texto;

        public CustomDialog(Activity activity, String texto) {
            super(activity);
            this.activity = activity;
            this.texto = texto;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);

            setContentView(R.layout.custom_dialog);
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.custom_dialog_main);
            layout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    return true;
                }
            });
            save = (Button) findViewById(R.id.dialog_save);
            cancel = (Button) findViewById(R.id.dialog_cancel);
            RelativeLayout hide = (RelativeLayout) findViewById(R.id.hiding_meter);
            if (Static_variables.selected.getMetros().size() == 1) {
                hide.setVisibility(View.GONE);
            }
            save.setOnClickListener(this);
            cancel.setOnClickListener(this);

            TextView text = (TextView) findViewById(R.id.dialog_text);
            text.setText(texto);
            //order_quemada();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_save:
                    EditText meter_uno = (EditText) findViewById(R.id.dialog_meters_uno);
                    EditText meter_dos = (EditText) findViewById(R.id.dialog_meters_dos);
                    if (meter_uno.getText().length() < 1) {
                        Toast.makeText(getContext(), "Fill meter one", Toast.LENGTH_SHORT).show();
                    }
                    if (Static_variables.selected.getMetros().size() > 1) {
                        if (meter_dos.getText().length() < 1) {
                            Toast.makeText(getContext(), "Fill meter two", Toast.LENGTH_SHORT).show();
                        } else {
                            UpdateMeters(Static_variables.selected, Integer.parseInt(meter_uno.getText().toString()), Integer.parseInt(meter_dos.getText().toString()));
                        }
                    } else {

                        UpdateMeters(Static_variables.selected, Integer.parseInt(meter_uno.getText().toString()), 0);

                    }
                    break;
                case R.id.dialog_cancel:
                    Intent a = new Intent(getApplicationContext(), truck_feed.class);
                    activity.startActivity(a);
                    break;
                default:
                    break;
            }
        }
    }


    Boolean visible = false;

    public void toastsito(String mensaje) {
        final ViewGroup transitionsContainer = (ViewGroup) findViewById(R.id.fuel_feed_layou);
        final RelativeLayout layout;
        layout = (RelativeLayout) findViewById(R.id.toast_layout);
        TextView toast_text = (TextView) findViewById(R.id.toast_text);
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

    String fechaUno = "2018-05-01";
    String fechaDos = "2018-06-23";
    ProgressDialog dd;

    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
            Request.Method.GET,
            Static_variables.Apis + "pendingFueling/0/0/",
            null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    // Do something with response
                    //mTextView.setText(response.toString());
                    dd = new ProgressDialog(act);
                    dd.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparente)));
                    dd.show();
                    dd.setContentView(R.layout.dialogo_progreso);
                    // Process the JSON
                    try {


                        Static_variables.icaos = new HashSet<>();
                        Static_variables.tail_numbers = new HashSet<>();
                        orders=new ArrayList<>();
                        // Loop through the array elements
                        for (int i = 0; i < response.length(); i++) {
                            // Get current json object
                            JSONObject order = response.getJSONObject(i);
                            int requestDetailId = order.getInt("requestDetailId");
                            int tailAircraftId = order.getInt("tailAircraftId");

                            String companyName = order.getString("companyName");
                            int serviceId = order.getInt("serviceId");
                            String itemName = order.getString("itemName");
                            String tailNbr = order.getString("tailNbr");

                            String icao = order.getString("icao");
                            String arrivalDate = order.getString("arrivalDate");
                            String departureDate = order.getString("departureDate");
                            String arrivalEstimateDate = order.getString("arrivalEstimateDate");
                            String departureEstimateDate = order.getString("departureEstimateDate");
                            String fuelOn = order.getString("fuelOn");
                            String qty = order.getString("qty");
                            String notes = order.getString("notes");
                            Static_variables.icaos.add(icao);
                           // int itemId=order.getInt("itemId");
                            Static_variables.tail_numbers.add(tailNbr);
                            int eStatus = order.getInt("eStatus");
                            boolean fsii = order.getBoolean("fsii");
                            String model = order.getString("model"), manufacturer = order.getString("manufacturer");
                            String fum_Qty_Info=order.getString("fum_Qty_Info");
                            String date = departureDate;

                            System.out.println("FUCK " + Static_variables.selected.itemName +" "+itemName);
                             // if(Static_variables.selected.itemId == itemId) {
                            orders.add(new Order(requestDetailId, tailAircraftId, serviceId, qty, companyName, itemName, tailNbr, icao, arrivalDate, departureDate, arrivalEstimateDate, departureEstimateDate, fuelOn, eStatus, fsii, model, manufacturer, notes,fum_Qty_Info));
                            //}
                            // Get the current student (json object) data
                            // Display the formatted json data in text view
                            //mTextView.append(firstName +" " + lastName +"\nAge : " + age);
                            //System.out.println("Order: "+i+" dp: "+departureDate);
                            //mTextView.append("\n\n");
                        }
                        dd.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dd.dismiss();
                    } finally {
                        actualize();
                        dd.dismiss();
                        swipeRefreshLayout.setRefreshing(false);

                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Do something when error occurred
                    error.printStackTrace();
                    dd.dismiss();
                }
            }) {
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap();
            headers.put("Authorization", "bearer " + Static_variables.Token);
            return headers;
        }
    };

    public void order_quemada() {
        actualize();

    }

    ProgressDialog dialog;

    private void UpdateMeters(final Truck t, final int newCurrent1, final int newCurrent2) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());

        dialog = new ProgressDialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparente)));
        dialog.show();
        dialog.setContentView(R.layout.dialogo_progreso);

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

        CustomDos req = new CustomDos(Request.Method.POST, Static_variables.Apis + "addOpenVerifiedTruck", cosito,
                new Response.Listener<JSONArray>() {

                    @Override

                    public void onResponse(JSONArray response) {
                        Truck a = Static_variables.selected;
                        a.getMetros().get(0).currentValue = newCurrent1;
                        if (a.getMetros().size() > 1) {
                            a.getMetros().get(1).currentValue = newCurrent2;
                        }
                        Static_variables.updateTruck(act);
                        customDialog.dismiss();
                        dialog.dismiss();
                        toastsito("Meters updated!");
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();

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
/*
    static class CustomJsonArrayRequest extends JsonRequest<JSONObject> {

        /**
         * Creates a new request.
         *
         * @param method        the HTTP method to use
         * @param url           URL to fetch the JSON from
         * @param jsonRequest   A {@link JSONObject} to post with the request. Null is allowed and
         *                      indicates no parameters will be posted along with request.
         * @param listener      Listener to receive the JSON response
         * @param errorListener Error listener, or null to ignore errors.
         *//*
        public CustomJsonArrayRequest(int method, String url, JSONArray jsonRequest,
                                      Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
            super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener,
                    errorListener);
        }

        @Override
        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
            try {
                String jsonString = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                return Response.success(new JSONObject(jsonString),
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JSONException je) {
                return Response.error(new ParseError(je));
            }
        }
    }
*/
    public void refresh(View v) {
        orders = new ArrayList<>();
        actualize();
        RequestQueue ExampleRequestQueue = Volley.newRequestQueue(this);
        ExampleRequestQueue.add(jsonArrayRequest);
    }


    static class CustomDos extends JsonRequest<JSONArray> {

        /**
         * Creates a new request.
         *
         * @param method        the HTTP method to use
         * @param url           URL to fetch the JSON from
         * @param jsonRequest   A {@link JSONObject} to post with the request. Null is allowed and
         *                      indicates no parameters will be posted along with request.
         * @param listener      Listener to receive the JSON response
         * @param errorListener Error listener, or null to ignore errors.
         */
        public CustomDos(int method, String url, JSONArray jsonRequest,
                         Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
            super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener,
                    errorListener);
        }

        @Override
        protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
            try {
                String jsonString = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                return Response.success(new JSONArray(jsonString),
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JSONException je) {
                return Response.error(new ParseError(je));
            }
        }
    }


}