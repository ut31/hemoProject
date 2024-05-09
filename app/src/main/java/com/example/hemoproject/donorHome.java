package com.example.hemoproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class donorHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor_home);

        final Button update = (Button) findViewById(R.id.alert);
        final Button events = (Button) findViewById(R.id.Events);
        final Button book = (Button) findViewById(R.id.createEvent);
        final Button appoint = (Button) findViewById(R.id.donors);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(donorHome.this, updateProfileUser.class));
            }
        });


        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(donorHome.this, upcomingEvents.class));
            }
        });



        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(donorHome.this, bookAppointment.class));
            }
        });



        appoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(donorHome.this, myAppointment.class));
            }
        });







    }

}