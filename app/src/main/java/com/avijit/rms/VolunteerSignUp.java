package com.avijit.rms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputLayout;

public class VolunteerSignUp extends AppCompatActivity {
    Button loginIntentButton,goButton;
    ImageView logoImage;
    TextInputLayout tran2,tran3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_sign_up);
        loginIntentButton = findViewById(R.id.signup_intent_button);
        goButton = findViewById(R.id.go);
        logoImage = findViewById(R.id.logo_image);
        tran2 = findViewById(R.id.username);
        tran3 = findViewById(R.id.password);
        loginIntentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),VolunteerLogIn.class);
                Pair[] pairs = new Pair[5];
                pairs[0]= new Pair<View,String>(loginIntentButton,"tran0");
                pairs[1]= new Pair<View,String>(logoImage,"tran1");
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
}
