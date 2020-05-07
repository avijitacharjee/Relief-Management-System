package com.avijit.rms;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avijit.rms.data.local.AppDatabase;
import com.avijit.rms.data.local.entities.Area;
import com.avijit.rms.data.local.entities.District;
import com.avijit.rms.data.local.entities.Division;
import com.avijit.rms.data.local.entities.User;
import com.avijit.rms.utils.AppUtils;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity {
    TextView wantToDonateButton,needHelpButton;
    private static final int PERMISSION_REQUEST_CODE = 200;
    int k;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        wantToDonateButton = findViewById(R.id.want_to_donate_button);
        needHelpButton = findViewById(R.id.need_help_button);

        wantToDonateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),VolunteerLogIn.class);
                startActivity(intent);
            }
        });
        setLocations();



        wantToDonateButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase db = AppDatabase.getInstance(MainActivity.this);
                        List<Division> divisions = db.divisionDao().getAll();
                        List<District> districts = db.districtDao().getAll();
                        List<Area> areas = db.areaDao().getAll();
                        System.out.println(divisions);
                    }
                });

                return true;
            }
        });
        needHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(getApplicationContext(),FamilyAddress.class);
                startActivity(intent);
            }
        });
        AppUtils appUtils = new AppUtils(MainActivity.this);

        requestPermission();

    }
    public void setLocations(){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://aniksen.me/covidbd/api/locations";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final AppDatabase db = AppDatabase.getInstance(MainActivity.this);

                    List<Division> divisionList = new ArrayList<>();
                    List<District> districtList = new ArrayList<>();
                    List<Area> areaList = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray locations = jsonObject.getJSONArray("locations");
                    for(int i=0;i<locations.length();i++)
                    {
                        JSONObject divisionResponse = locations.getJSONObject(i);
                        final Division division = new Division(divisionResponse.getString("division_id"),divisionResponse.getString("division_name"));
                        divisionList.add(division);
                        JSONArray districts = divisionResponse.getJSONArray("districts");
                        for(int j =0;j<districts.length();j++)
                        {
                            JSONObject districtResponse = districts.getJSONObject(j);
                            final com.avijit.rms.data.local.entities.District district ;
                            district = new District(districtResponse.getString("district_id"),divisionResponse.getString("division_id"),districtResponse.getString("district_name"));
                            districtList.add(district);
                            JSONArray areas = districtResponse.getJSONArray("areas");
                            for(int k=0;k<areas.length();k++)
                            {
                                JSONObject areaResponse = areas.getJSONObject(k);
                                final Area area = new Area(district.districtId,areaResponse.getString("area_id"),areaResponse.getString("area"),areaResponse.getString("area_type"));
                                areaList.add(area);
                            }
                        }
                    }
                    final Division[] divisions = new Division[divisionList.size()];
                    final District[] districts = new District[districtList.size()];
                    final Area[] areas = new Area[areaList.size()];
                    for(int i=0;i<divisionList.size();i++)
                    {
                        divisions[i] = divisionList.get(i);
                    }
                    for(int i=0;i<districtList.size();i++)
                    {
                        districts[i] = districtList.get(i);
                    }
                    for(int i=0;i<areaList.size();i++)
                    {
                        areas[i] = areaList.get(i);
                    }
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            AppDatabase db = AppDatabase.getInstance(MainActivity.this);
                            db.divisionDao().deleteAll();
                            db.districtDao().deleteAll();
                            db.areaDao().deleteAll();

                            db.divisionDao().insertAll(divisions);
                            db.districtDao().insertAll(districts);
                            db.areaDao().insert(areas);
                        }
                    });

                }catch (Exception e){
                    Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }
            }
        },new AppUtils(MainActivity.this).errorListener);
        stringRequest.setRetryPolicy(AppUtils.STRING_REQUEST_RETRY_POLICY);
        queue.add(stringRequest);
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
                //System.exit(0);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setMessage("Are you sure to exit?");

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);

        return (result == PackageManager.PERMISSION_GRANTED) && (result1 == PackageManager.PERMISSION_GRANTED) && (result2==PackageManager.PERMISSION_GRANTED );
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION, CAMERA}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean fineLocationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean coraseLocationAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (fineLocationAccepted && coraseLocationAccepted && cameraAccepted)
                    {

                    }
                        //Snackbar.make(view, "Permission Granted, Now you can access location data and camera.", Snackbar.LENGTH_LONG).show();
                    else {

                       // Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)
                                || shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION)
                                    || shouldShowRequestPermissionRationale(CAMERA)
                            ) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                requestPermission();
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }
                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .create()
                .show();
    }

}
