package com.example.hemoproject;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class usersAppointments extends AppCompatActivity {

    private FirebaseFirestore db;
    private LinearLayout appointmentsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usersappointments);

        db = FirebaseFirestore.getInstance();
        appointmentsContainer = findViewById(R.id.appointmentsContainer);

        // Map to store the last appointment for each user
        Map<String, QueryDocumentSnapshot> lastAppointments = new HashMap<>();

        // Query appointments for all users
        db.collection("appointments")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String userId = document.getString("userId");
                            String hospital = document.getString("hospital");
                            Date date = document.getDate("date");

                            if (userId != null && hospital != null && date != null) {
                                // Check if this appointment is the last one for the user
                                if (!lastAppointments.containsKey(userId)) {
                                    lastAppointments.put(userId, document);
                                }
                            }
                        }

                        // Display the last appointment for each user
                        for (QueryDocumentSnapshot lastAppointment : lastAppointments.values()) {
                            String userId = lastAppointment.getString("userId");
                            String hospital = lastAppointment.getString("hospital");
                            Date date = lastAppointment.getDate("date");

                            // Get user details
                            db.collection("users").document(userId).get()
                                    .addOnSuccessListener(userDocument -> {
                                        if (userDocument.exists()) {
                                            String userName = userDocument.getString("name");
                                            String userEmail = userDocument.getString("email");

                                            // Add user details and appointment to the layout
                                            addUserAppointmentTextView(userName, userEmail, hospital, date);
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("usersAppointments", "Failed to retrieve user details", e);
                                    });
                        }
                    } else {
                        Log.e("usersAppointments", "Error getting appointments", task.getException());
                    }
                });
    }


    private void addUserAppointmentTextView(String userName, String userEmail, String hospital, Date date) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, dpToPx(8), 0, 0); // Add margin between text views
        textView.setLayoutParams(params);

        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        textView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray)); // Use your color resource for dark red

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        String formattedDate = dateFormat.format(date);

        String userDetails = "Name: " + userName + "\n" +
                "Email: " + userEmail + "\n" +
                "Hospital: " + hospital + "\n" +
                "Date: " + formattedDate;

        textView.setText(userDetails);

        appointmentsContainer.addView(textView);

        // Add a space between text views
        Space space = new Space(this);
        LinearLayout.LayoutParams spaceParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dpToPx(8)); // Adjust the space height as needed
        space.setLayoutParams(spaceParams);
        appointmentsContainer.addView(space);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}
