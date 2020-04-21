package com.avijit.rms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button wantToDonateButton,needHelpButton;

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
        needHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(getApplicationContext(),FamilyAddress.class);
                startActivity(intent);
            }
        });

    }
}
