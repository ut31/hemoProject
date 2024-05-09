package com.example.hemoproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

@AndroidEntryPoint
public class signin extends AppCompatActivity {



    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        firebaseAuth = FirebaseAuth.getInstance();

        final EditText userId = findViewById(R.id.userId);
        final EditText userPass = findViewById(R.id.passInput);
        final Button login = findViewById(R.id.logIn);
        final Button createAccount = findViewById(R.id.createAccount);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = userId.getText().toString().trim();
                String password = userPass.getText().toString().trim();

                // Authenticate user with Firebase
                firebaseAuth.signInWithEmailAndPassword(id, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if (user != null) {
                                    String userId = user.getUid();
                                    checkUserRoleAndRedirect(userId);
                                }
                            } else {
                                Toast.makeText(signin.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open create account activity
                startActivity(new Intent(signin.this, createNewAccount.class));
            }
        });
    }
    private void isDonor(String userId, Callback<Boolean> callback) {
        FirebaseFirestore.getInstance().collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String role = documentSnapshot.getString("role");
                        callback.onResult("donor".equals(role));
                    } else {
                        callback.onResult(false);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure to retrieve document
                    callback.onResult(false);
                });
    }

    private void isManager(String userId, Callback<Boolean> callback) {
        FirebaseFirestore.getInstance().collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String role = documentSnapshot.getString("role");
                        callback.onResult("manager".equals(role));
                    } else {
                        callback.onResult(false);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure to retrieve document
                    callback.onResult(false);
                });
    }

    interface Callback<T> {
        void onResult(T result);
    }

    private void checkUserRoleAndRedirect(String userId) {
        isDonor(userId, isDonor -> {
            if (isDonor) {
                startActivity(new Intent(signin.this, donorHome.class));
                finish();
            } else {
                isManager(userId, isManager -> {
                    if (isManager) {
                        startActivity(new Intent(signin.this, managerHome.class));
                        finish();
                    } else {
                        // Handle other user roles or show an error message
                        Toast.makeText(signin.this, "User role not recognized", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
