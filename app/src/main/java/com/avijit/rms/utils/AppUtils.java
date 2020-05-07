package com.avijit.rms.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avijit.rms.AddDonateSchedule;
import com.avijit.rms.MainActivity;
import com.avijit.rms.R;
import com.google.android.material.navigation.NavigationView;

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
    public static final RetryPolicy STRING_REQUEST_RETRY_POLICY = new RetryPolicy() {
        @Override
        public int getCurrentTimeout() {
            return 50000;
        }

        @Override
        public int getCurrentRetryCount() {
            return 50000;
        }

        @Override
        public void retry(VolleyError error) throws VolleyError {

        }
    };
    public final NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            switch (id)
            {
                case R.id.logout: {
                                    context.getSharedPreferences("RMS",MODE_PRIVATE).edit().putString("token","").apply();
                                    context.startActivity(new Intent(context,MainActivity.class));
                                  }break;
                case R.id.home: {
                    context.startActivity(new Intent(context,MainActivity.class));
                }break;
                case R.id.nav_add_donate_schedule: {
                    context.startActivity(new Intent(context, AddDonateSchedule.class));
                }
            }
            return true;
        }
    };
    public final Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
        }
    };

}
