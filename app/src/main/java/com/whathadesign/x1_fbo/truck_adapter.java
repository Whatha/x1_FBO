package com.whathadesign.x1_fbo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Daniel on 23/02/2018.
 */
public class truck_adapter extends RecyclerView.Adapter<truck_adapter.PersonViewHolder>{

    Context myContext;
    Activity act;
    String TAG = "X1FBO";

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView info,description,capacity,balance;

        ViewStub include;
        ProgressBar progress;
        ImageButton popup;
        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cartica_truck);
            info = (TextView)itemView.findViewById(R.id.truck_info);
            description = (TextView)itemView.findViewById(R.id.truck_description);
            capacity = (TextView)itemView.findViewById(R.id.truck_capacity);
            balance = (TextView)itemView.findViewById(R.id.truck_balance);
            progress= (ProgressBar)itemView.findViewById(R.id.truck_progress);
            include=(ViewStub) itemView.findViewById(R.id.truck_indicator);
            popup=(ImageButton)itemView.findViewById(R.id.truck_button);
        }
    }

    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.truck_card, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    public void onBindViewHolder(final PersonViewHolder personViewHolder, final int i) {
        personViewHolder.info.setText(String.valueOf(trucks.get(i).lastOperationDate));
        personViewHolder.description.setText(trucks.get(i).name);
        personViewHolder.capacity.setText(String.valueOf(trucks.get(i).capacity));
        personViewHolder.balance.setText(String.valueOf(trucks.get(i).balance));
        personViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Static_variables.first_truck=true;
                Intent a= new Intent(myContext,Fuel_feed.class);
                Static_variables.setSelected(trucks.get(i));
                act.startActivity(a);
            }
        });
        int max, current;
        max= trucks.get(i).capacity;
        current= trucks.get(i).balance;
        personViewHolder.progress.setMax(max);
        personViewHolder.progress.setProgress(current);
       /* if(current<2000){
        //    personViewHolder.capacity_state.setImageResource(R.drawable.alert);
        }else{*/
            //personViewHolder.capacity_state.setImageResource(R.color.transparente);
     //   }

        ViewStub stub = personViewHolder.include;

        if(stub.getParent()!=null)
            switch (trucks.get(i).itemName) {
                case "Avgas":
                    stub.setLayoutResource(R.layout.avgas);
                    stub.inflate();
                    break;
                case "Jet A":
                    stub.setLayoutResource(R.layout.jeta);
                    stub.inflate();
                    personViewHolder.progress.setProgressDrawable(act.getResources().getDrawable(R.drawable.jeta_progress));
                    break;
                case "Diesel":
                    stub.setLayoutResource(R.layout.diesel);
                    stub.inflate();
                    break;
                case "Gasoline":
                    stub.setLayoutResource(R.layout.gasoline);
                    stub.inflate();
                    break;

            }


        if(trucks.get(i).balance<0){
            personViewHolder.balance.setTextColor(act.getResources().getColorStateList(R.color.rojo));
        }
        final int[] location = new int[2];

        // Get the x, y location and store it in the location[] array
        // location[0] = x, location[1] = y.

        //Initialize the Point with x, and y positions
        final Point[] p = new Point[1];




        personViewHolder.popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                personViewHolder.popup.getLocationOnScreen(location);
                p[0] = new Point();
                p[0].x = location[0];
                p[0].y = location[1];
                if(p[0] !=null) {
                    showPopup(act, p[0],trucks.get(i).itemName);
                }


            }
        });

    }


    List<Truck> trucks;

    truck_adapter(List<Truck> trucks, Context myContext, Activity act){
        this.trucks = trucks;
        this.myContext=myContext;
        this.act=act;
    }

    public int getItemCount() {
        return trucks.size();
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


        //The "x" and "y" position of the "Show Button" on screen.




        // The method that displays the popup.
        private void showPopup(final Activity context, Point p, String t) {
            // Inflate the popup_layout.xml
            LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = layoutInflater.inflate(R.layout.truck_popup, viewGroup);
            TextView text = layout.findViewById(R.id.charge_text);
            text.setText(t);
            // Creating the PopupWindow
            final PopupWindow popup = new PopupWindow(context);
            popup.setContentView(layout);
            popup.setFocusable(true);

            // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
            int OFFSET_X = -50;
            int OFFSET_Y = -10;

            // Clear the default translucent background

            // Displaying the popup at the specified location, + offsets.
            popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);
            RelativeLayout fuel, close;

            fuel=(RelativeLayout)layout.findViewById(R.id.fuel_btn_popup);
            close=(RelativeLayout)layout.findViewById(R.id.close_popup);

            fuel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent a= new Intent(context,Fuel_feed.class);
                    context.startActivity(a);
                }
            });

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popup.dismiss();
                }
            });
            // Getting a reference to Close button, and close the popup when clicked.
            /*Button close = (Button) layout.findViewById(R.id.close);
            close.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    popup.dismiss();
                }
            });*/

    }

}