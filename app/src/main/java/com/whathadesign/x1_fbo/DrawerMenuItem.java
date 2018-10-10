package com.whathadesign.x1_fbo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

/**
 * Created by Daniel on 12/04/2018.
 */

@Layout(R.layout.drawer_item)
public class DrawerMenuItem {

    public static final int PENDING_FUELING = 1;
    public static final int NEW_FUELING = 2;
    public static final int TRUCK_LOG = 3;
    public static final int TOP_OFF = 4;
    public static final int RECIRC = 5;
    public static final int TRANSFER = 6;
    public static final int DEFUEL = 7;
    public static final int LOGOUT = 8;
    public static final int CHANGE_TRUCK = 9;
    public static final int LOGINOUT = 10;

    private int mMenuPosition;
    private Context mContext;
    private Activity mActivity;
    private DrawerCallBack mCallBack;

    @View(R.id.itemNameTxt)
    private TextView itemNameTxt;

    @View(R.id.itemIcon)
    private ImageView itemIcon;

    public DrawerMenuItem(Activity activity,Context context, int menuPosition) {
        mContext = context;
        mActivity=activity;
        mMenuPosition = menuPosition;
    }

    @Resolve
    private void onResolved() {
        switch (mMenuPosition){
            case PENDING_FUELING:
                itemNameTxt.setText("Pending Fuelings");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.pending_fuelings_icon));
                break;
            case NEW_FUELING:
                itemNameTxt.setText("New Fueling");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.new_fueling_icon));
                break;
            case TRUCK_LOG:
                itemNameTxt.setText("Truck Log");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_truck_log));
                break;
            case TOP_OFF:
                itemNameTxt.setText("Top Off");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_top_off));
                break;
            case RECIRC:
                itemNameTxt.setText("Recirc");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_recirc));
                break;
            case TRANSFER:
                itemNameTxt.setText("Transfer");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_transfer));
                break;
            case DEFUEL:
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_defuel));
                itemNameTxt.setText("Defuel");
                break;
            case LOGOUT:
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.close_truck));
                itemNameTxt.setText("Close Truck");
                break;
            case CHANGE_TRUCK:
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.arrow));
                itemNameTxt.setText("Change Truck");
                break;
            case LOGINOUT:
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.logout));
                itemNameTxt.setText("Log Out");
                break;
        }
    }

    @Click(R.id.mainView)
    private void onMenuItemClick(){
        Intent b= new Intent(mContext,Progress.class);
        b.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        switch (mMenuPosition){
            case PENDING_FUELING:
                Intent a= new Intent(mContext,Fuel_feed.class);
                a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                a.putExtra("status","holi");
                mContext.startActivity(a);
                if(mCallBack != null)mCallBack.onProfileMenuSelected();
                break;
            case NEW_FUELING:
                Intent fueling= new Intent(mContext,New_fueling.class);
                fueling.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                fueling.putExtra("nombre","Fueling");
                mContext.startActivity(fueling);
                if(mCallBack != null)mCallBack.onGroupsMenuSelected();
                break;
            case TRUCK_LOG:
                Intent log= new Intent(mContext,TruckLog.class);
                log.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(log);
                if(mCallBack != null)mCallBack.onGroupsMenuSelected();
                break;
            case TOP_OFF:
                Intent topoff= new Intent(mContext,Top_off_feed.class);
                topoff.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                topoff.putExtra("nombre","Top off");
                mContext.startActivity(topoff);
                if(mCallBack != null)mCallBack.onGroupsMenuSelected();
                break;
            case RECIRC:
                Intent recirc= new Intent(mContext,Recirc.class);
                recirc.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                recirc.putExtra("nombre","Recirc");
                mContext.startActivity(recirc);
                if(mCallBack != null)mCallBack.onGroupsMenuSelected();
                break;
            case TRANSFER:
                Intent trn= new Intent(mContext,Transfer.class);
                trn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                trn.putExtra("nombre","Transfer");
                mContext.startActivity(trn);
                if(mCallBack != null)mCallBack.onSettingsMenuSelected();
                break;
            case DEFUEL:
                b.putExtra("nombre","Defuel");
                mContext.startActivity(b);
                if(mCallBack != null)mCallBack.onTermsMenuSelected();
                break;
            case LOGOUT:
                CloseDialog close = new CloseDialog(mActivity,mContext, "");
                close.show();
                close.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT);
                close.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);

                if(mCallBack != null)mCallBack.onLogoutMenuSelected();
                break;
            case CHANGE_TRUCK:
                Intent chng= new Intent(mContext,truck_feed.class);
                chng.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(chng);
                if(mCallBack != null)mCallBack.onLogoutMenuSelected();
                break;
            case LOGINOUT:
                Intent asd= new Intent(mContext,Login.class);
                asd.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(asd);
                if(mCallBack != null)mCallBack.onLogoutMenuSelected();
                break;
        }
    }

    public void setDrawerCallBack(DrawerCallBack callBack) {
        mCallBack = callBack;
    }

    public interface DrawerCallBack{
        void onProfileMenuSelected();
        void onRequestMenuSelected();
        void onGroupsMenuSelected();
        void onMessagesMenuSelected();
        void onNotificationsMenuSelected();
        void onSettingsMenuSelected();
        void onTermsMenuSelected();
        void onLogoutMenuSelected();
    }
}