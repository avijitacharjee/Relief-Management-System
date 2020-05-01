package com.avijit.rms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class VolunteerSignUp extends AppCompatActivity {
    Button loginIntentButton,goButton;
    ImageView logoImage;
    TextInputLayout tran2,tran3;
    Spinner typeSpinner;
    String types[] = new String[]{"As Individual","As Thana User","As Company"};
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

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(VolunteerSignUp.this,MainActivity.class));
        /*finish();
        overridePendingTransition(0,0);*/
    }
}
