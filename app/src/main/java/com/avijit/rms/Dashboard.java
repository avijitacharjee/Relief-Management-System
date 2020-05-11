package com.avijit.rms;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avijit.rms.utils.AppUtils;

import org.json.JSONObject;

public class Dashboard extends AppCompatActivity {
    TextView totalTextView,recoveredTextView,deathTextView;
    TextView next;
    AppUtils appUtils;
    AlertDialog dialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        appUtils = new AppUtils(this);
        dialog = appUtils.dialog;
        fieldInit();
        setData();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }
    private void setData(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://aniksen.me/covidbd/api/corona-summary";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject object = new JSONObject(response);
                    object = object.getJSONObject("data");
                    totalTextView.setText(object.getString("totalcases"));
                    recoveredTextView.setText(object.getString("recovered"));
                    deathTextView.setText(object.getString("deaths"));

                }catch (Exception e){}
                dialog.dismiss();
            }
        },appUtils.errorListener);
        stringRequest.setRetryPolicy(AppUtils.STRING_REQUEST_RETRY_POLICY);
        queue.add(stringRequest);
        dialog.show();
    }
    private void fieldInit(){
        totalTextView = findViewById(R.id.totalCases);
        recoveredTextView = findViewById(R.id.recovered);
        deathTextView = findViewById(R.id.death);
        next = findViewById(R.id.next);
    }
}
