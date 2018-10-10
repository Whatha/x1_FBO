package com.whathadesign.x1_fbo;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Daniel on 02/05/2018.
 */

public class Queue {

    //Declare a private  RequestQueue variable
    private RequestQueue requestQueue;
    Context context;
    private static Queue mInstance;

    private Queue(Context context){
        this.context=context;
    }


    public static synchronized Queue getInstance()
    {
        return mInstance;
    }
    /*
    Create a getRequestQueue() method to return the instance of
    RequestQueue.This kind of implementation ensures that
    the variable is instatiated only once and the same
    instance is used throughout the application
     */
    public RequestQueue getRequestQueue()
    {
        if (requestQueue==null)
            requestQueue= Volley.newRequestQueue(context);

        return requestQueue;
    }

    /*
         public method to add the Request to the the single
    instance of RequestQueue created above.Setting a tag to every
    request helps in grouping them. Tags act as identifier
    for requests and can be used while cancelling them
    */
    public void addToRequestQueue(Request request, String tag)
    {
        request.setTag(tag);
        getRequestQueue().add(request);

    }

/*
Cancel all the requests matching with the given tag
     */

    public void cancelAllRequests(String tag)
    {
        getRequestQueue().cancelAll(tag);
    }
}