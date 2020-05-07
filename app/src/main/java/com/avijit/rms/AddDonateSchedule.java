package com.avijit.rms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.avijit.rms.data.local.AppDatabase;
import com.avijit.rms.data.local.entities.Area;
import com.avijit.rms.data.local.entities.District;
import com.avijit.rms.data.local.entities.Division;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.os.AsyncTask.execute;

public class AddDonateSchedule extends AppCompatActivity {
    DatePickerDialog datePickerDialog;
    TextView dateEditText;
    Spinner divisionSpinner,districtSpinner,typeSpinner,areaSpinner;
    EditText addressEditText;
    TextView nextButton;

    private List<Division> divisionList =new ArrayList<>();
    private List<District> districtList =new ArrayList<>();
    private List<Area> areaIdList =new ArrayList<>();
    /**
     * @param savedInstanceState previously saved bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_donate_schedule);
        dateEditText = findViewById(R.id.date_picker);
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
                                dateEditText.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
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

    }
    private void setDistricts()
    {
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
                        districts[i]=districtList.get(i).name;
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
}
