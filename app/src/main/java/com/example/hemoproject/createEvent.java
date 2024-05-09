package com.example.hemoproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
public class createEvent extends AppCompatActivity {
    String selectedDate;
    String name;
    String location;
    String time;
    String date;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        db = FirebaseFirestore.getInstance();

        final EditText eventName = findViewById(R.id.eventName);
        final EditText Location = findViewById(R.id.Location);
        final EditText Time = findViewById(R.id.time);
        final Button createBtn = findViewById(R.id.createBtn);
        final CalendarView calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Save the selected date as a string
                selectedDate = String.valueOf(dayOfMonth) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(year);
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = eventName.getText().toString().trim();
                location = Location.getText().toString().trim();
                time = Time.getText().toString().trim();
                date = selectedDate;

                // Validate time format (HH:mm)
                if (!isValidTimeFormat(time)) {
                    Toast.makeText(getApplicationContext(), "Please enter the time in 24-hour format (HH:mm)", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a new event object
                Map<String, Object> event = new HashMap<>();
                event.put("name", name);
                event.put("location", location);
                event.put("time", time);
                event.put("date", date);

                // Add the event to Firestore
                db.collection("events")
                        .add(event)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(getApplicationContext(), "Event created successfully", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getApplicationContext(), "Failed to create event", Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }

    private static boolean isValidTimeFormat(String time) {
        // Check if the time is in the format HH:mm
        return time.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]");
    }
}
