package com.example.hemoproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class bookAppointment extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText hospitalEditText;
    private EditText timeEditText;
    private Button bookButton;
    private CalendarView calendarView;
    String selectedDate;
    String hospital;
    String location;
    String time;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_appointment);

        // Initialize components
        hospitalEditText = findViewById(R.id.Hospital);
        timeEditText = findViewById(R.id.Time);
        bookButton = findViewById(R.id.button);
        calendarView = findViewById(R.id.calendarView);

        db = FirebaseFirestore.getInstance();

        // Set on-date-change listener for calendar
        calendarView.setOnDateChangeListener((view, year, monthOfYear, dayOfMonth) -> {
            selectedDate(year, monthOfYear, dayOfMonth);
        });

        // Set on-click listener for book button
        bookButton.setOnClickListener(v -> bookAppointment());
    }

    private void selectedDate(int year, int monthOfYear, int dayOfMonth) {
        // Format date string (dd/MM/yyyy)
        date = String.format(Locale.US, "%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year);
    }

    private boolean isValidTimeFormat(String time) {
        // Validate time format as "HH:mm" (24-hour format)
        String regex = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
        return time.matches(regex);
    }

    private void bookAppointment() {
        hospital = hospitalEditText.getText().toString().trim();
        time = timeEditText.getText().toString().trim();

        if (hospital.isEmpty() || time.isEmpty()) {
            // Display a message if any of the required fields are empty
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidTimeFormat(time)) {
            // Display a message for invalid time format
            Toast.makeText(this, "Please enter the time in 24-hour format (HH:mm)", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                Date parsedDate = dateFormat.parse(date);
                Timestamp timestamp = new Timestamp(parsedDate);

                Map<String, Object> appointment = new HashMap<>();
                appointment.put("userId", userId); // Reference to the user document
                appointment.put("hospital", hospital);
                appointment.put("time", time);
                appointment.put("date", timestamp);

                db.collection("appointments")
                        .add(appointment)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(this, "Appointment booked successfully", Toast.LENGTH_SHORT).show();
                            clearForm();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to book appointment", Toast.LENGTH_SHORT).show();
                        });
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to parse date", Toast.LENGTH_SHORT).show();
            }
        } else {
            // No user is currently signed in
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearForm() {
        hospitalEditText.setText("");
        timeEditText.setText("");
    }
}
