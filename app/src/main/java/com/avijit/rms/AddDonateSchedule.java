package com.avijit.rms;

import androidx.annotation.BinderThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avijit.rms.data.local.AppDatabase;
import com.avijit.rms.data.local.entities.Area;
import com.avijit.rms.data.local.entities.District;
import com.avijit.rms.data.local.entities.Division;
import com.avijit.rms.utils.AppUtils;
import com.avijit.rms.utils.EndDrawerToggle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.os.AsyncTask.execute;

public class AddDonateSchedule extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    AlertDialog dialog ;
    DatePickerDialog datePickerDialog;
    TextView dateEditText;
    Spinner divisionSpinner,districtSpinner,typeSpinner,areaSpinner;
    EditText addressEditText;
    TextView nextButton;
    DrawerLayout drawer;
    AppBarConfiguration mAppBarConfiguration;

    private List<Division> divisionList =new ArrayList<>();
    private List<District> districtList =new ArrayList<>();
    private List<String> typeList =new ArrayList<>();
    private List<Area> areaList =new ArrayList<>();

    /**
     * List of areas after selecting type
     */
    private List<Area> selectedArea = new ArrayList<>();
    /**
     * @param savedInstanceState previously saved bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_donate_schedule);
        dateEditText = findViewById(R.id.date_picker);
        dialog = new AppUtils(this).dialog;
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(AddDonateSchedule.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                dateEditText.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("RMS");
        toolbar.setSubtitle("Add a schedule for giving relief");
        //toolbar.setLogo(R.drawable.ic_exit_to_app_black_24dp);
        toolbar.setBackgroundColor(Color.BLACK);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Window window = getWindow();

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(AddDonateSchedule.this);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder()
                .setDrawerLayout(drawer)
                .build();
        EndDrawerToggle toggle = new EndDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.closeDrawer(Gravity.RIGHT);
                } else {
                    drawer.openDrawer(Gravity.RIGHT);
                }
            }
        });



        divisionSpinner= findViewById(R.id.division_spinner);
        districtSpinner = findViewById(R.id.district_spinner);
        typeSpinner = findViewById(R.id.type_spinner);
        areaSpinner = findViewById(R.id.area_spinner);
        addressEditText= findViewById(R.id.address_edit_text);
        nextButton = findViewById(R.id.next_button);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(AddDonateSchedule.this);
                divisionList= db.divisionDao().getAll();
                String[] divisions = new String[divisionList.size()+1];
                divisions[0]="--Select Division--";
                for(int i=0;i<divisionList.size();i++)
                {
                    divisions[i+1]=divisionList.get(i).name;
                }
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddDonateSchedule.this, R.layout.spinner_layout,divisions);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        divisionSpinner.setAdapter(adapter);
                    }
                });

            }
        });

        setDistricts();
        setTypes();
        setAreas();
        divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parent.getChildAt(0)).setBackgroundColor(Color.WHITE);
                if(position>0)
                {
                    setDistricts();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0)
                {
                    setTypes();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parent.getChildAt(0)).setBackgroundColor(Color.WHITE);
                if(position>0)
                {
                    setAreas();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                RequestQueue requestQueue= Volley.newRequestQueue(AddDonateSchedule.this);
                String url = "https://aniksen.me/covidbd/api/schedule-create";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(AddDonateSchedule.this, response, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }, new AppUtils(AddDonateSchedule.this).errorListener){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("user_id","1");
                        params.put("schedule_date",dateEditText.getText().toString());
                        params.put("division_id",divisionList.get(divisionSpinner.getSelectedItemPosition()-1).divisionId);
                        params.put("district_id",districtList.get(districtSpinner.getSelectedItemPosition()-1).districtId);
                        params.put("area_id",areaList.get(areaSpinner.getSelectedItemPosition()-1).areaId);
                        params.put("address",addressEditText.getText().toString());
                        params.put("company_id","1");
                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return super.getHeaders();
                    }
                };
                stringRequest.setRetryPolicy(AppUtils.STRING_REQUEST_RETRY_POLICY);
                requestQueue.add(stringRequest);
                dialog.show();
            }
        });
    }
    private void setDistricts() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(AddDonateSchedule.this);
                String[] districts = new String[1];
                districts[0]="--Select Districts--";
                if(divisionSpinner.getSelectedItemPosition()>0)
                {
                    districtList = db.districtDao().getDistrictByDivisionId(divisionList.get(divisionSpinner.getSelectedItemPosition()-1).divisionId);
                    districts = new String[districtList.size()+1];
                    districts[0]="--Select Districts";
                    for(int i=0;i<districtList.size();i++)
                    {
                        districts[i+1]=districtList.get(i).name;
                    }
                }
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddDonateSchedule.this, R.layout.spinner_layout,districts);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        districtSpinner.setAdapter(adapter);
                    }
                });
            }
        });
    }
    private void setTypes() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(AddDonateSchedule.this);
                String[] types = new String[1];
                types[0]="--Select Types--";
                if(districtSpinner.getSelectedItemPosition()>0) {
                    areaList = db.areaDao().getAreasByDistrictId(districtList.get(districtSpinner.getSelectedItemPosition()-1).districtId);
                    for(int i=0;i<areaList.size();i++) {
                        //districts[i+1]=districtList.get(i).name;
                        Area area = areaList.get(i);
                        if(!typeList.contains(area.type)) {
                            typeList.add(area.type);
                        }
                    }
                    types = new String[typeList.size()+1];
                    types[0]="--Select Types--";
                    for(int i=0;i<typeList.size();i++){
                        types[i+1]=typeList.get(i);
                    }
                }
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddDonateSchedule.this, R.layout.spinner_layout,types);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        typeSpinner.setAdapter(adapter);
                    }
                });
            }
        });
    }
    private void setAreas(){
        selectedArea.clear();
        for(int i=0;i<areaList.size();i++){
            if(areaList.get(i).type.equals(typeList.get(typeSpinner.getSelectedItemPosition()-1))){
                selectedArea.add(areaList.get(i));
            }
        }
        String[] areas = new String[1];
        areas[0] = "--Select Area--";
        if(selectedArea.size()>0){
            areas= new String[selectedArea.size()+1];
            areas[0]="--Select Area--";
            for(int i=0;i<selectedArea.size();i++){
                areas[i+1]=selectedArea.get(i).name;
            }
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddDonateSchedule.this, R.layout.spinner_layout,areas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                areaSpinner.setAdapter(adapter);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
