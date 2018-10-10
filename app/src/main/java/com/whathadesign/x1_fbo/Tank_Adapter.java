package com.whathadesign.x1_fbo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Daniel on 21/04/2018.
 */

public class Tank_Adapter extends RecyclerView.Adapter<Tank_Adapter.PersonViewHolder>{

    Context myContext;
    Activity act;
    String TAG = "X1FBO";

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView farm,tank,capacity,balance;
        ProgressBar progress;
        ImageButton popup;
        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cartica_tank);
            farm = (TextView)itemView.findViewById(R.id.farm_name);
            tank = (TextView)itemView.findViewById(R.id.tank_name);
            capacity = (TextView)itemView.findViewById(R.id.tank_capacity);
            balance = (TextView)itemView.findViewById(R.id.tank_balance);
            progress= (ProgressBar)itemView.findViewById(R.id.tank_progress);
        }
    }

    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.top_off_card, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    public void onBindViewHolder(final PersonViewHolder personViewHolder, final int i) {
        personViewHolder.farm.setText(Tanks.get(i).farm);
        personViewHolder.tank.setText(Tanks.get(i).tank);
        personViewHolder.capacity.setText(String.valueOf(Tanks.get(i).capacity));
        personViewHolder.balance.setText(String.valueOf(Tanks.get(i).balance));
        personViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent a= new Intent(myContext,Fuel_feed.class);
                act.startActivity(a);*/
                Static_variables.farm_selected=Tanks.get(i).t;
                Intent a= new Intent(myContext,Top_off.class);
                a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                a.putExtra("nombre","Top off");
                myContext.startActivity(a);
            }
        });
        int max, current;
        max=Tanks.get(i).capacity;
        current=Tanks.get(i).balance;
        personViewHolder.progress.setMax(max);
        personViewHolder.progress.setProgress(current);
        if(Tanks.get(i).tank.equals("Jet A")){
            personViewHolder.progress.setProgressDrawable(act.getResources().getDrawable(R.drawable.jeta_progress));
        }


    }


    List<Top_off_feed.Tank> Tanks;

    Tank_Adapter(List<Top_off_feed.Tank> Tanks, Context myContext, Activity act){
        this.Tanks = Tanks;
        this.myContext=myContext;
        this.act=act;
    }

    public int getItemCount() {
        return Tanks.size();
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    //The "x" and "y" position of the "Show Button" on screen.


}