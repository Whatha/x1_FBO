package com.whathadesign.x1_fbo;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by Daniel on 19/04/2018.
 */

public class Log_adapter extends RecyclerView.Adapter<Log_adapter.PersonViewHolder> {

    Context myContext;
    Activity act;
    String TAG = "X1FBO";

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView meter_one_end, meter_two_end, meter_one_total, meter_two_total, fuel_qty_in, fuel_qty_out, truck_balance;
        TextView tail, icao, fueler, opt_time;
        ImageView type;

        RelativeLayout rl;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.log_card);
            type = (ImageView) itemView.findViewById(R.id.log_type);
            tail = (TextView) itemView.findViewById(R.id.log_tail);
            icao = (TextView) itemView.findViewById(R.id.log_icao);
            meter_one_end = (TextView) itemView.findViewById(R.id.log_m1_end);
            meter_one_total = (TextView) itemView.findViewById(R.id.log_m1_total);
            meter_two_end = (TextView) itemView.findViewById(R.id.log_m2_end);
            meter_two_total = (TextView) itemView.findViewById(R.id.log_m2_total);
            fuel_qty_in = (TextView) itemView.findViewById(R.id.log_qty_in);
            fuel_qty_out = (TextView) itemView.findViewById(R.id.log_qty_out);
            fueler = (TextView) itemView.findViewById(R.id.log_fueler);
            opt_time = (TextView) itemView.findViewById(R.id.log_opt_time);
            truck_balance = (TextView) itemView.findViewById(R.id.log_total);

        }
    }

    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.log_card, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    public void onBindViewHolder(final PersonViewHolder personViewHolder, final int i) {
        if (!logs.get(i).tailNbr.equals("null")) {
            personViewHolder.tail.setText(logs.get(i).tailNbr);
        }
        if (!logs.get(i).icao.equals("null")) {
            personViewHolder.icao.setText(logs.get(i).icao);
        }

        if (!logs.get(i).details.get(0).startingMeter.equals("null")) {
            personViewHolder.meter_one_end.setText(parser(logs.get(i).details.get(0).startingMeter));
        } else {
            personViewHolder.meter_one_end.setText("-");
        }
        if (!logs.get(i).details.get(0).endingMeter.equals("null")) {
            personViewHolder.meter_one_total.setText(parser(logs.get(i).details.get(0).endingMeter));
        } else {
            personViewHolder.meter_one_total.setText("-");
        }
        if (!logs.get(i).details.get(0).endingMeter.equals("null") && !logs.get(i).details.get(0).startingMeter.equals("null")) {
            personViewHolder.fuel_qty_in.setText(String.valueOf((int) (Double.parseDouble(logs.get(i).details.get(0).endingMeter) - Double.parseDouble(logs.get(i).details.get(0).startingMeter))));
        }
        if (logs.get(i).details.size() > 1) {
            if (!logs.get(i).details.get(1).startingMeter.equals("null")) {
                personViewHolder.meter_two_end.setText(parser(logs.get(i).details.get(1).startingMeter));
            } else {
                personViewHolder.meter_two_end.setText("-");
            }
            if (!logs.get(i).details.get(1).endingMeter.equals("null")) {
                personViewHolder.meter_two_total.setText(parser(logs.get(i).details.get(1).endingMeter));
            } else {
                personViewHolder.meter_two_total.setText("-");
            }
            if (!logs.get(i).details.get(1).endingMeter.equals("null") && !logs.get(i).details.get(1).startingMeter.equals("null")) {
                personViewHolder.fuel_qty_out.setText(String.valueOf((int) (Double.parseDouble(logs.get(i).details.get(1).endingMeter) - Double.parseDouble(logs.get(i).details.get(1).startingMeter))));
            }
            personViewHolder.truck_balance.setText(String.valueOf(logs.get(i).details.get(0).qty + logs.get(i).details.get(1).qty));
        } else {
            personViewHolder.truck_balance.setText(String.valueOf(logs.get(i).details.get(0).qty));

        }

        if(!logs.get(i).details.get(0).fueler.equals("null")) {
            personViewHolder.fueler.setText(logs.get(i).details.get(0).fueler);
        }else{
            personViewHolder.fueler.setText(Static_variables.user.firstName+" "+Static_variables.user.lastName);

        }
        personViewHolder.opt_time.setText(String.valueOf(Static_variables.parseDate(logs.get(i).operationDate)));

        System.out.println(i + " FECHAAA: " + logs.get(i).operationDate);


        switch (logs.get(i).operationDescription) {
            case "Fuel Movement":
                personViewHolder.type.setImageResource(R.drawable.icon_top_off);
                break;
            case "Defuel":
                personViewHolder.type.setImageResource(R.drawable.icon_defuel);
                break;
            case "Recirculation":
                personViewHolder.type.setImageResource(R.drawable.icon_recirc);
                break;
            case "Transfer":
                personViewHolder.type.setImageResource(R.drawable.icon_transfer);
                break;
            case "Pending":
                personViewHolder.type.setImageResource(R.drawable.pending_fuelings_icon);
                break;

            case "Verified Truck":
                personViewHolder.type.setImageResource(R.drawable.truck);
                break;
            case "Fueling":
                personViewHolder.type.setImageResource(R.drawable.fuelpump);
                personViewHolder.cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CustomDialog a;
                        if(logs.get(i).details.size()>1) {
                             a = new CustomDialog(act, String.valueOf(logs.get(i).details.get(0).qty), logs.get(i).tailNbr, String.valueOf(logs.get(i).details.get(0).qty), String.valueOf(logs.get(i).details.get(1).qty), true,logs.get(i).id);
                        }else {
                             a = new CustomDialog(act, String.valueOf(logs.get(i).details.get(0).qty), logs.get(i).tailNbr, logs.get(i).details.get(0).endingMeter, "-", true, logs.get(i).id);
                        }
               /* Intent a = new Intent(myContext, Order_view.class);
                act.startActivity(a);*/
                        a.show();
                    }
                });
                break;
        }


        if (i % 2 == 0) {
            personViewHolder.cv.setBackgroundResource(R.color.blanco);
        } else {
            personViewHolder.cv.setBackgroundResource(R.color.transparente);
        }


    }


    List<Log> logs;

    Log_adapter(List<Log> logs, Context myContext, Activity act) {
        this.logs = logs;
        this.myContext = myContext;
        this.act = act;
    }

    public int getItemCount() {
        return logs.size();
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public String parser(String s) {
        String end = "";
        int a = (int) Double.parseDouble(s);
        end = String.valueOf(a);
        return end;
    }
    private class CustomDialog extends Dialog implements
            android.view.View.OnClickListener {
        public Activity activity;
        public Button complete, cancel;
        private TextView qty_requested, tailnbr, meter1_delivered, meter2_delivered, total_text;
        private Button fsii;
        private String qty, tail, meter1, meter2;
        private int total;
        private boolean isFsii;
        public int id;

        String texto;

        public CustomDialog(Activity activity, String qty, String tail, String meter1, String meter2, boolean fsii,int id) {
            super(activity);
            this.activity = activity;
            this.qty = qty;
            this.tail = tail;
            this.meter1 = meter1;
            this.meter2 = meter2;
            isFsii = fsii;
            this.id=id;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.ticket_layout);

            complete = (Button) findViewById(R.id.ticket_complete);
            complete.setOnClickListener(this);
            complete.setText("View Ticket");
            cancel = (Button) findViewById(R.id.ticket_cancel);
            cancel.setText("Close");
            cancel.setOnClickListener(this);
            qty_requested = (TextView) findViewById(R.id.ticket_qry_rqst);
            tailnbr = (TextView) findViewById(R.id.ticket_tail);
            meter1_delivered = (TextView) findViewById(R.id.ticket_meter1_total);
            meter2_delivered = (TextView) findViewById(R.id.tick_meter2_total);
            fsii = (Button) findViewById(R.id.ticket_fsii);
            total_text = (TextView) findViewById(R.id.ticker_meter1Qty);
            int a=0;
            int b=0;
            if(!meter1.equals("null")&& !meter1.equals("-")) {
                a =(int) Double.parseDouble(meter1.toString());
                meter1=String.valueOf(a);
            }else{
                meter1="-";
            }
            if(!meter2.equals("null") && !meter2.equals("-"))
                b = Integer.parseInt(meter2);

            total = a + b;
            qty_requested.setText(qty);
            tailnbr.setText(tail);
            meter1_delivered.setText(meter1);
            meter2_delivered.setText(meter2);
            if (isFsii) {
                fsii.setBackground(activity.getResources().getDrawable(R.drawable.fsii_1));
            } else {
                fsii.setBackground(activity.getResources().getDrawable(R.drawable.fsii_2));
            }
            total_text.setText(String.valueOf(total));

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ticket_complete:
                    getPDF(id);
                    dd = new ProgressDialog(act);
                    dd.getWindow().setBackgroundDrawable(new ColorDrawable(act.getResources().getColor(R.color.transparente)));
                    dd.show();
                    dd.setContentView(R.layout.dialogo_progreso);
                    break;
                case R.id.ticket_cancel:
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    }

    ProgressDialog dd;

    public void getPDF(final int id) {
    String mUrl = "https://apistg.x1fbo.com/api/inventoryRpt/fuelTicket/";
    InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.GET, mUrl + id,
            new Response.Listener<byte[]>() {
                @Override
                public void onResponse(byte[] response) {
                    // TODO handle the response
                    try {

                        if (response != null) {
                            String fileName = Environment.getExternalStorageDirectory().getAbsolutePath();
                            String name = id+"ticket.pdf";
                            FileOutputStream outputStream = new FileOutputStream(new File(fileName,name));


                            outputStream.write(response);
                            outputStream.close();
                            Toast.makeText(act, "Download complete.", Toast.LENGTH_LONG).show();
                            System.out.println(act.getFilesDir()+"/"+name);
                            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ name);
                            Intent target = new Intent(Intent.ACTION_VIEW);
                            target.setDataAndType(Uri.fromFile(file),"application/pdf");
                            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                            Intent intent = Intent.createChooser(target, "Open File");
                            try {
                                act.startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                // Instruct the user to install a PDF reader here, or something
                            }
                        }
                        dd.dismiss();

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        android.util.Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");

                        Toast.makeText(myContext,"Error, Try again!",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        dd.dismiss();
                    }
                }
            }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            // TODO handle the error
            error.printStackTrace();
        }
    }, null);
    RequestQueue mRequestQueue = Volley.newRequestQueue(act, new HurlStack());
    mRequestQueue.add(request);
}

}