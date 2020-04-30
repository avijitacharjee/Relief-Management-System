package com.avijit.rms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class VolunteerLogIn extends AppCompatActivity {
    Button logInButton,signUpIntentButton;
    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_log_in);

        logInButton = findViewById(R.id.login_button);
        signUpIntentButton = findViewById(R.id.signup_intent_button);
        SharedPreferences sharedPref= getSharedPreferences("RMS",MODE_PRIVATE);
        if(!sharedPref.getString("token","").equals(""))
        {
            Intent intent = new Intent(getApplicationContext(),SearchByNid.class);
            startActivity(intent);
        }

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
                //startActivity(new Intent(getApplicationContext(),SearchByNid.class));
            }
        });

        signUpIntentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),VolunteerSignUp.class);
            }
        });



    }
    private boolean checkLogin()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://aniksen.me/covidbd/oauth/token";
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String token = jsonObject.getString("access_token");
                            SharedPreferences sharedPreferences = getSharedPreferences("RMS",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("token",token);
                            editor.commit();
                        }catch (Exception e)
                        {
                            Toast.makeText(VolunteerLogIn.this, "Invalid username/password", Toast.LENGTH_SHORT).show();
                        }
                       /*  */
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(VolunteerLogIn.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("grant_type","password");
                params.put("client_id","2");
                params.put("client_secret","ick8KJoYgvd4blo3NWbAk9KeWMEOx5XOlQr6ryY5");
                params.put("username","avijitach@gmail.com");
                params.put("password","password");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>();
                //headers.put("Content-Type","application/*");
                headers.put("Content-Type", "application/json");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };
        requestQueue.add(stringRequest);

        return true;
    }

}
