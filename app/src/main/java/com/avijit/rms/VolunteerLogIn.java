package com.avijit.rms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class VolunteerLogIn extends AppCompatActivity {
    private Button logInButton,signUpIntentButton;
    private EditText userNameEditText,passwordEditText;
    ImageView logoImage;
    TextInputLayout tran2,tran3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_volunteer_login_2);


        logInButton = findViewById(R.id.login_button);
        signUpIntentButton = findViewById(R.id.signup_intent_button);
        logoImage = findViewById(R.id.logo_image);
        userNameEditText = findViewById(R.id.user_name_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        tran2 = findViewById(R.id.username);
        tran3 = findViewById(R.id.password);


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
                Pair[] pairs = new Pair[5];
                pairs[1]= new Pair<View,String>(signUpIntentButton,"tran0");
                pairs[0]= new Pair<View,String>(logoImage,"tran1");
                pairs[2]= new Pair<View,String>(tran2,"tran2");
                pairs[3]= new Pair<View,String>(tran3,"tran3");
                pairs[4]= new Pair<View,String>(logInButton,"tran4");
                ActivityOptions options = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    options = ActivityOptions.makeSceneTransitionAnimation(VolunteerLogIn.this,pairs);
                }
                startActivity(intent,options.toBundle());
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
                            startActivity(new Intent(VolunteerLogIn.this,SearchByNid.class));
                        }catch (Exception e)
                        {
                            Toast.makeText(VolunteerLogIn.this, "Invalid username/password", Toast.LENGTH_SHORT).show();
                        }

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
                params.put("client_secret","Xhqe0rptK0qCmRjuXbw2PADML5fjaE3RJOBhTMHn");
                params.put("username",userNameEditText.getText().toString());
                params.put("password",passwordEditText.getText().toString());
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(VolunteerLogIn.this,MainActivity.class));
    }
}
