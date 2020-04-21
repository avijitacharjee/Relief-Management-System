package com.avijit.rms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class FamilyAddress extends AppCompatActivity {
    Button nextButton;

    Spinner divisionSpinner,districtSpinner,typeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_address);

        divisionSpinner= findViewById(R.id.division_spinner);
        districtSpinner = findViewById(R.id.district_spinner);
        typeSpinner = findViewById(R.id.type_spinner);

        nextButton = findViewById(R.id.next_button);

        String[] divisions = {"--Select division--", "Chittagong","Dhaka","Rangpur"};
        String[] districts = {"--Select district--", "Chittagong","Comilla","Rangamati"};
        String[] types = {"--Select type--", "A","B" };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,divisions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        divisionSpinner.setAdapter(adapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,districts);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,types);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter2);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),FamilyRegistration.class));
            }
        });
    }
}
