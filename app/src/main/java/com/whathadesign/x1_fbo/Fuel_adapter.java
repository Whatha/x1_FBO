package com.whathadesign.x1_fbo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Daniel on 02/03/2018.
 */

public class Fuel_adapter extends RecyclerView.Adapter<Fuel_adapter.PersonViewHolder>{

    Context myContext;
    Activity act;
    String TAG = "X1FBO";

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView indicator, tail, icao, dept_time, fuel_order;

        ImageView indicator_state, fuel_on_img;
        Button btn,fsii;

        RelativeLayout rl,status;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_fuel);
            tail = (TextView)itemView.findViewById(R.id.card_tail_number);
            icao = (TextView)itemView.findViewById(R.id.card_icao);
            dept_time = (TextView)itemView.findViewById(R.id.card_dept_time);
            fuel_order = (TextView)itemView.findViewById(R.id.card_fuel_order);
            fuel_on_img=(ImageView)itemView.findViewById(R.id.card_fuel_on_img);
            btn= (Button)itemView.findViewById(R.id.feed_accept);
            fsii= (Button)itemView.findViewById(R.id.card_fsii);
            status=(RelativeLayout)itemView.findViewById(R.id.status);
        }
    }

    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.feed_card, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @SuppressLint("ResourceType")
    public void onBindViewHolder(final PersonViewHolder personViewHolder, final int i) {
        personViewHolder.tail.setText(orders.get(i).tailNbr);
        personViewHolder.icao.setText(orders.get(i).icao);
        if(!orders.get(i).departureDate.equals("null")) {
            personViewHolder.dept_time.setText(Static_variables.parseDate(orders.get(i).departureDate));
            personViewHolder.dept_time.setTextColor(act.getResources().getColor(R.color.verne));
            System.out.println("fecha 1:"+Static_variables.parseDate(orders.get(i).departureDate));

        }else if(!orders.get(i).DepartureEstimateDate.equals("null")) {
            personViewHolder.dept_time.setText(Static_variables.parseDate(orders.get(i).DepartureEstimateDate));
            System.out.println("fecha 2:"+Static_variables.parseDate(orders.get(i).DepartureEstimateDate));

        }else{
            personViewHolder.dept_time.setText("-");
        }

        personViewHolder.fuel_order.setText(orders.get(i).fum_Qty_Info);



        if(orders.get(i).fuelOn!=null) {
            if (orders.get(i).fuelOn.equals("arrival") || orders.get(i).fuelOn.equals("Arrival")) {
                personViewHolder.fuel_on_img.setImageResource(R.drawable.arrival_icon);
            } else {
                personViewHolder.fuel_on_img.setImageResource(R.drawable.departure_icon);
            }
        }

            if (orders.get(i).fsii){
                personViewHolder.fsii.setBackground(act.getResources().getDrawable(R.drawable.fsii_1));
            } else {
                personViewHolder.fsii.setBackground(act.getResources().getDrawable(R.drawable.fsii_2));
            }

        if(i%2==0){
            personViewHolder.cv.setBackgroundResource(R.color.blanco);
        }else{
            personViewHolder.cv.setBackgroundResource(R.color.transparente);
        }

        switch (orders.get(i).eStatus){
            case 1:
                personViewHolder.status.setBackgroundColor(act.getResources().getColor(R.color.borderyellow));
                personViewHolder.btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.check, 0, 0, 0);
                personViewHolder.btn.setText("Accept");

                break;
            case 2:
                personViewHolder.status.setBackgroundColor(act.getResources().getColor(R.color.verne));
                break;
            case 3:
                personViewHolder.status.setBackgroundColor(act.getResources().getColor(R.color.rojo));
                break;
            case 4:
                personViewHolder.status.setBackgroundColor(act.getResources().getColor(R.color.borderblue));
            personViewHolder.btn.setCompoundDrawablesWithIntrinsicBounds(R.color.transparente, 0, 0, 0);
                personViewHolder.btn.setText("Release");

                break;

        }


        personViewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Static_variables.order=orders.get(i);
                if (orders.get(i).eStatus==1) {
                    changeStatus(orders.get(i).requestDetailId,4);
                }else if(orders.get(i).eStatus==4){
                    changeStatus(orders.get(i).requestDetailId,1);

                }

            }
        });

        personViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /*dialog = new ProgressDialog(view.getContext());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(act.getResources().getColor(R.color.transparente)));
                dialog.show();
                dialog.setContentView(R.layout.dialogo_progreso);*/
                Static_variables.order=orders.get(i);
                Intent a = new Intent(myContext, Order_view.class);
                act.startActivity(a);
            }
        });


    }
    ProgressDialog dialog;

    List<Order> orders;

    Fuel_adapter(List<Order> orders, Context myContext, Activity act){
        this.orders = orders;
        this.myContext=myContext;
        this.act=act;
    }

    public int getItemCount() {
        return orders.size();
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    private void changeStatus(final int requestDetailId, final int change){
        try {
            RequestQueue MyRequestQueue = Volley.newRequestQueue(myContext);
            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, Static_variables.Apis + "changeStatus/", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //This code is executed if the server responds, whether or not the response contains data.
                    //The String 'response' contains the server's response.
                    System.out.println("---------RESPUESTA: "+response);
                    Static_variables.order.eStatus=change;
                    Intent a = new Intent(myContext, Order_view.class);
                    act.startActivity(a);
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
                    MyData.put("eStatus", String.valueOf(change)); //Add the data you'd like to send to the server.

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
        }

    }


}