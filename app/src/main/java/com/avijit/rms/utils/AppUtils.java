package com.avijit.rms.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

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
import com.avijit.rms.ShowPendingDonateSchedule;
import com.google.android.material.navigation.NavigationView;

import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class AppUtils {
    private Context context;
    public AlertDialog dialog;

    public AppUtils(Context context) {
        this.context = context;
        setProgressDialog();
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
            return 5;
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
                    break;
                }
                case R.id.home: {
                    context.startActivity(new Intent(context,MainActivity.class));
                    break;
                }
                case R.id.nav_add_donate_schedule: {
                    context.startActivity(new Intent(context, AddDonateSchedule.class));
                    break;
                }
                case R.id.nav_show_pending_donate_schedule: {
                    context.startActivity(new Intent(context, ShowPendingDonateSchedule.class));
                    break;
                }
            }
            return true;
        }
    };
    public final Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
            if(dialog.isShowing()){
                dialog.dismiss();
            }
        }
    };

    public void setProgressDialog() {

        int llPadding = 30;
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(llPadding, llPadding, llPadding, llPadding);
        ll.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        ll.setLayoutParams(llParam);

        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setIndeterminate(true);
        progressBar.setPadding(0, 0, llPadding, 0);
        progressBar.setLayoutParams(llParam);

        llParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        TextView tvText = new TextView(context);
        tvText.setText("Loading ...");
        tvText.setTextColor(Color.parseColor("#000000"));
        tvText.setTextSize(20);
        tvText.setLayoutParams(llParam);

        ll.addView(progressBar);
        ll.addView(tvText);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setView(ll);

        dialog = builder.create();
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(layoutParams);
        }
    }

}
