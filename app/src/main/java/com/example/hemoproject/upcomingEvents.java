package com.example.hemoproject;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class upcomingEvents extends AppCompatActivity {
    private LinearLayout eventsContainer;
    private List<String> eventsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming_events);

        eventsContainer = findViewById(R.id.appointmentsContainer);

        // Retrieve events from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String eventName = document.getString("name");
                            String eventDate = document.getString("date");
                            String eventTime = document.getString("time");
                            String eventLocation = document.getString("location");

                            // Check if the event date is after the current date
                            if (isFutureDate(eventDate)) {
                                String eventText = eventName + " - " + eventDate + " - " + eventTime + "\n" + "Location: " + eventLocation;
                                addEventTextView(eventText);
                            }
                        }
                    } else {
                        // Handle failure to retrieve events
                        Toast.makeText(getApplicationContext(), "Failed to retrieve events", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean isFutureDate(String eventDate) {
        // Implement logic to check if eventDate is after the current date
        // For simplicity, let's assume eventDate is in the format "dd/MM/yyyy"
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();
        Date date;
        try {
            date = sdf.parse(eventDate);
            return date.after(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void addEventTextView(String eventText) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, dpToPx(8), 0, 0);
        textView.setLayoutParams(params);
        textView.setText(eventText);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        textView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
        textView.setGravity(Gravity.CENTER_VERTICAL);
        eventsContainer.addView(textView);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
