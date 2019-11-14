package com.example.debatetracker;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class HttpFacade {

    private static HttpFacade instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private HttpFacade(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized void loadInstance(Context context) {
        if (instance == null) {
            instance = new HttpFacade(context);
        }
    }

    public static HttpFacade getInstance() {
        return instance;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
