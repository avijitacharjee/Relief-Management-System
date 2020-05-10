package com.avijit.rms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avijit.rms.adapters.ReliefScheduleRecyclerViewAdapter;
import com.avijit.rms.data.local.AppDatabase;
import com.avijit.rms.data.local.entities.Area;
import com.avijit.rms.data.local.entities.District;
import com.avijit.rms.data.local.entities.Division;
import com.avijit.rms.utils.AppUtils;
import com.avijit.rms.utils.EndDrawerToggle;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowPendingDonateSchedule extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    AppUtils appUtils;
    AlertDialog dialog;
    DrawerLayout drawer;
    AppBarConfiguration mAppBarConfiguration;
    RecyclerView recyclerView;
    ReliefScheduleRecyclerViewAdapter adapter;

    List<String> sls = new ArrayList<>();
    List<String> dates = new ArrayList<>();
    List<String> names = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pending_donate_schedule);

        appUtils = new AppUtils(this);
        this.dialog = appUtils.dialog;

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("RMS");
        toolbar.setSubtitle("Pending schedules for giving relief");
        //toolbar.setLogo(R.drawable.ic_exit_to_app_black_24dp);
        toolbar.setBackgroundColor(Color.BLACK);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(ShowPendingDonateSchedule.this);
        navigationView.setNavigationItemSelectedListener(appUtils.navigationItemSelectedListener);
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
        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        String userId;
        try{
            JSONObject object = new JSONObject(getSharedPreferences("RMS",MODE_PRIVATE).getString("user",""));
            userId = object.getString("id");
        }

        catch (Exception e){userId="1";}
        fetchData(userId);

        adapter = new ReliefScheduleRecyclerViewAdapter(sls,dates,names);
        recyclerView.setAdapter(adapter);
    }
    private void fetchData(String id) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://aniksen.me/covidbd/api/pending-schedules/"+id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        sls.add(""+(i+1));
                        dates.add(jsonArray.getJSONObject(i).getString("schedule_date"));
                        names.add(jsonArray.getJSONObject(i).getString("name"));
                    }
                    adapter.notifyDataSetChanged();
                }catch (Exception e ){

                }
            }
        }, appUtils.errorListener);
        stringRequest.setRetryPolicy(AppUtils.STRING_REQUEST_RETRY_POLICY);
        requestQueue.add(stringRequest);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}