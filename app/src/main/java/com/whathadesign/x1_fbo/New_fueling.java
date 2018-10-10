package com.whathadesign.x1_fbo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

public class New_fueling extends AppCompatActivity {
    private Activity actividad;
    RequestQueue ExampleRequestQueue;
    AutoCompleteTextView tailnumber_quemaos, model_text, icao_text;
    RelativeLayout button_append;
    String type="Specific Qty";
    Button add;
String fuelOn;
boolean ap;
    final CheckBox [] checkboxes= new CheckBox[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_fueling);
        LinearLayout layout=(LinearLayout)findViewById(R.id.insert_point);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });
        actividad=this;
        Static_variables.set_down_info_bar(this.findViewById(android.R.id.content));
        Static_variables.setupDrawer(this,"New Fueling");
        findViewById(R.id.new_fueling_main).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });
        Spinner spinner = (Spinner) findViewById(R.id.transfer_spinner_uno);

        String[] letra = {"Gallons","Liters"};
        button_append = (RelativeLayout) findViewById(R.id.button_append);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, letra));

        checkboxes[0] = (CheckBox) findViewById(R.id.specifiq_check);
        checkboxes[1] = (CheckBox) findViewById(R.id.topoff_check);
        checkboxes[2] = (CheckBox) findViewById(R.id.will_check);
        checkboxes[3] = (CheckBox) findViewById(R.id.up_check);


        for (int i = 0; i < checkboxes.length; i++) {
            final int finalI = i;
            checkboxes[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(checkboxes[finalI].isChecked()){
                        for (int j = 0; j < checkboxes.length; j++) {
                            if(j!=finalI){
                                checkboxes[j].setChecked(false);
                            }
                        }
                        type=checkboxes[finalI].getText().toString();
                    }
                }
            });
        }
        add=(Button)findViewById(R.id.add);
        final Button [] botoncitos= new Button[2];
        botoncitos[0]=(Button) findViewById(R.id.newf_arrival);
        botoncitos[1]=(Button)findViewById(R.id.newf_departure);
        for (int i = 0; i < botoncitos.length; i++) {
                final int finalI = i;

                botoncitos[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!botoncitos[finalI].isActivated()) {
                                if(finalI==0){
                                    fuelOn="Arrival";
                                }else{
                                    fuelOn="Departure";
                                }
                            botoncitos[finalI].setActivated(true);
                            for (int j = 0; j < botoncitos.length; j++) {
                                if (j != finalI) {
                                    botoncitos[j].setActivated(false);
                                }
                                if(botoncitos[j].isActivated()){
                                    botoncitos[j].setTextColor(getResources().getColor(R.color.blanco));
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        botoncitos[j].setBackground(getDrawable(R.drawable.gray_rect));
                                        botoncitos[j].setBackgroundTintList(getResources().getColorStateList(R.color.azulletra));
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        botoncitos[j].setCompoundDrawableTintList(getResources().getColorStateList(R.color.blanco));
                                    }
                                }else{
                                    botoncitos[j].setTextColor(getResources().getColor(R.color.grisletra));
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        botoncitos[j].setBackground(getDrawable(R.drawable.white_rect));
                                        botoncitos[j].setBackgroundTintList(getResources().getColorStateList(R.color.grisclaro));
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        botoncitos[j].setCompoundDrawableTintList(getResources().getColorStateList(R.color.grisletra));
                                    }
                                }
                            }
                        }
                        System.out.println("FUELON:"+fuelOn);

                    }
                });
        }
            final Button [] fsii= new Button[2];
        fsii[0]=(Button) findViewById(R.id.fsii_plus);
        fsii[1]=(Button)findViewById(R.id.fsii_min);

        for (int i = 0; i < fsii.length; i++) {
            final int finalI = i;
            fsii[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!fsii[finalI].isActivated()) {

                        fsii[finalI].setActivated(true);
                        for (int j = 0; j < fsii.length; j++) {
                            if (j != finalI) {
                                fsii[j].setActivated(false);
                            }
                            if(fsii[j].isActivated()){
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    fsii[j].setBackgroundTintList(getResources().getColorStateList(R.color.verne));
                                }
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    fsii[j].setCompoundDrawableTintList(getResources().getColorStateList(R.color.verne));
                                }
                            }else{
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    fsii[j].setBackgroundTintList(getResources().getColorStateList(R.color.negro));
                                }
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    fsii[j].setCompoundDrawableTintList(getResources().getColorStateList(R.color.negro));
                                }
                            }
                        }
                    }
                }
            });
        }
        final String [] icaos= new String[Static_variables.models.size()];
        String [] models= new String[Static_variables.models.size()];

        for (int i = 0; i < icaos.length; i++) {
            icaos[i]=Static_variables.models.get(i).icao;
            models[i]=Static_variables.models.get(i).model;
        }
        icao_text = (AutoCompleteTextView) findViewById(R.id.new_fl_icao);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,icaos);
        icao_text.setAdapter(adapter);

        model_text = (AutoCompleteTextView) findViewById(R.id.new_fl_model);
        final ArrayAdapter<String> modeladapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, models);
        model_text.setAdapter(modeladapter);

        tailnumber_quemaos = (AutoCompleteTextView) findViewById(R.id.new_fl_tailnumber);




        TextWatcher a = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            Button add=(Button)findViewById(R.id.add);

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (tailnumber_quemaos.getText().length() > 0) {
                    String [] a= tailnumber_quemaos.getText().toString().split(" ");
                    searchTail(a[0]);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        final boolean[] button_ready = {false};
        TextWatcher b = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(tailnumber_quemaos.getText().length()>4 && icao_text.length()>3 && model_text.length()>3){

                    add.setTextColor(getResources().getColor(R.color.blanco));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        add.setBackgroundTintList(getResources().getColorStateList(R.color.azul_barras));
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        tailnumber_quemaos.setBackgroundTintList(getResources().getColorStateList(R.color.border_gris));
                        icao_text.setBackgroundTintList(getResources().getColorStateList(R.color.border_gris));
                        model_text.setBackgroundTintList(getResources().getColorStateList(R.color.border_gris));

                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        tailnumber_quemaos.setCompoundDrawableTintList(getResources().getColorStateList(R.color.border_gris));
                        icao_text.setCompoundDrawableTintList(getResources().getColorStateList(R.color.border_gris));
                        model_text.setCompoundDrawableTintList(getResources().getColorStateList(R.color.border_gris));

                    }
                    button_ready[0] =true;
                }else{
                    add.setTextColor(getResources().getColor(R.color.grismedio));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        add.setBackgroundTintList(getResources().getColorStateList(R.color.grisnew));
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        tailnumber_quemaos.setBackgroundTintList(getResources().getColorStateList(R.color.rojo));
                        icao_text.setBackgroundTintList(getResources().getColorStateList(R.color.rojo));
                        model_text.setBackgroundTintList(getResources().getColorStateList(R.color.rojo));

                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        tailnumber_quemaos.setCompoundDrawableTintList(getResources().getColorStateList(R.color.rojo));
                        icao_text.setCompoundDrawableTintList(getResources().getColorStateList(R.color.rojo));
                        model_text.setCompoundDrawableTintList(getResources().getColorStateList(R.color.rojo));

                    }
                    button_ready[0] =false;

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        tailnumber_quemaos.addTextChangedListener(a);

        tailnumber_quemaos.addTextChangedListener(b);
        icao_text.addTextChangedListener(b);
        model_text.addTextChangedListener(b);

        tailnumber_quemaos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object item= adapterView.getItemAtPosition(i);
                if(item instanceof String){
                    for (int j = 0; j < tails.size(); j++) {
                        String[] a =((String) item).split(" ");
                        if(a[0].equals(tails.get(j).tailNbr)){
                            icao_text.setText(tails.get(j).icao);
                            model_text.setText(tails.get(j).modelName);
                            ap=false;
                        }
                    }
                }

            }
    });



        icao_text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object item= adapterView.getItemAtPosition(i);
                if(item instanceof String){
                    for (int j = 0; j < Static_variables.models.size(); j++) {
                        if(item.equals(Static_variables.models.get(j).icao)){
                            model_text.setText(Static_variables.models.get(j).model);

                        }
                    }
                }

            }
        });

        model_text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object item= adapterView.getItemAtPosition(i);
                if(item instanceof String){
                    for (int j = 0; j < Static_variables.models.size(); j++) {
                        if(item.equals(Static_variables.models.get(j).model)){
                            icao_text.setText(Static_variables.models.get(j).icao);

                        }
                    }
                }

            }
        });
        ExampleRequestQueue = Volley.newRequestQueue(this);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(button_ready[0])
                enviar_aircraft(tailnumber_quemaos.getText().toString(),model_text.getText().toString(),Static_variables.selected.masterItemId);
            }
        });
    }

    public void onFueling(View v){
        EditText qty_text=(EditText)findViewById(R.id.qty_requested);
        EditText notes=(EditText)findViewById(R.id.new_notes);


        if(tailnumber_quemaos.getText().length()>3 && icao_text.length()>2 && model_text.length()>2) {
            int qty=0;
            Button fsii_btn=(Button)findViewById(R.id.fsii_plus);
            Boolean fsii=fsii_btn.isActivated();
            if (checkboxes[0].isSelected()) {
                if (qty_text.getText().length() > 0 && Integer.parseInt(qty_text.getText().toString()) > 0) {
                    qty = Integer.parseInt(qty_text.getText().toString());
                    saveFueling(Static_variables.selected, Static_variables.selected.itemId, qty, fsii, 25, notes.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Qty must be more than 0", Toast.LENGTH_SHORT).show();
                }
            }else{
                saveFueling(Static_variables.selected, Static_variables.selected.itemId, qty, fsii, 25, notes.getText().toString());
            }
        }else{
            Toast.makeText(actividad,"Please fill the info",Toast.LENGTH_SHORT).show();
        }
    }

    public void onCancel(View v){
        Static_variables.cancelDialog a= new Static_variables.cancelDialog(this,"Cancel new Fueling?",Fuel_feed.class);
        a.show();
    }
public void searchTail(final String tail_base) {
   final JsonArrayRequest getTails = new JsonArrayRequest(
            Request.Method.GET,
            Static_variables.Apis + "simpleTail/"+tail_base+"/",
            null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    // Do something with response
                    //mTextView.setText(response.toString());
                    // Process the JSON
                    try {
                        // Loop through the array elements

                        if(response.length()>0) {
                            button_append.setVisibility(View.GONE);
                            tails=new ArrayList<>();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                tailnumber_quemaos.setBackgroundTintList(getResources().getColorStateList(R.color.border_gris));
                                icao_text.setBackgroundTintList(getResources().getColorStateList(R.color.border_gris));
                                model_text.setBackgroundTintList(getResources().getColorStateList(R.color.border_gris));

                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                tailnumber_quemaos.setCompoundDrawableTintList(getResources().getColorStateList(R.color.border_gris));
                                icao_text.setCompoundDrawableTintList(getResources().getColorStateList(R.color.border_gris));
                                model_text.setCompoundDrawableTintList(getResources().getColorStateList(R.color.border_gris));

                            }
                            for (int i = 0; i < response.length(); i++) {
                                // Get current json object
                                JSONObject truck = response.getJSONObject(i);
                                int tailAircraftId = truck.getInt("tailAircraftId");
                                int itemId=999;
                                String tailNbr = truck.getString("tailNbr");
                                String icao = truck.getString("icao");
                                String modelName = truck.getString("modelName");
                                String id =truck.getString("itemId");
                                if(!id.equals("null")) {
                                  itemId =Integer.parseInt(id);
                                }
                               // String itemName = truck.getString("itemName");

                                String itemName="gas";
                                String imgPath = truck.getString("imgPath");
                                String companyName = truck.getString("companyName");
                                if(itemId==Static_variables.selected.masterItemId) {
                                    tails.add(new Tail(tailAircraftId, tailNbr, icao, modelName, itemId, itemName, imgPath, companyName));
                                }
                                System.out.println("added: "+tailAircraftId +" "+ itemId + " "+Static_variables.selected.masterItemId);

                                // Get the current student (json object) data
                                // Display the formatted json data in text view
                                //mTextView.append(firstName +" " + lastName +"\nAge : " + age);
                                //mTextView.append("\n\n");
                            }
                        }else{
                            button_append.setVisibility(View.VISIBLE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                tailnumber_quemaos.setBackgroundTintList(getResources().getColorStateList(R.color.rojo));
                                icao_text.setBackgroundTintList(getResources().getColorStateList(R.color.rojo));
                                model_text.setBackgroundTintList(getResources().getColorStateList(R.color.rojo));

                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                tailnumber_quemaos.setCompoundDrawableTintList(getResources().getColorStateList(R.color.rojo));
                                icao_text.setCompoundDrawableTintList(getResources().getColorStateList(R.color.rojo));
                                model_text.setCompoundDrawableTintList(getResources().getColorStateList(R.color.rojo));

                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    } finally {
                        String [] tails_adp=new String[tails.size()];
                        for (int i = 0; i < tails_adp.length; i++) {
                            tails_adp[i]=tails.get(i).tailNbr+" "+tails.get(i).companyName;
                        }
                        ArrayAdapter<String> tailadapter = new ArrayAdapter<String>(actividad, android.R.layout.simple_dropdown_item_1line, tails_adp);
                        tailnumber_quemaos.setAdapter(tailadapter);
                        if(ap) {
                            tailnumber_quemaos.showDropDown();
                        }
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
    ExampleRequestQueue.add(getTails);

}
ArrayList <Tail>tails=new ArrayList<>();

    private void saveFueling(final Truck t, int itemId, int qty, boolean fsii,int requestDetailId,String notes) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        /*dd = new ProgressDialog(this);
        dd.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparente)));
        dd.show();
        dd.setContentView(R.layout.dialogo_progreso);*/
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String d = df.format(Calendar.getInstance().getTime());
        String date = d;
        JSONObject MyData = new JSONObject();
        String tail[]=tailnumber_quemaos.getText().toString().split(" ");
        String companyName="";
        for (int i = 1; i < tail.length; i++) {
            companyName=companyName+" "+tail[i];
        }
        Tail tt=new Tail(0,"null","null","null",0,"null","null","null");
        for (int i = 0; i < tails.size(); i++) {
            if(tail[0].equals(tails.get(i).tailNbr)){
                tt=tails.get(i);
            }
        }

        Order a = new Order(requestDetailId,tt.tailAircraftId,1,String.valueOf(qty),companyName,t.itemName,tt.tailNbr,tt.icao,"null","null","null","null",fuelOn,4,fsii,tt.modelName,"",notes,type);
       Static_variables.order=a;
        Intent h = new Intent(getApplicationContext(), Order_view.class);
        startActivity(h);
        /*  try {
            MyData.put("operationDate", date);
            MyData.put("requestDetailId", String.valueOf(requestDetailId)); //Add the data you'd like to send to the server.
            JSONArray operationDetails = new JSONArray();
            JSONObject meter1 = new JSONObject();
            JSONObject meter2 = new JSONObject();
            try {
                    meter1.put("companyId", 1);
                    meter1.put("warehouseId", String.valueOf(t.id));
                    meter1.put("meterId", String.valueOf(t.getMetros().get(0).id));
                    meter1.put("startingMeter", String.valueOf(t.getMetros().get(0).currentValue));
                    meter1.put("endingMeter", null);
                    meter1.put("qty",qty);
                    meter1.put("tailAircraftId",tail[0]);
                    meter1.put("itemId", itemId);

                    operationDetails.put(meter1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String b;
           b = "{selectedInventoryId:0," +
                    "flightNbr:adssda"+"," +
                    "aditive:" + String.valueOf(fsii) + "," +
                    " note:" + notes + "," +
                    "referenceNbr:null," +"}";



            MyData.put("PropertyJson", b);
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
                        dd.dismiss();
                        Intent a= new Intent(getApplicationContext(),TruckLog.class);
                        a.putExtra("status","Fueling created!");
                        startActivity(a);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        dd.dismiss();
                        Toast.makeText(actividad,"Error!",Toast.LENGTH_SHORT).show();

                    }
                }) {

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("Authorization", "bearer " + Static_variables.Token);
                return headers;
            }
        };

        // Adding request to request queue
        MyRequestQueue.add(strReq);*/
    }
    ProgressDialog dd;

    private void enviar_aircraft(String tailNbr, String aircModelId, int itemId) {
        final RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        dd = new ProgressDialog(this);
        dd.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparente)));
        dd.show();
        dd.setContentView(R.layout.dialogo_progreso);
        JSONObject MyData = new JSONObject();

        try {
            MyData.put("tailNbr", tailNbr);
            int id=233;
            System.out.println("SAIZE"+Static_variables.models.size());
            for (int i = 0; i <Static_variables.models.size() ; i++) {
                System.out.println(aircModelId+" el otro: "+Static_variables.models.get(i).model);

                if(aircModelId.equals(Static_variables.models.get(i).model)){
                     id=Static_variables.models.get(i).id;
                    System.out.println("TRUEEEESADJ");
                    break;
                }else{
                    id=233;
                }
            }
            MyData.put("aircModelId", id);
            MyData.put("masterItemId", itemId);
            System.out.println("SAFD: "+MyData);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest aa = new JsonObjectRequest(Request.Method.POST, Static_variables.Apis + "saveAircraft", MyData,
                new Response.Listener<JSONObject>() {

                    @Override

                    public void onResponse(JSONObject response) {
                        dd.dismiss();
                        Toast.makeText(actividad,"Aircraft saved!",Toast.LENGTH_SHORT).show();
                        button_append.setVisibility(View.GONE);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            tailnumber_quemaos.setBackgroundTintList(getResources().getColorStateList(R.color.border_gris));
                            icao_text.setBackgroundTintList(getResources().getColorStateList(R.color.border_gris));
                            model_text.setBackgroundTintList(getResources().getColorStateList(R.color.border_gris));

                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            tailnumber_quemaos.setCompoundDrawableTintList(getResources().getColorStateList(R.color.border_gris));
                            icao_text.setCompoundDrawableTintList(getResources().getColorStateList(R.color.border_gris));
                            model_text.setCompoundDrawableTintList(getResources().getColorStateList(R.color.border_gris));

                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        dd.dismiss();
                        Toast.makeText(actividad,"Error!",Toast.LENGTH_SHORT).show();

                    }
                }) {

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("Authorization", "bearer " + Static_variables.Token);
                return headers;
            }
        };

        // Adding request to request queue
        MyRequestQueue.add(aa);
    }
}
