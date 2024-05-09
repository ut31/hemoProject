package com.example.hemoproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class managerHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_home);

        final Button alert = (Button) findViewById(R.id.alert);
        final Button Events = (Button) findViewById(R.id.Events);
        final Button createEvent = (Button) findViewById(R.id.createEvent);
        final Button donors = (Button) findViewById(R.id.donors);
        final Button Hospitals = (Button) findViewById(R.id.Hospitals);
        final Button Statistic = (Button) findViewById(R.id.Statistic);
        final Button donorsAppointments = (Button) findViewById(R.id.donorsAppointments);

        Events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // open create account activity
                startActivity(new Intent(managerHome.this, upcomingEvents.class));

            }
        });



        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // open create account activity
                startActivity(new Intent(managerHome.this, createEvent.class));

            }
        });




        donors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // open create account activity
                startActivity(new Intent(managerHome.this, allDonors.class));

            }
        });



        alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // open create account activity
                startActivity(new Intent(managerHome.this, createNewAccount.class));

            }
        });

        Hospitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // open create account activity
                startActivity(new Intent(managerHome.this, hospitals.class));

            }
        });



        Statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // open create account activity
                startActivity(new Intent(managerHome.this, Statistic.class));

            }
        });


        donorsAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // open create account activity
                startActivity(new Intent(managerHome.this, usersAppointments.class));

            }
        });




    }
}
