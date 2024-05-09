package com.example.hemoproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class updateProfileUser extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        final EditText name = findViewById(R.id.name);
        final EditText area = findViewById(R.id.area);
        final EditText phone = findViewById(R.id.phone);
        final Button save = findViewById(R.id.Save);
        final TextView blood = findViewById(R.id.bloodout);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Retrieve user data from Firestore
            db.collection("users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Set user data to the respective fields
                            name.setText(documentSnapshot.getString("name"));
                            String government = documentSnapshot.getString("government");
                            area.setText(government != null ? government : "");
                            phone.setText(documentSnapshot.getString("phone"));
                            blood.setText(documentSnapshot.getString("BloodType"));
                        }
                    })

                    .addOnFailureListener(e -> {
                        Toast.makeText(updateProfileUser.this, "Failed to retrieve profile", Toast.LENGTH_SHORT).show();
                    });


            save.setOnClickListener(v -> {
                String newName = name.getText().toString().trim();
                String newArea = area.getText().toString().trim();
                String newPhone = phone.getText().toString().trim();
                String newBlood = blood.getText().toString().trim();

                // Update the user document in Firestore
                db.collection("users").document(userId)
                        .update("name", newName, "area", newArea, "phone", newPhone, "bloodType", newBlood)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(updateProfileUser.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(updateProfileUser.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        });
            });

        }
    }
}
