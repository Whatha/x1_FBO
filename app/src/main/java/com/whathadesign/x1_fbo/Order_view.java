package com.whathadesign.x1_fbo;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.ColorDrawable;
import android.os.IBinder;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mindorks.placeholderview.PlaceHolderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.whathadesign.x1_fbo.Static_variables.usbService;

public class Order_view extends AppCompatActivity {
    private Activity actividad;
    private LinearLayout status;
    private Button acept;
    boolean acepted = false;
   public CustomDialog dialog;
    private MyHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);
        actividad = this;
        Static_variables.set_down_info_bar(this.findViewById(android.R.id.content));
        Static_variables.setupDrawer(this, "Fuel Order");
        mHandler = new MyHandler(this,this);

        View.OnTouchListener a = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return false;
            }
        };
        findViewById(R.id.insert_point).setOnTouchListener(a);
        findViewById(R.id.order_main).setOnTouchListener(a);
        findViewById(R.id.main_order_relative).setOnTouchListener(a);
        ImageView fuel_on_img;
        System.out.println("ORDEN FUEL ON: "+Static_variables.order.fuelOn);

        TextView customer, tail, icao, dept_time, fuel_order, jet_type, order_type, qty, note;
        tail = (TextView) findViewById(R.id.card_tail_number);
        icao = (TextView) findViewById(R.id.card_icao);
        dept_time = (TextView) findViewById(R.id.card_dept_time);
        fuel_order = (TextView) findViewById(R.id.card_fuel_order);
        fuel_on_img = (ImageView) findViewById(R.id.card_fuel_on_img);
        customer = (TextView) findViewById(R.id.customer);
        status = (LinearLayout) findViewById(R.id.status);
        jet_type = (TextView) findViewById(R.id.jet_type);
        order_type = (TextView) findViewById(R.id.oder_type);
        qty = (TextView) findViewById(R.id.qty);
        note = (TextView) findViewById(R.id.note);
        Order order = Static_variables.order;
        tail.setText(order.tailNbr);
        icao.setText(order.icao);
        Button fsii=(Button)findViewById(R.id.card_fsii);
        if(!order.departureDate.equals("null")) {
            dept_time.setText(Static_variables.parseDate(order.departureDate));
            dept_time.setTextColor(getResources().getColor(R.color.verne));
            System.out.println("fecha 1:"+Static_variables.parseDate(order.departureDate));

        }else if(!order.DepartureEstimateDate.equals("null")) {
            dept_time.setText(Static_variables.parseDate(order.DepartureEstimateDate));
            System.out.println("fecha 2:"+Static_variables.parseDate(order.DepartureEstimateDate));

        }else{
            dept_time.setText("-");
        }
        customer.setText(order.companyName);
        if(order.type!=null){
            fuel_order.setText(order.type);
        }else {
            fuel_order.setText(order.fum_Qty_Info);
        }
        jet_type.setText(order.model + " " + order.manufacturer);
        order_type.setText(order.itemName);
        qty.setText(order.qty);
        note.setText(order.notes);
        acept = (Button) findViewById(R.id.order_accept);
        if (Static_variables.order.eStatus == 4) {
            cambiar(4);

        }
        acept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (acepted == false) {
                    changeStatus(Static_variables.order.requestDetailId, 4);

                } else {
                    changeStatus(Static_variables.order.requestDetailId, 1);

                }
            }
        });

        if (order.fuelOn != null) {
            if (order.fuelOn.equals("Arrival") || order.fuelOn.equals("arrival") ) {
                fuel_on_img.setImageResource(R.drawable.arrival_icon);
            } else {
                fuel_on_img.setImageResource(R.drawable.departure_icon);
            }
        }

        if (order.fsii) {
                fsii.setBackground(getResources().getDrawable(R.drawable.fsii_1));
            } else {
                fsii.setBackground(getResources().getDrawable(R.drawable.fsii_2));
            }
        RequestQueue ExampleRequestQueue = Volley.newRequestQueue(this);
        ExampleRequestQueue.add(getExtraData);


    }

    public void changeStatusColor(int color) {
        switch (color) {

            case 1:
                status.setBackground(getResources().getDrawable(R.drawable.yellow_bottom_line));
                acepted = false;
                acept.setText("Accept");
                final View myView = findViewById(R.id.iniciar_fueling);
                myView.setVisibility(View.INVISIBLE);
                break;
            case 2:
                status.setBackground(getResources().getDrawable(R.drawable.status_green));
                break;
            case 3:
                status.setBackground(getResources().getDrawable(R.drawable.status_red));
                break;
            case 4:
                status.setBackground(getResources().getDrawable(R.drawable.status_blue));
                acepted = true;
                acept.setText("Cancel");

                final View tt = findViewById(R.id.iniciar_fueling);
                tt.setVisibility(View.VISIBLE);

                break;


        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }

    public void cambiar(int cambio) {
        changeStatusColor(cambio);

    }

    EditText meter1;
    EditText meter1end;
    EditText meter1total;
    EditText meter2;
    EditText meter2end;
    EditText meter2total;

    public void seguir(){
        dd = new ProgressDialog(this);
        dd.setCanceledOnTouchOutside(false);
        dd.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparente)));
        dd.show();
        dd.setContentView(R.layout.dialogo_progreso);
        Toast.makeText(getApplicationContext(),"Start fueling",Toast.LENGTH_SHORT).show();
        Static_variables.fuel(Static_variables.order.tailNbr.toString());
        /*dialog = new Order_view.CustomDialog(this, 1321, 12, Static_variables.order.tailNbr.toString(), Static_variables.order.fsii,10,10);
        dialog.show();*/
    }
    public void onFueling(View v) {
        //completeJson(Static_variables.order, Static_variables.selected, (Order_view) this,123,123);

        seguir();

    }

    public void openText(){
        final LayoutInflater inflater = (LayoutInflater) this.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup parent = (ViewGroup) findViewById(R.id.status);
        final View myView = findViewById(R.id.order_accept);
        final View myView2 = findViewById(R.id.iniciar_fueling);

        myView2.setVisibility(View.INVISIBLE);
        myView.setVisibility(View.INVISIBLE);
        inflater.inflate(R.layout.fueling_start_layout_append, parent);

        meter1 = (EditText) findViewById(R.id.apend_meter1);
        meter1end = (EditText) findViewById(R.id.apend_meter1_end);
        meter1total = (EditText) findViewById(R.id.apend_meter1_total);
        meter2 = (EditText) findViewById(R.id.apend_meter2);
        meter2end = (EditText) findViewById(R.id.apend_meter2_end);
        meter2total = (EditText) findViewById(R.id.apend_meter2_total);
        Button cancel = (Button) findViewById(R.id.fuel_order_cancel);
        Button continuar = (Button) findViewById(R.id.fuel_order_continue);
        TextView[]textos=new TextView[3];
        textos[0]=(TextView)findViewById(R.id.textView3);
        textos[1]=(TextView)findViewById(R.id.textView8);
        textos[2]=(TextView)findViewById(R.id.textView32);

        meter1.setText(String.valueOf(Static_variables.selected.metros.get(0).currentValue));
        if (Static_variables.selected.metros.size() > 1) {
            meter2.setText(String.valueOf(Static_variables.selected.metros.get(1).currentValue));
        }else{
            meter2.setVisibility(View.INVISIBLE);
            meter2end.setVisibility(View.INVISIBLE);
            meter2total.setVisibility(View.INVISIBLE);
            textos[0].setVisibility(View.INVISIBLE);
            textos[1].setVisibility(View.INVISIBLE);
            textos[2].setVisibility(View.INVISIBLE);

        }

        TextWatcher a = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(meter1end.isFocused()) {
                    if (meter1.getText().length() > 0 && meter1end.getText().length() > 0) {
                        meter1total.setText(Static_variables.calculoAuto(meter1.getText().toString(), meter1end.getText().toString()));
                    } else {
                        meter1total.setText("0");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        TextWatcher b = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(meter2end.isFocused()) {
                    if (meter2.getText().length() > 0 && meter2end.getText().length() > 0) {
                        meter2total.setText(Static_variables.calculoAuto(meter2.getText().toString(), meter2end.getText().toString()));
                    } else {
                        meter2total.setText("0");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                 /*   if(editable==meter2end.getText()){
                    if (meter2.getText().length() > 0 && meter2end.getText().length() > 0) {
                        meter2total.setText(Static_variables.calculoAuto(meter2.getText().toString(), meter2end.getText().toString()));
                    } else {
                        meter2total.setText("0");
                    }
                    }else if (editable==meter2total.getText()){
                        if (meter2.getText().length() > 0 && meter2total.getText().length() > 0) {
                            meter2end.setText(Static_variables.calculoAuto(meter2total.getText().toString(), meter2.getText().toString()));
                        } else {
                            meter2end.setText("0");
                        }
                    }*/
            }
        };

        TextWatcher c = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(meter1total.isFocused()) {
                    if (meter1.getText().length() > 0 && meter1total.getText().length() > 0) {
                        meter1end.setText(Static_variables.calculoAutoDos(meter1.getText().toString(), meter1total.getText().toString()));
                    } else {
                        meter1end.setText("0");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
               /* if(editable==meter2end.getText()){
                    if (meter2.getText().length() > 0 && meter2end.getText().length() > 0) {
                        meter2total.setText(Static_variables.calculoAuto(meter2.getText().toString(), meter2end.getText().toString()));
                    } else {
                        meter2total.setText("0");
                    }
                }else if (editable==meter2total.getText()){
                    if (meter2.getText().length() > 0 && meter2total.getText().length() > 0) {
                        meter2end.setText(Static_variables.calculoAuto(meter2total.getText().toString(), meter2.getText().toString()));
                    } else {
                        meter2end.setText("0");
                    }
                }*/
            }
        };

        TextWatcher d = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(meter2total.isFocused()) {
                    if (meter2.getText().length() > 0 && meter2total.getText().length() > 0) {
                        meter2end.setText(Static_variables.calculoAutoDos(meter2.getText().toString(), meter2total.getText().toString()));
                    } else {
                        meter2end.setText("0");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
               /* if(editable==meter2end.getText()){
                    if (meter2.getText().length() > 0 && meter2end.getText().length() > 0) {
                        meter2total.setText(Static_variables.calculoAuto(meter2.getText().toString(), meter2end.getText().toString()));
                    } else {
                        meter2total.setText("0");
                    }
                }else if (editable==meter2total.getText()){
                    if (meter2.getText().length() > 0 && meter2total.getText().length() > 0) {
                        meter2end.setText(Static_variables.calculoAuto(meter2total.getText().toString(), meter2.getText().toString()));
                    } else {
                        meter2end.setText("0");
                    }
                }*/
            }
        };
        meter1.addTextChangedListener(a);
        meter2.addTextChangedListener(b);
        meter1end.addTextChangedListener(a);
        meter2end.addTextChangedListener(b);
        meter1total.addTextChangedListener(c);
        meter2total.addTextChangedListener(d);

       /* meter1total.setEnabled(false);
        meter2total.setEnabled(false);
        meter1total.setKeyListener(null);

        meter2total.setKeyListener(null);*/


        continuar.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                System.out.println("Validacion: "+validacion(meter1total.getText().toString(),meter2total.getText().toString()));
                if(meter1end.getText().length()<1 && meter2end.getText().length()<1){
                    Toast.makeText(getApplicationContext(), "Put the meter info", Toast.LENGTH_SHORT).show();

                }else if(!validacion(meter1total.getText().toString(),meter2total.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Quantity must be more than 0", Toast.LENGTH_SHORT).show();

                }else {
                    //dialog = new CustomDialog(actividad, String.valueOf(Static_variables.order.qty), Static_variables.order.tailNbr.toString(), meter1total.getText().toString(), meter2total.getText().toString(), Static_variables.order.fsii);
                    //dialog.show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.removeViewAt(parent.getChildCount()-1);
                myView.setVisibility(View.VISIBLE);
                myView2.setVisibility(View.VISIBLE);
                changeStatus(Static_variables.order.requestDetailId, 4);

                Intent a= new Intent(getApplicationContext(),Fuel_feed.class);
                startActivity(a);
                /*Intent a = new Intent(getApplicationContext(), Fuel_feed.class);
                a.putExtra("status", "holi");
                startActivity(a);*/
            }
        });
        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrolsito);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        };
        scrollView.post(runnable);
    }

    boolean validacion(String a, String b){
        int uno= 0;
        int dos=0;
        if(!a.equals("")){
            uno=Integer.parseInt(a);
        }
        if(!b.equals("")) {
             dos = Integer.parseInt(b);
        }
        if((uno+dos)>0){
            return true;
        }
        return false;

    }

    @Override
    public void onBackPressed() {
        Static_variables.cancelDialog a= new Static_variables.cancelDialog(this,"Are you sure you want to go  back?",TruckLog.class);
        a.show();
    }

    public static class CustomDialog extends Dialog implements
            android.view.View.OnClickListener {
        public Activity activity;
        public Button complete, cancel;
        private TextView qty_requested, tailnbr, meter1_start, meter2_start, meter1_qty, meter2_qty,meter1_delivered, meter2_delivered, total_text;
        private Button fsii;
        private String  tail;
        private int total, meter1total, meter2total,qty1,qty2;
        private boolean isFsii;
        String texto;

        public CustomDialog(Activity activity, int qty1, int qty2, String tail, boolean fsii, int meter1total, int meter2total) {
            super(activity);
            this.activity = activity;
            this.qty1=qty1;
            this.qty2=qty2;
            this.tail = tail;
            this.meter1total=meter1total;
            this.meter2total=meter2total;
            isFsii = fsii;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.ticket_layout);

            complete = (Button) findViewById(R.id.ticket_complete);
            complete.setOnClickListener(this);
            cancel = (Button) findViewById(R.id.ticket_cancel);
            cancel.setOnClickListener(this);
            qty_requested = (TextView) findViewById(R.id.ticket_qry_rqst);
            tailnbr = (TextView) findViewById(R.id.ticket_tail);
            meter1_start = (TextView) findViewById(R.id.ticket_meter1_start);
            meter2_start = (TextView) findViewById(R.id.ticket_meter2_start);
            meter1_qty = (TextView) findViewById(R.id.ticker_meter1Qty);
            meter2_qty = (TextView) findViewById(R.id.ticker_meter2_qty);
            meter1_delivered = (TextView) findViewById(R.id.ticket_meter1_total);
            meter2_delivered = (TextView) findViewById(R.id.tick_meter2_total);
            fsii = (Button) findViewById(R.id.ticket_fsii);
            //total_text = (TextView) findViewById(R.id.tick_tl);
            int a=0,b=0;


            total = qty1 + qty2;
            tailnbr.setText(tail);
            meter1_delivered.setText(String.valueOf(meter1total));
            meter2_delivered.setText(String.valueOf(meter2total));
            meter1_qty.setText(String.valueOf(qty1));
            meter2_qty.setText(String.valueOf(qty2));
            int mt1=0,mt2=0;

            mt1=meter1total-qty1;
            mt2=meter2total-qty2;

            meter1_start.setText(String.valueOf(mt1));
            meter2_start.setText(String.valueOf(mt2));

            if (isFsii) {
                fsii.setBackground(activity.getResources().getDrawable(R.drawable.fsii_1));
            } else {
                fsii.setBackground(activity.getResources().getDrawable(R.drawable.fsii_2));
            }
            qty_requested.setText(String.valueOf(total));

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ticket_complete:
                    completeJson(Static_variables.order, Static_variables.selected, (Order_view) activity,qty1,qty2);
                    break;
                case R.id.ticket_cancel:
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    }


    private void changeStatus(final int requestDetailId, final int cambio) {
        try {
            RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, Static_variables.Apis + "changeStatus/", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //This code is executed if the server responds, whether or not the response contains data.
                    //The String 'response' contains the server's response.
                    System.out.println("---------RESPUESTA: " + response);
                    Static_variables.order.eStatus = cambio;
                    cambiar(cambio);
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
                    MyData.put("requestDetailId", String.valueOf(requestDetailId)); //Add the data you'd like to send to the server.
                    MyData.put("eStatus", String.valueOf(cambio)); //Add the data you'd like to send to the server.
                    return MyData;
                }

                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap();
                    headers.put("Authorization", "bearer " + Static_variables.Token);
                    return headers;
                }
            };
            MyRequestQueue.add(MyStringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("EMOSIDO ENGAÑADO");
        }

    }
public ProgressDialog dd;
    private static void completeJson(final Order o, final Truck t, final Order_view act, int qty1, int qty2) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(act);
        act.dd = new ProgressDialog(act);
        act.dd.setCanceledOnTouchOutside(false);
        act.dd.getWindow().setBackgroundDrawable(new ColorDrawable(act.getResources().getColor(R.color.transparente)));
        act. dd.show();
        act. dd.setContentView(R.layout.dialogo_progreso);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String d = df.format(Calendar.getInstance().getTime());
        String date = d;
        JSONObject MyData = new JSONObject();

        try {
            MyData.put("operationDate", date);
            MyData.put("requestDetailId", String.valueOf(o.requestDetailId)); //Add the data you'd like to send to the server.
            JSONArray operationDetails = new JSONArray();
            JSONObject meter1 = new JSONObject();
            JSONObject meter2 = new JSONObject();
            try {
                    if (t.getMetros().get(0).currentValue != 0) {
                        meter1.put("itemId", act.itemId)
                                .put("companyId", act.companyId)
                                .put("warehouseId", String.valueOf(t.id))
                        .put("meterId", String.valueOf(t.getMetros().get(0).id))
                        .put("startingMeter", String.valueOf((t.getMetros().get(0).currentValue)-qty1))
                        .put("endingMeter", String.valueOf(t.getMetros().get(0).currentValue))
                        .put("qty", String.valueOf(qty1))
                        .put("tailAircraftId", act.tailAircraftId);

                    }
                    operationDetails.put(meter1);



                    if ( t.getMetros().size() > 1) {
                        if(t.getMetros().get(1).currentValue != 0) {
                            meter2.put("ItemId", act.itemId)
                                    .put("companyId", act.companyId)
                                    .put("warehouseId", String.valueOf(t.id))
                                    .put("meterId", String.valueOf(t.getMetros().get(1).id))
                                    .put("startingMeter", String.valueOf((t.getMetros().get(1).currentValue) - qty2))
                                    .put("endingMeter", String.valueOf(t.getMetros().get(1).currentValue))
                                    .put("qty", String.valueOf(qty2))
                                    .put("tailAircraftId", act.tailAircraftId);
                        }
                    }
                    operationDetails.put(meter2);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            String a;
            a = "{selectedInventoryId:0," +
                    "flightNbr:'nsda'," +
                    "aditive:" + String.valueOf(o.fsii) + "," +
                    "note:'" + o.notes + "'," +
                    "referenceNbr:null," +
                    "endingDateTime:" + o.arrivalDate + "," +
                    "startingDateTime:" + o.departureDate + "}";



            MyData.put("propertyJson", a);
            MyData.put("operationDetails", operationDetails); //Add the data you'd like to send to the server.
            System.out.println("LEFIN: " + MyData);
            System.out.println(" ");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST, Static_variables.Apis + "addFuel", MyData,
                new Response.Listener<JSONObject>() {

                    @Override

                    public void onResponse(JSONObject response) {
                        if(act.dialog.isShowing()) {
                            act.dialog.dismiss();
                        }
                        Toast.makeText(act,"Start fueling",Toast.LENGTH_SHORT).show();
                        Static_variables.fuel(Static_variables.order.tailNbr.toString());
                        act.dd.dismiss();
                        Intent b = new Intent(act, Fuel_feed.class);
                        b.putExtra("status", "Fueling completed!");
                        act.startActivity(b);

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        act.dd.dismiss();
                        Toast.makeText(act,"Error!",Toast.LENGTH_SHORT).show();
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

    String tailAircraftId, companyId,itemName,itemId="2",imgPath;
    /*JsonArrayRequest getExtraData = new JsonArrayRequest(
            Request.Method.GET,
            Static_variables.Apis + "simpleTail/"+Static_variables.order.tailNbr,
            null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    // Do something with response
                    //mTextView.setText(response.toString());

                    // Process the JSON
                    try {
                        Static_variables.icaos=new HashSet<>();
                        Static_variables.tail_numbers=new HashSet<>();
                        // Loop through the array elements
                        for (int i = 0; i < response.length(); i++) {
                            // Get current json object

                            JSONObject res= response.getJSONObject(i);

                            //JSONObject res = response.getJSONObject(i);




                         //   JSONArray p=res.getJSONArray("result");
                            /*JSONObject order=p.getJSONObject(0);
                            tailAircraftId=order.getString("tailAircraftId");
                            companyId = order.getString("companyId");
                            itemName = order.getString("itemName");
                            itemId = order.getString("itemId");
                            imgPath = order.getString("imgPath");

                            //mTextView.append("\n\n");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }finally {
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
    };*/

    JsonArrayRequest getExtraData = new JsonArrayRequest(
            Request.Method.GET,
            Static_variables.Apis + "simpleTail/"+Static_variables.order.tailNbr,
            null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    // Do something with response
                    //mTextView.setText(response.toString());

                    // Process the JSON
                    try {
                        // Loop through the array elements
                            // Get current json object

                        JSONArray res= response;

                            //JSONObject res = response.getJSONObject(i);




                            JSONObject order=res.getJSONObject(0);
                            tailAircraftId=order.getString("tailAircraftId");
                            companyId = order.getString("companyId");

                            itemId = String.valueOf(Static_variables.selected.itemId);
                            imgPath = order.getString("imgPath");

                        System.out.println("COSAAAAAAAAAAAA:"+tailAircraftId+" "+companyId+" "+itemName+" "+itemId+" "+itemId+" "+imgPath);
                            //mTextView.append("\n\n");

                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println("COSAAAAAAAAAAAA ERROR:"+tailAircraftId+" "+companyId+" "+itemName+" "+itemId+" "+itemId+" "+imgPath);

                    }finally {
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


    @Override
    public void onResume() {
        super.onResume();
        setFilters();  // Start listening notifications from UsbService
        startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mUsbReceiver);
        unbindService(usbConnection);
    }

    // ----------------------------------------------------------------
    // ----------------------- Servicios Serial -----------------------
    // ----------------------------------------------------------------

    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!UsbService.SERVICE_CONNECTED) {
            Intent startService = new Intent(this, service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            startService(startService);
        }
        Intent bindingIntent = new Intent(this, service);
        bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void setFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(UsbService.ACTION_NO_USB);
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
        registerReceiver(mUsbReceiver, filter);
    }

    /*
     * Notifications from UsbService will be received here.
     */
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UsbService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show();
                    Static_variables.fuel("4");
                    break;
                case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                    Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_NO_USB: // NO USB CONNECTED
                    Toast.makeText(context, "No USB connected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                    Toast.makeText(context, "USB disconnected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
                    Toast.makeText(context, "USB device not supported", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            usbService = ((UsbService.UsbBinder) arg1).getService();
            usbService.setHandler(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            usbService = null;
        }
    };
}
