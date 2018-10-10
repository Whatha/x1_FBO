package com.whathadesign.x1_fbo;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.mindorks.placeholderview.PlaceHolderView;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class Login extends AppCompatActivity {
    private Toolbar mTopToolbar;
    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    private PlaceHolderView mGalleryView;
    private String URL_LOGIN= "https://authstg.x1fbo.com/token";
    public static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.login_main_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            return true;
            }
        });
        Fabric.with(this, new Crashlytics());
        //toolBarsita();

     /*   mDrawer = (DrawerLayout)findViewById(R.id.drawerLayout);
        mDrawerView = (PlaceHolderView)findViewById(R.id.drawerView);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mGalleryView = (PlaceHolderView)findViewById(R.id.galleryView);
        setupDrawer();*/
         if( Build.VERSION.SDK_INT >= 23 && !checkPermissionForWriteExtertalStorage()){
             try {
                 requestPermissionForWriteExtertalStorage();
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }


    }
    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean checkPermissionForWriteExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionForWriteExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public void onLogin(View v){
       login();
    }

    private void login(){
        EditText nombre= (EditText) findViewById(R.id.nme);
        EditText pass= (EditText) findViewById(R.id.pass);
        String nm= nombre.getText().toString();
        String ps= pass.getText().toString();
        if(nm.length()<1 || ps.length()<1){
            Toast.makeText(getApplicationContext(), "Whoops! Please fill in all required fields", Toast.LENGTH_SHORT).show();
        }else{
            //solicitud(nm,ps);
            final_countdown(nm,ps);
        }

        //solicitud(nm,ps);
      Intent a=new Intent(getApplicationContext(),truck_feed.class);
      startActivity(a);

       // postNewComment(nm,ps);
    }

    private void solicitud(final String nombre, final String pass){
        try {
            RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

            StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,
                    URL_LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("---------RESPUESTA: "+response);

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("volley", "Error: " + error.getMessage());
                    error.printStackTrace();
                    Toast.makeText(getApplicationContext(),"bad request",Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    String id="414e1927a3884f68abc79f7283837fd1";
                    String type="password";
                    params.put("grant_type", type.trim()); //Add the data you'd like to send to the server.
                    params.put("username", "tauser"); //Add the data you'd like to send to the server.
                    params.put("password", "Ta123!@#"); //Add the data you'd like to send to the server.
                    params.put("client_id", id.trim()); //Add the data you'd like to send to the server.

                    return params;
                }

                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> headers=new HashMap<String,String>();
                    headers.put("Accept","application/json");
                    headers.put("Content-Type","application/x-www-form-urlencoded");
                    return headers;
                }

            };
           MyRequestQueue.add(jsonObjRequest);

    }catch (Exception e){
            e.printStackTrace();
        }
    }

    public  void postNewComment(final String nombre, final String pass){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST,URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),"SUCCESS",Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("volley", "Error: " + error.getMessage());
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),"bad request",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                String id="414e1927a3884f68abc79f7283837fd1";
                String type="password";
                params.put("grant_type", type.trim()); //Add the data you'd like to send to the server.
                params.put("username", "tauser"); //Add the data you'd like to send to the server.
                params.put("password", "Ta123!@#"); //Add the data you'd like to send to the server.
                params.put("client_id", id.trim()); //Add the data you'd like to send to the server.

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }
ProgressDialog dialog;
    public void final_countdown(final String nombre, final String pass){
        dialog = new ProgressDialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparente)));
        dialog.show();
        dialog.setContentView(R.layout.dialogo_progreso);
        Thread t= new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "grant_type=password&username="+nombre+"&password="+pass+"&client_id=414e1927a3884f68abc79f7283837fd1");
                okhttp3.Request r = new okhttp3.Request.Builder()
                        .url("https://authsbx.x1fbo.com/token")
                        .post(body)
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("postman-token", "1b53da4c-70b2-5ed2-77ea-bf9155bbfd5e")
                        .build();

                try {
                    okhttp3.Response response = client.newCall(r).execute();
                    System.out.println("Respuesta: "+response);
                    String MyResult = response.body().string();
                    System.out.println("postJSONRequest response.body : "+MyResult);
                        try {

                        JSONObject obj = new JSONObject(MyResult);
                        String a= obj.getString("access_token");
                        System.out.println("EL TOKEN: "+ a);
                        Static_variables.setToken(a);
                        Intent b=new Intent(getApplicationContext(),truck_feed.class);
                        startActivity(b);
                        dialog.dismiss();
                    } catch (Throwable t) {
                        System.out.println("No funciono");
                        Handler handler = new Handler(Looper.getMainLooper());

                        handler.post(new Runnable() {

                            @Override
                            public void run() {
prepare();                            }
                        });
                    }
                } catch (IOException e) {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Error!",Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }
            }
        });

        t.start();


    }

    public void prepare(){
        Toast.makeText(getApplicationContext(),"The user name or password is incorrect",Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }


 /*   public void toolBarsita(){
        mTopToolbar = (Toolbar) findViewById(R.id.barra);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setActionBar(mTopToolbar);
            TextView tx= (TextView) findViewById(R.id.bar_textito);
            tx.setText("Buenos días");
            mTopToolbar.setNavigationIcon(R.drawable.ic_logo);

            mTopToolbar.setNavigationOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(Login.this, "Back Arrow Toolbar Image Icon Clicked", Toast.LENGTH_LONG).show();
                        }
                    }

            );
        }
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

