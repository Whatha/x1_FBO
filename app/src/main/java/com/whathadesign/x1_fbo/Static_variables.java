package com.whathadesign.x1_fbo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
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
import com.mindorks.placeholderview.PlaceHolderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by Daniel on 02/05/2018.
 */

public class Static_variables {
   // public static String Apis = "https://apistg.x1fbo.com/api/truckApp/";
    public static String Apis="https://apistg.x1fbo.com/api/truckApp/";
//Token viejo
    //public static String Token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1bmlxdWVfbmFtZSI6InRhdXNlciIsInVzciI6IjMyYjViOWMwLTRlMGQtNGMwMC05MTI2LTNjZmEyM2YwNzUyYyIsImZibyI6IjM0NTMiLCJjbyI6IjE5Iiwia2V5IjoiQ2xpZW50NCIsInBlciI6Ilt7XCI0XCI6N30se1wiNVwiOjd9LHtcIjZcIjozMX0se1wiN1wiOjd9LHtcIjhcIjo3fSx7XCI5XCI6MzF9LHtcIjExXCI6MjU1fSx7XCIxNlwiOjd9LHtcIjE3XCI6N30se1wiMjBcIjo3fSx7XCIyMlwiOjd9LHtcIjMwXCI6N30se1wiMzNcIjo3fSx7XCIzNlwiOjd9LHtcIjM3XCI6N30se1wiMzhcIjoxNX0se1wiNDBcIjo3fSx7XCI0MVwiOjd9LHtcIjQzXCI6N30se1wiNDRcIjo3fSx7XCI0NlwiOjd9LHtcIjQ4XCI6N30se1wiNTVcIjo3fSx7XCI1N1wiOjd9LHtcIjU4XCI6N30se1wiNTlcIjo3fSx7XCI2MlwiOjEyN30se1wiNjZcIjo3fSx7XCI2N1wiOjd9LHtcIjY4XCI6N30se1wiNjlcIjo3fSx7XCI3MFwiOjd9LHtcIjcyXCI6N30se1wiNzNcIjo3fSx7XCI3NFwiOjd9LHtcIjc1XCI6N30se1wiNzZcIjo3fSx7XCI3N1wiOjd9LHtcIjc4XCI6N30se1wiNzlcIjo3fSx7XCI4MFwiOjd9LHtcIjgxXCI6MTV9XSIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6NjI1MDUiLCJhdWQiOiI0MTRlMTkyN2EzODg0ZjY4YWJjNzlmNzI4MzgzN2ZkMSIsImV4cCI6MTUyNTIwOTEwOSwibmJmIjoxNTI1MTIyNzA5fQ.Y0Ftt_ok_0Q-40jrhYFT0Fhh6e10P-VYpjdAmiwY3iw";
    public static String Token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1bmlxdWVfbmFtZSI6InRhdXNlciIsInVzciI6ImRmZDc0NGE5LTI1OWUtNDM2Ny1iZTA0LTc5NzgzMGY1YjRlMSIsImZibyI6IjM0NTQiLCJjbyI6IjE5Iiwia2V5IjoiQ2xpZW50NCIsInBlciI6Ilt7XCI0XCI6N30se1wiNVwiOjd9LHtcIjZcIjozMX0se1wiN1wiOjd9LHtcIjhcIjo3fSx7XCI5XCI6MzF9LHtcIjExXCI6MjU1fSx7XCIxNlwiOjd9LHtcIjE3XCI6N30se1wiMjBcIjo3fSx7XCIyMlwiOjd9LHtcIjMwXCI6N30se1wiMzNcIjo3fSx7XCIzNlwiOjd9LHtcIjM3XCI6N30se1wiMzhcIjoxNX0se1wiNDBcIjo3fSx7XCI0MVwiOjd9LHtcIjQzXCI6N30se1wiNDRcIjo3fSx7XCI0NlwiOjd9LHtcIjQ4XCI6N30se1wiNTVcIjo3fSx7XCI1N1wiOjd9LHtcIjU4XCI6N30se1wiNTlcIjo3fSx7XCI2MlwiOjEyN30se1wiNjNcIjo3fSx7XCI2NFwiOjd9LHtcIjY2XCI6N30se1wiNjdcIjo3fSx7XCI2OFwiOjd9LHtcIjY5XCI6N30se1wiNzBcIjo3fSx7XCI3MlwiOjd9LHtcIjczXCI6N30se1wiNzRcIjo3fSx7XCI3NVwiOjd9LHtcIjc2XCI6N30se1wiNzdcIjo3fSx7XCI3OFwiOjd9LHtcIjc5XCI6N30se1wiODBcIjo3fSx7XCI4MVwiOjE1fV0iLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjYyNTA1IiwiYXVkIjoiNDE0ZTE5MjdhMzg4NGY2OGFiYzc5ZjcyODM4MzdmZDEiLCJleHAiOjE1Mjk1NTIxODMsIm5iZiI6MTUyOTQ2NTc4M30.9egzw8nt8ZWV9HbbRJPpxKdRzTuO7LzUZcZhEI1b8E4";
    public static ArrayList <model> models=new ArrayList<>();

    public static HashSet<String> tail_numbers;
    public static HashSet<String> icaos;
    public static ArrayList<Truck> hydrants=new ArrayList<>();
    public static ArrayList<Truck> farm=new ArrayList<>();
    public static Truck selected;
    public static Truck farm_selected;
    public static User user;

    public static UsbService usbService;
    public static Order order;

    public static String getApis() {
        return Apis;
    }

    public static void setApis(String apis) {
        Apis = apis;
    }

    public static String getToken() {
        return Token;
    }

    public static void setToken(String token) {
        Token = token;
    }

    public static Truck getSelected() {
        return selected;
    }

    public static void setSelected(Truck selected) {
        Static_variables.selected = selected;
    }

    public static void set_down_info_bar(View v){
        if(user!=null){
            TextView userName=(TextView) v.findViewById(R.id.feed_trucker_name);
            TextView fboName=(TextView) v.findViewById(R.id.fbo_name);
            TextView singin=(TextView) v.findViewById(R.id.singin_text);

            userName.setText(Static_variables.user.firstName+" "+Static_variables.user.lastName);
            singin.setText(Static_variables.user.fbo);
            fboName.setText(Static_variables.user.email);
        }else{
            user=new User(1,"Pedro","Pastor","holi@gmail.com","FBO3");

            TextView userName=(TextView) v.findViewById(R.id.feed_trucker_name);
            TextView fboName=(TextView) v.findViewById(R.id.fbo_name);
            TextView singin=(TextView) v.findViewById(R.id.singin_text);

            userName.setText(Static_variables.user.firstName+" "+Static_variables.user.lastName);
            singin.setText(" ");
            fboName.setText(Static_variables.user.email);
        }
    }
    public static void updateTruck(Activity act){

        TextView truck_name= (TextView) act.findViewById(R.id.truck_description_feed);
        TextView capacity= (TextView) act.findViewById(R.id.capacity_feed);
        TextView meter_one= (TextView) act.findViewById(R.id.meterone_feed);
        TextView meter_two= (TextView) act.findViewById(R.id.metertwo_feed);
        TextView hider= (TextView) act.findViewById(R.id.textView6);

        TextView balance= (TextView) act.findViewById(R.id.balance_text);
        TextView progress_capacity= (TextView) act.findViewById(R.id.progress_capcity);
        RelativeLayout contenedor=(RelativeLayout)act.findViewById(R.id.contenedor);
       // ViewStub stub = (ViewStub)  act.findViewById(R.id.stubviewsito);
        View include= (View) act.findViewById(R.id.stubv);
        ProgressBar bar= (ProgressBar) act.findViewById(R.id.balance_progress_bar);
        Truck camion= Static_variables.selected;
        truck_name.setText(camion.name);
        capacity.setText(String.valueOf(camion.capacity));
        meter_one.setText(String.valueOf(camion.metros.get(0).currentValue));
        if(camion.metros.size()>1) {
            meter_two.setText(String.valueOf(camion.metros.get(1).currentValue));
        }
        balance.setText(String.valueOf(camion.balance));
        progress_capacity.setText(String.valueOf(camion.capacity));
        if(camion.balance<0){
            balance.setTextColor(act.getResources().getColorStateList(R.color.rojo));
        }
        if(!camion.itemName.equals("Avgas")){
            bar.setProgressDrawable(act.getResources().getDrawable(R.drawable.jeta_progress));
        }
        bar.setMax(camion.capacity);
        bar.setProgress(camion.balance);

        final LayoutInflater inflater = (LayoutInflater) act.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup parent = (ViewGroup) act.findViewById(R.id.contenedor);

          switch (camion.itemName){
            case "Avgas":
                inflater.inflate(R.layout.avgas_min, parent);
                break;
            case "Jet A":
                inflater.inflate(R.layout.jeta_min, parent);
                bar.setProgressDrawable(act.getResources().getDrawable(R.drawable.jeta_progress));
                break;
            case "Diesel":
                inflater.inflate(R.layout.diesel_min, parent);
                break;
            case "Gasoline":
                inflater.inflate(R.layout.gasoline_min, parent);
                break;
        }
        if(selected.getMetros().size()==1){
              meter_two.setVisibility(View.INVISIBLE);
              hider.setVisibility(View.INVISIBLE);
        }
    }

    public static boolean first_truck=true;

    public static void setupDrawer(Activity act, String title) {
        ask4Truck(act,selected.id);
        PlaceHolderView mDrawerView;
        DrawerLayout mDrawer;
        Toolbar mToolbar;

        mDrawer = (DrawerLayout) act.findViewById(R.id.drawerLayout);
        mDrawerView = (PlaceHolderView) act.findViewById(R.id.drawerView);
        mToolbar = (Toolbar) act.findViewById(R.id.toolbar);

        act.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mDrawerView
                .addView(new DrawerHeader(user.firstName+" "+user.lastName))
                .addView(new DrawerMenuItem(act,act.getApplicationContext(), DrawerMenuItem.PENDING_FUELING))
                .addView(new DrawerMenuItem(act,act.getApplicationContext(), DrawerMenuItem.TRUCK_LOG))
                .addView(new DrawerMenuItem(act,act.getApplicationContext(), DrawerMenuItem.NEW_FUELING))
                .addView(new DrawerMenuItem(act,act.getApplicationContext(), DrawerMenuItem.TOP_OFF))
                .addView(new DrawerMenuItem(act,act.getApplicationContext(), DrawerMenuItem.RECIRC))
              //  .addView(new DrawerMenuItem(act,act.getApplicationContext(), DrawerMenuItem.DEFUEL))
                .addView(new DrawerMenuItem(act,act.getApplicationContext(), DrawerMenuItem.TRANSFER))
                .addView(new DrawerMenuItem(act,act.getApplicationContext(), DrawerMenuItem.LOGOUT))
                .addView(new DrawerMenuItem(act,act.getApplicationContext(), DrawerMenuItem.LOGINOUT));

            mDrawerView.addView(1,new DrawerMenuItem(act,act.getApplicationContext(), DrawerMenuItem.CHANGE_TRUCK));
        mDrawerView.setBackgroundColor(act.getResources().getColor(R.color.colorPrimary));
        mDrawerView.bringToFront();
        mDrawerView.requestLayout();
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(act, mDrawer, mToolbar, R.string.open, R.string.closest) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };


        TextView toolbarText= (TextView) act.findViewById(R.id.bar_textito);
        toolbarText.setText(title);
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        Static_variables.updateTruck(act);

    }
    
    public static String [] Stringparser(HashSet<String> parse){
        String [] array= new String[parse.size()];
        int counter=0;
        for (String s: parse) {
            array[counter]=s;
            counter++;
        }

        return array;
    }

    public static void icaos_quemaos(){
      icaos  = new HashSet<>();
        icaos.add("holi1");
        icaos.add("holi2");
        icaos.add("holi3");
        icaos.add("holi4");
        icaos.add("holi5");

    }
    public static void  tailnumbers_quemaos(){

        tail_numbers  = new HashSet<>();
        tail_numbers.add("tail 1");
        tail_numbers.add("tail 2");
        tail_numbers.add("tail 3");
        tail_numbers.add("tail 3");
        tail_numbers.add("tail 3");

        tail_numbers.add("tail 4");
        tail_numbers.add("tail 5");
        }


        public static String parseDate(String s){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
            Date date = new Date();
            TimeZone tz = TimeZone.getDefault();

            String dateTime = " ";
            try {
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                date= dateFormat.parse(s);
                dateFormat.setTimeZone(tz);
                dateFormat.format(date);
                dateTime= DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(date);

            } catch (ParseException e) {
                SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                try {
                    df2.setTimeZone(TimeZone.getTimeZone("UTC"));


                    date= df2.parse(s);
                    df2.setTimeZone(tz);
                    df2.format(date);

                    dateTime= df2.getDateTimeInstance(df2.SHORT, df2.SHORT).format(date);

                } catch (ParseException e1) {
                    e1.printStackTrace();
                }

            }
                return dateTime;
        }

        public static String calculoAuto(String uno, String dos){
            String text = "";

            try {
               int a = Integer.parseInt(uno);
               int b = Integer.parseInt(dos);
               int c = b - a;
                if(c>0) {
                    text = String.valueOf(c);
                }else{
                    text="0";
                }
           }catch (NumberFormatException e){
               text="0";
           }
            return text;

        }

        public static String calculoAutoDos(String uno, String dos){
            String text = "";

            try {
               int a = Integer.parseInt(uno);
               int b = Integer.parseInt(dos);
               int c = b + a;
                    text = String.valueOf(c);
           }catch (NumberFormatException e){
               text="0";
           }
            return text;

        }

        public static String todayDate(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String d = df.format(Calendar.getInstance().getTime());
        String date = d;
        return date;
    }

        public static String getDate(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String d = df.format(Calendar.getInstance().getTime());
        String a = d;
        return a;
    }
    public static String getOtherDay(int day){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String d = df.format(yesterday(day));
        String a = d;
        return a;
    }
    public static Date yesterday(int day) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -(day));
        return cal.getTime();
    }

public static void ask4Truck(final Activity act, int id) {
   RequestQueue ExampleRequestQueue= Volley.newRequestQueue(act);
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
                            int masterItemId = truck.getInt("masterItemId");

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
                            if(eWarehouseType==2 || eWarehouseType==3) {
                                System.out.println("AÑADIDOOOOOO: "+id+" "+masterItemId+"Verified: "+verified);
                                Truck t =new Truck(id, balance, canDispense, canDefuel,verified, code, capacity, eWarehouseType, itemId,masterItemId, itemName, name, date, meters);
                                if(t.id==selected.id){
                                    Static_variables.selected=t;
                                }
                            }
                            // Get the current student (json object) data
                            // Display the formatted json data in text view
                            //mTextView.append(firstName +" " + lastName +"\nAge : " + age);
                            //mTextView.append("\n\n");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }finally {
                        updateTruck(act);

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
ExampleRequestQueue.add(jsonArrayRequest);
}

    public static void fuel(String serve){
        if (usbService != null) { // if UsbService was correctly binded, Send data
            byte[] request = null;
            if (serve.equals("1")) {
                request = HexCodeGenerator.getVolume();
            } else if (serve.equals("2")) {
                request = HexCodeGenerator.beginDelivery();
            } else if (serve.equals("3")) {
                request = HexCodeGenerator.getTailNumber();
            } else if (serve.equals("4")) {
                request = HexCodeGenerator.setTruckID(String.valueOf(selected.id));
            } else if (serve.equals("5")) {
                request = HexCodeGenerator.getTruckID();
            } else if (serve.equals("6")) {
                request = HexCodeGenerator.getSysMode();
            } else if (serve.equals("7")) {
                request = HexCodeGenerator.getSystemGross();
            } else {
                String tailNumber = serve;
                request = HexCodeGenerator.setTailNumber(tailNumber);
            }
            // Con este metodo se escribe en el puerto serial
            usbService.write(request);
        }
    }

    public static class cancelDialog extends Dialog implements
            android.view.View.OnClickListener {
        public Activity activity;
        public Button yes,no;
        public Class clase;
        String texto;

        public cancelDialog(Activity activity, String texto, Class clase) {
            super(activity);
            this.activity = activity;
            this.texto = texto;
            this.clase=clase;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);

            setContentView(R.layout.cancel_dialog);
            yes = (Button) findViewById(R.id.cancel_dialog_yes);
            no = (Button) findViewById(R.id.cancel_dialog_no);

            yes.setOnClickListener(this);
            no.setOnClickListener(this);

            TextView text = (TextView) findViewById(R.id.cancel_dialog_text);
            text.setText(texto);
            //order_quemada();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cancel_dialog_yes:
                    Intent a= new Intent(activity.getApplicationContext(),clase);
                    activity.startActivity(a);
                    break;
                case R.id.cancel_dialog_no:
                    dismiss();
                    break;
                default:
                    break;
            }
        }


    }
}