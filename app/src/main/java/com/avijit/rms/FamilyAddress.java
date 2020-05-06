package com.avijit.rms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
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
import com.avijit.rms.location.AppLocationService;
import com.avijit.rms.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FamilyAddress extends AppCompatActivity {
    TextView nextButton;

    Spinner divisionSpinner,districtSpinner,typeSpinner,areaSpinner;
    EditText addressEditText;

    ProgressBar progressBar;
    ProgressDialog progressDialog;
    String[] divisions =  {"--Select division--"};
    String[] districts = {"--Select district--"};
    String[] areas = {"--Select area--"};
    String[] types = {"--Select type--", "A","B" };

    String division="";
    String district="";
    String type="";
    public int divisionId;
    public int districtId;
    public int areaId;

   // Intent intent = new Intent(getApplicationContext(),FamilyAddress.class);

    private List<String> divisionsIdList =new ArrayList<>();
    private List<String> districtIdList =new ArrayList<>();
    private List<String> areaIdList =new ArrayList<>();

    public static JSONObject areaResponse=null;
    List<String> typesList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_address);
        //TODO locations background task

        divisionSpinner= findViewById(R.id.division_spinner);
        districtSpinner = findViewById(R.id.district_spinner);
        typeSpinner = findViewById(R.id.type_spinner);
        areaSpinner = findViewById(R.id.area_spinner);
        addressEditText= findViewById(R.id.address_edit_text);
        nextButton = findViewById(R.id.next_button);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("RMS");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout,divisions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        divisionSpinner.setAdapter(adapter);
        setDivisions();

        divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parent.getChildAt(0)).setBackgroundColor(Color.WHITE);
                //Toast.makeText(FamilyAddress.this, divisionSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                division=divisionSpinner.getSelectedItem().toString();
                if(position>0)
                {
                    divisionId = Integer.parseInt(divisionsIdList.get(position-1));
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
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parent.getChildAt(0)).setBackgroundColor(Color.WHITE);
                //district = districtSpinner.getSelectedItem().toString();
                if(position>0)
                {
                    districtId = Integer.parseInt(districtIdList.get(position-1));
                    setTypes(districtId);
                }
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
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parent.getChildAt(0)).setBackgroundColor(Color.WHITE);
                type = typeSpinner.getSelectedItem().toString();
                setAreas(type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        typesList.add("--Select Types--");

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,new String[]{"--Select area--"});
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaSpinner.setAdapter(adapter3);
        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parent.getChildAt(0)).setBackgroundColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(formValidationPassed())
               {
                   Intent familyRegistration = new Intent(getApplicationContext(),FamilyRegistration.class);
                   //ids
                   familyRegistration.putExtra("divisionId",divisionsIdList.get(divisionSpinner.getSelectedItemPosition()-1));
                   familyRegistration.putExtra("districtId",districtIdList.get(districtSpinner.getSelectedItemPosition()-1));
                   familyRegistration.putExtra("type",type);
                   familyRegistration.putExtra("address",addressEditText.getText().toString());
                   familyRegistration.putExtra("areaId",areaIdList.get(areaSpinner.getSelectedItemPosition()-1));
                   //Original values
                   familyRegistration.putExtra("division",divisionSpinner.getSelectedItem().toString());
                   familyRegistration.putExtra("district",districtSpinner.getSelectedItem().toString());
                   familyRegistration.putExtra("area",areaSpinner.getSelectedItem().toString());
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
    public void setAreas(String type){
        if(areaResponse!=null){
            try{
                JSONArray jsonArray = areaResponse.getJSONArray("areas");
                List<String> areasList = new ArrayList<>();
                areasList.add("--Select Area--");
                for(int i=0;i<jsonArray.length();i++) {
                    if(jsonArray.getJSONObject(i).getString("type").equals(type)){
                        areasList.add(jsonArray.getJSONObject(i).getString("name"));
                    }
                }
                areas= new String[areasList.size()];
                for(int i=0;i<areasList.size();i++){
                    areas[i] = areasList.get(i);
                }
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,areas);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                areaSpinner.setAdapter(adapter2);

            }catch (Exception e){

            }
        }
    }
    public void setTypes(int id) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://aniksen.me/covidbd/api/areas/"+id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    areaResponse = jsonObject;
                    JSONArray jsonArray = jsonObject.getJSONArray("areas");
                    for(int i=0;i<jsonArray.length();i++) {
                        if(!typesList.contains(jsonArray.getJSONObject(i).getString("type"))){
                            typesList.add(jsonArray.getJSONObject(i).getString("type"));
                        }
                        areaIdList.add(jsonArray.getJSONObject(i).getString("id"));
                    }
                    String[] t = new String[jsonArray.length()];
                    for (int i = 0; i < typesList.size(); i++) {
                        t[i] = typesList.get(i);
                    }
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(FamilyAddress.this,android.R.layout.simple_spinner_dropdown_item,t);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    typeSpinner.setAdapter(adapter2);

                }catch (Exception e){
                    System.out.println(e);
                }
                progressDialog.dismiss();

            }
        }, commonErrorListener);
        stringRequest.setRetryPolicy(AppUtils.STRING_REQUEST_RETRY_POLICY);
        requestQueue.add(stringRequest);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
    }
    Response.ErrorListener commonErrorListener =new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            /*textView.setText("Failed"+error.toString());*/
            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    };

    public boolean onOptionsItemSelected(MenuItem item){
        super.onBackPressed();
        return true;
    }
    private boolean formValidationPassed() {

        //TODO complete validation
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
        if(addressEditText.length()==0)
        {
            flag=false;
        }
        return flag;
    }
    public void setDivisions() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="https://aniksen.me/covidbd/api/divisions";

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
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
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
        stringRequest.setRetryPolicy(AppUtils.STRING_REQUEST_RETRY_POLICY);
        queue.add(stringRequest);
        progressDialog = new ProgressDialog(FamilyAddress.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
    }
    public void setDistricts(String divisionId) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="https://aniksen.me/covidbd/api/districts/"+divisionId;

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
                            districtIdList.clear();
                            for(int i=0;i<length;i++)
                            {
                                JSONObject district = data.getJSONObject(i);
                                divs[i+1]=(district.getString("name"));
                                districtIdList.add(district.getString("id"));
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
        stringRequest.setRetryPolicy(AppUtils.STRING_REQUEST_RETRY_POLICY);
        queue.add(stringRequest);
        ProgressBar progressBar = new ProgressBar(this);

    }
}
