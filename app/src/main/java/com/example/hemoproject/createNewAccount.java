package com.example.hemoproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class createNewAccount extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_accounta);

        firebaseAuth = FirebaseAuth.getInstance();

        final TextView log = findViewById(R.id.loga);
        final EditText nameInput = findViewById(R.id.nameTxt);
        final EditText phoneInput = findViewById(R.id.phoneTxt);
        final EditText idInput = findViewById(R.id.Email);
        final EditText passInput1 = findViewById(R.id.passwordTxt);
        final EditText passInput2 = findViewById(R.id.rePasswordTxt);
        final EditText dateOfBirthInput = findViewById(R.id.birthDateTxt);
        final EditText hospitalInput = findViewById(R.id.hospitalNameTxt);
        final RadioButton mng = findViewById(R.id.managerRbtn);
        final RadioButton donor = findViewById(R.id.donorRbtn);
        final AutoCompleteTextView autoCompleteTextView = findViewById(R.id.cities);
        final Button createAccount = findViewById(R.id.createAccountBtn);

        String Name;

        mng.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                hospitalInput.setHint("Hospital");
            } else {
                hospitalInput.setHint("Blood");
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.city_names));
        autoCompleteTextView.setAdapter(adapter);

        createAccount.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String government = autoCompleteTextView.getText().toString().trim();
            String phone = phoneInput.getText().toString().trim();
            String email = idInput.getText().toString().trim();
            String password = passInput1.getText().toString().trim();
            String birthDate = dateOfBirthInput.getText().toString().trim();
            String hospitalOrBloodType = donor.isChecked() ? hospitalInput.getText().toString().trim() : hospitalInput.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || birthDate.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please fill in all the required fields", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(passInput2.getText().toString().trim())) {
                Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else if (donor.isChecked() && !isValidBloodType(hospitalOrBloodType)) {
                Toast.makeText(getApplicationContext(), "Invalid blood type", Toast.LENGTH_SHORT).show();
            } else {
                // Check if email contains "@medicalstaff"
                boolean isStaff = email.contains("@medicalstaff");
                if (isStaff || !mng.isChecked()) {
                    // Register user with Firebase
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    if (user != null) {
                                        // Add user to Firestore with role and hospital/blood type
                                        Map<String, Object> userData = new HashMap<>();
                                        userData.put("email", email);
                                        userData.put("government", government);
                                        userData.put("role", mng.isChecked() ? "manager" : "donor"); // Set role based on selection
                                        userData.put("name", name);
                                        userData.put("phone", phone);
                                        userData.put("birthDate", birthDate);
                                        if (mng.isChecked()) {
                                            userData.put("hospital", hospitalOrBloodType.toUpperCase());
                                        } else {
                                            // Set hospital to empty string for manager accounts
                                            userData.put("BloodType", hospitalOrBloodType.toUpperCase());
                                        }
                                        // Add user data to Firestore
                                        FirebaseFirestore.getInstance().collection("users")
                                                .document(user.getUid())
                                                .set(userData)
                                                .addOnSuccessListener(aVoid -> {
                                                    // Registration successful, navigate to appropriate activity
                                                    if (!mng.isChecked()) {
                                                        startActivity(new Intent(createNewAccount.this, donorHome.class));
                                                    } else {
                                                        startActivity(new Intent(createNewAccount.this, managerHome.class));
                                                    }
                                                    finish();
                                                })
                                                .addOnFailureListener(e -> {
                                                    // Handle failure to add user data to Firestore
                                                    Toast.makeText(getApplicationContext(), "Failed to add user data to Firestore", Toast.LENGTH_SHORT).show();
                                                });
                                    } else {
                                        Toast.makeText(getApplicationContext(), "User is null", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), "Only staff members can create manager accounts", Toast.LENGTH_SHORT).show();
                }
            }
        });

        log.setOnClickListener(v -> startActivity(new Intent(createNewAccount.this, signin.class)));
    }

    private boolean isValidBloodType(String bloodType) {
        // Regular expression pattern for case-insensitive blood type validation
        String bloodTypePattern = "^(?i)(A|B|AB|O)[+-]$";
        // Check if the provided blood type matches the pattern
        return bloodType.matches(bloodTypePattern);

    }
}