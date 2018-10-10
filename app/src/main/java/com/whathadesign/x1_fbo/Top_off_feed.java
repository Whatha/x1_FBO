package com.whathadesign.x1_fbo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.mindorks.placeholderview.PlaceHolderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Top_off_feed extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context myContext;

    private Activity actividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_off_feed);
        myContext=this.getApplicationContext();
        actividad = this;
        mRecyclerView = (RecyclerView) findViewById(R.id.top_off_recycler);


        Static_variables.set_down_info_bar(this.findViewById(android.R.id.content));
        Static_variables.setupDrawer(this,"New Top Off");


        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mLayoutManager = new GridLayoutManager(myContext, 3);
        mRecyclerView.setLayoutManager(mLayoutManager);
        initializeData();
    }

    private List<Tank> tanks;


    private void initializeData() {
        tanks = new ArrayList<Tank>();


        for (int i = 0; i < Static_variables.farm.size(); i++) {
            Truck a=Static_variables.farm.get(i);
            if(Static_variables.selected.itemName.equals(a.itemName)) {
                tanks.add(new Tank(a.name, a.itemName, a.balance, a.capacity, a));
            }
        }
        mAdapter = new Tank_Adapter(tanks, myContext, this);
        mRecyclerView.setAdapter(mAdapter);

    }


    public static class Tank {
        String farm, tank;
        int capacity, balance;
        Truck t;

        Tank(String farm, String tank, int balance, int capacity, Truck t) {
            this.farm = farm;
            this.tank = tank;
            this.capacity = capacity;
            this.balance = balance;
            this.t=t;
        }
    }

}
