package com.avijit.rms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FamilyAddress extends AppCompatActivity {
    Button nextButton;

    Spinner divisionSpinner,districtSpinner,typeSpinner;
    EditText areaEditText;

    ProgressBar progressBar;
    ProgressDialog progressDialog;
    String[] divisions = {"--Select division--"};
    String[] districts = {"--Select district--"};
    String[] types = {"--Select type--", "A","B" };

    String division="";
    String district="";
    String type="";


   // Intent intent = new Intent(getApplicationContext(),FamilyAddress.class);

    private List<String> divisionsIdList =new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_address);

        divisionSpinner= findViewById(R.id.division_spinner);
        districtSpinner = findViewById(R.id.district_spinner);
        typeSpinner = findViewById(R.id.type_spinner);
        areaEditText = findViewById(R.id.area_edit_text);

        nextButton = findViewById(R.id.next_button);


       /* String areas[] = {"Brammanbaria","Noakhali","Barishal"};
        ArrayAdapter<String> areaAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, areas);
        areaEditText.setThreshold(2);
        areaEditText.setAdapter(areaAdapter);*/


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,divisions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        divisionSpinner.setAdapter(adapter);
        setDivisions();

        divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(FamilyAddress.this, divisionSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                division=divisionSpinner.getSelectedItem().toString();
                if(position>0)
                {
                    Toast.makeText(getApplicationContext(), divisionsIdList.get(position-1)+"", Toast.LENGTH_SHORT).show();
                    setDistricts(divisionsIdList.get(position-1));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,districts);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(adapter1);
        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                district = districtSpinner.getSelectedItem().toString();
                //setDistricts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,types);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter2);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = typeSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent familyRegistration = new Intent(getApplicationContext(),FamilyRegistration.class);
               familyRegistration.putExtra("Division",division);
               familyRegistration.putExtra("District",district);
               familyRegistration.putExtra("Type",type);
               if(formValidationPassed())
               {
                   startActivity(familyRegistration);
               }
               else
               {
                  //  Toast.makeText(FamilyAddress.this, "All fields are required", Toast.LENGTH_SHORT).show();
                   Toast toast = Toast.makeText(getApplicationContext(),"All fields are required" , Toast.LENGTH_SHORT);
                   View view = toast.getView();

//Gets the actual oval background of the Toast then sets the colour filter
                   view.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

//Gets the TextView from the Toast so it can be editted
                   TextView text = view.findViewById(android.R.id.message);
                   text.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                   text.setTextColor(Color.WHITE);

                   toast.show();
               }
            }
        });
    }
    private boolean formValidationPassed()
    {
        boolean flag = true;
        if(division.startsWith("-"))
        {
            flag=false;
        }
        if(district.startsWith("-"))
        {
            flag=false;
        }
        if(type.startsWith("-"))
        {
            flag=false;
        }
        if(areaEditText.length()==0)
        {
            flag=false;
        }
        return flag;
    }
    public void setDivisions()
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://aniksen.me/covidbd/api/divisions";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray data = jsonObject.getJSONArray("divisions");
                            int length = data.length();
                            String[] divs = new String[length+1];
                            divs[0]="--Select Division--";
                            for(int i=0;i<length;i++)
                            {
                                JSONObject division = data.getJSONObject(i);
                                divs[i+1]=(division.getString("name"));
                                divisionsIdList.add(division.getString("id"));
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(FamilyAddress.this, android.R.layout.simple_spinner_dropdown_item,divs);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            divisionSpinner.setAdapter(adapter);


                        }catch (JSONException e)
                        {
                            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /*textView.setText("Failed"+error.toString());*/
                Toast.makeText(getApplicationContext(),"Net connection failed",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }){

            /* passing request body */
            /*protected Map<String,String> getParams()
            {
                Map<String,String> params = new HashMap<String,String>() ;
                params.put("access_key","6808");
                params.put("get_categories","1");

                return params;
            }*/
            /** Passing some request headers* */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        queue.add(stringRequest);
        progressDialog = new ProgressDialog(FamilyAddress.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
    }
    public void setDistricts(String divisionId)
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://aniksen.me/covidbd/api/districts/"+divisionId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray data = jsonObject.getJSONArray("districts");
                            int length = data.length();
                            String[] divs = new String[length+1];
                            divs[0]="--Select Division--";
                            for(int i=0;i<length;i++)
                            {
                                JSONObject division = data.getJSONObject(i);
                                divs[i+1]=(division.getString("name"));
                                //divisionsIdList.add(division.getString("id"));
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(FamilyAddress.this, android.R.layout.simple_spinner_dropdown_item,divs);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            districtSpinner.setAdapter(adapter);

                        }catch (JSONException e)
                        {
                            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /*textView.setText("Failed"+error.toString());*/
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        }){

            /* passing request body */
            /*protected Map<String,String> getParams()
            {
                Map<String,String> params = new HashMap<String,String>() ;
                params.put("access_key","6808");
                params.put("get_categories","1");

                return params;
            }*/
            /** Passing some request headers* */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        queue.add(stringRequest);
    }
}
