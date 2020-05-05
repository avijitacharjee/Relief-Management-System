package com.avijit.rms.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avijit.rms.MainActivity;

import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class AppUtils {
    private Context context;

    public AppUtils(Context context) {
        this.context = context;
    }

    public void getLocations (){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = "https://aniksen.me/covidbd/api/locations";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                context.getSharedPreferences("RMS",MODE_PRIVATE).edit().putString("locations",response).apply();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
    }

}
