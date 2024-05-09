package com.example.hemoproject;

import android.os.Bundle;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class allDonors extends AppCompatActivity {

    private LinearLayout donorsContainer;
    private List<Donor> donorsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_donors);

        donorsContainer = findViewById(R.id.donorsContainer);
        donorsList = new ArrayList<>(); // Initialize the list

        // Retrieve donors from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("role", "donor") // Filter for users with role "donor"
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String name = document.getString("name");
                            String email = document.getString("email");
                            String government = document.getString("government");
                            String bloodType = document.getString("BloodType");
                            String phone = document.getString("phone"); // Add this line to retrieve the phone number
                            donorsList.add(new Donor(name, email, government, bloodType, phone));
                        }
                        // Add TextViews for each donor
                        for (Donor donor : donorsList) {
                            addDonorTextView(donor);
                        }
                    } else {
                        // Handle failure to retrieve donors
                    }
                });
    }

    private void addDonorTextView(Donor donor) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, dpToPx(8), 0, 0); // Add margin between text views
        textView.setLayoutParams(params);

        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        textView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray)); // Use your color resource for dark red

        String donorDetails = "Name: " + donor.getName() + "\n" +
                "Email: " + donor.getEmail() + "\n" +
                "Government: " + donor.getGovernment() + "\n" +
                "Blood Type: " + donor.getBloodType() + "\n" +
                "Phone: " + donor.getPhone(); // Include the phone number

        textView.setText(donorDetails);

        donorsContainer.addView(textView);

        // Add a space between text views
        Space space = new Space(this);
        LinearLayout.LayoutParams spaceParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dpToPx(8)); // Adjust the space height as needed
        space.setLayoutParams(spaceParams);
        donorsContainer.addView(space);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}
