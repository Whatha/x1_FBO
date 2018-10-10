package com.whathadesign.x1_fbo;

import android.widget.ImageView;
import android.widget.TextView;

import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

/**
 * Created by Daniel on 12/04/2018.
 */
@Layout(R.layout.drawer_header)
public class DrawerHeader {
    String nombre;

    public DrawerHeader(String nombre) {
        this.nombre = nombre;
    }

    @View(R.id.profileImageView)
    private ImageView profileImage;

    @View(R.id.nameTxt)
    private TextView nameTxt;


    @Resolve
    private void onResolved() {
        nameTxt.setText(Static_variables.user.firstName+" "+Static_variables.user.lastName);
    }
}