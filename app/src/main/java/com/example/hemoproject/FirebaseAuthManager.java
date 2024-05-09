package com.example.hemoproject;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthManager {

    private FirebaseAuth auth;

    public FirebaseAuthManager() {
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
    }

    // Additional methods can be added here to interact with FirebaseAuth
}
