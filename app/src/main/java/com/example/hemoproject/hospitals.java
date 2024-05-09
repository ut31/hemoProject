package com.example.hemoproject;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class hospitals extends AppCompatActivity {
    private LinearLayout hospitalsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospitals);

        hospitalsContainer = findViewById(R.id.hospitals);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("role", "manager") // Filter users with role "manager"
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String hospitalName = document.getString("hospital");
                            String managerName = document.getString("name"); // Assuming manager name is stored in the "name" field
                            String email = document.getString("email");
                            String phone = document.getString("phone"); // Assuming phone number is stored in the "phone" field
                            addHospitalTextView(hospitalName, managerName, email, phone);
                        }
                    } else {
                        // Handle failure to retrieve hospitals
                    }
                });
    }

    private void addHospitalTextView(String hospitalName, String managerName, String email, String phone) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, dpToPx(8), 0, dpToPx(8)); // Add margin between text views
        textView.setLayoutParams(layoutParams);

        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        textView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray)); // Use your color resource for dark red
        textView.setGravity(Gravity.CENTER); // Center the text horizontally

        String hospitalDetails = "Hospital Name: " + hospitalName + "\n" +
                "Manager: " + managerName + "\n" +
                "Email: " + email + "\n" +
                "Phone: " + phone;

        textView.setText(hospitalDetails);

        hospitalsContainer.addView(textView);

        // Add a space between text views
        Space space = new Space(this);
        LinearLayout.LayoutParams spaceParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dpToPx(8)); // Adjust the space height as needed
        space.setLayoutParams(spaceParams);
        hospitalsContainer.addView(space);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}
