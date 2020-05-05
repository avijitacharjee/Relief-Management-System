package com.avijit.rms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolunteerSignUp extends AppCompatActivity {
    ProgressDialog progressDialog;
    Button loginIntentButton,goButton;
    ImageView logoImage;
    TextInputLayout tran2,tran3;
    Spinner typeSpinner;
    String types[] = new String[]{"As Individual","As Thana User","As Company"};
    String typeIds[] ;
    EditText nameEditText,emailEditText,phoneEditText,nidEditText,passwordEditText,confirmPasswordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_sign_up);

        loginIntentButton = findViewById(R.id.signup_intent_button);
        goButton = findViewById(R.id.go);
        logoImage = findViewById(R.id.logo_image);
        tran2 = findViewById(R.id.username);
        tran3 = findViewById(R.id.password);
        typeSpinner = findViewById(R.id.type_spinner);
        initEditTexts();
        setTypeSpinner();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,types);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter2);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parent.getChildAt(0)).setBackgroundColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        loginIntentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),VolunteerLogIn.class);
                Pair[] pairs = new Pair[5];
                pairs[1]= new Pair<View,String>(loginIntentButton,"tran0");
                pairs[0]= new Pair<View,String>(logoImage,"tran1");
                pairs[2]= new Pair<View,String>(tran2,"tran2");
                pairs[3]= new Pair<View,String>(tran3,"tran3");
                pairs[4]= new Pair<View,String>(goButton,"tran4");
                ActivityOptions options = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    options = ActivityOptions.makeSceneTransitionAnimation(VolunteerSignUp.this,pairs);
                }
                startActivity(intent,options.toBundle());
            }
        });
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(formIsValid())
                {
                    v();
                }
            }
        });

    }

    /*
     * Volley request for registration
     * @Params no params
     */
    public void setTypeSpinner(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://aniksen.me/covidbd/api/user-type";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    String[] t = new String[jsonArray.length()];
                    typeIds = new String[jsonArray.length()];
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        t[i] = jsonArray.getJSONObject(i).getString("name");
                        typeIds[i]=jsonArray.getJSONObject(i).getString("id");
                    }
                    types=t;
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(VolunteerSignUp.this,android.R.layout.simple_spinner_dropdown_item,types);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    typeSpinner.setAdapter(adapter2);

                }catch (Exception e) {

                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VolunteerSignUp.this, ""+error, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>();
                headers.put("","");
                return super.getHeaders();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };
        requestQueue.add(stringRequest);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
    }

    /*
    * Volley request for registration
    * @Params no params
    */
    public void v() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://aniksen.me/covidbd/api/saveuser";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(VolunteerSignUp.this, response , Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(VolunteerSignUp.this, error.toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("name",nameEditText.getText().toString());
                params.put("email",emailEditText.getText().toString());
                params.put("password",passwordEditText.getText().toString());
                params.put("phone",phoneEditText.getText().toString());
                params.put("nid",nidEditText.getText().toString());
                params.put("tbl_user_types_id",typeIds[typeSpinner.getSelectedItemPosition()]);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>();
                headers.put("Content-Type","application/json");
                headers.put("Content-Type","application/x-www-form-urlencoded");

                
                return headers;
            }
        };
        stringRequest.setRetryPolicy(new RetryPolicy() {
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
        });
        requestQueue.add(stringRequest);
        progressDialog = new ProgressDialog(VolunteerSignUp.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

    }

    /*
    * initializes all editTexts
    * @Params no params
    */
    private void initEditTexts()
    {
        nameEditText = findViewById(R.id.full_name_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        nidEditText = findViewById(R.id.nid_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(VolunteerSignUp.this,MainActivity.class));
        /*finish();
        overridePendingTransition(0,0);*/
    }
    private boolean formIsValid()
    {
        boolean valid = true;
        if(nameEditText.getText().toString().equals(""))
        {
            tran2.setError("Name can't be empty");
            valid = false;
        }
        if(emailEditText.getText().toString().equals(""))
        {
            emailEditText.setError("Email can't be empty");
            valid = false;
        }
        if(phoneEditText.getText().toString().equals(""))
        {
            phoneEditText.setError("Phone can't be empty");
            valid = false;
        }
        if(nidEditText.getText().toString().equals(""))
        {
            nidEditText.setError("NID can't be empty");
            valid = false;
        }
        if(passwordEditText.getText().toString().equals(""))
        {
            passwordEditText.setError("Password can't be empty");
            valid = false;
        }
        if(confirmPasswordEditText.getText().toString().equals(""))
        {
            confirmPasswordEditText.setError("Confirm password");
            valid = false;
        }
        if(!confirmPasswordEditText.getText().toString().equals(passwordEditText.getText().toString()))
        {
            confirmPasswordEditText.setError("Passwords doesn't matched");
            valid = false;
        }
        return valid;
    }
}
