package com.whathadesign.x1_fbo;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.mindorks.placeholderview.PlaceHolderView;

public class Progress extends AppCompatActivity {
    private Activity actividad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_progress);

        actividad=this;

        Static_variables.set_down_info_bar(this.findViewById(android.R.id.content));
        Static_variables.setupDrawer(this,"In Progress");
    }

}
