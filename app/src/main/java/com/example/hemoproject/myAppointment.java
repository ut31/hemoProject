package com.example.hemoproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class myAppointment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_appointment);

        final TextView hospital = findViewById(R.id.hospital);
        final TextView date = findViewById(R.id.date);
        final TextView time = findViewById(R.id.time);
        final Button delete = (Button) findViewById(R.id.delete);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Query appointments for the current user, sorted by date in descending order, and limit to one document
            db.collection("appointments")
                    .whereEqualTo("userId", userId)
                    .orderBy("date", Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                            // Get the last appointment document
                            QuerySnapshot querySnapshot = task.getResult();
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);

                            String hospitalName = document.getString("hospital");
                            Date appointmentDate = document.getDate("date");
                            String appointmentTime = document.getString("time");

                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                            String formattedDate = dateFormat.format(appointmentDate);

                            hospital.setText(hospitalName);
                            date.setText(formattedDate);
                            time.setText(appointmentTime);
                        } else {
                            Log.e("MyAppointment", "Error getting appointments", task.getException());
                        }
                    });
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // Query appointments for the current user, sorted by date in descending order, and limit to one document
                    db.collection("appointments")
                            .whereEqualTo("userId", userId)
                            .orderBy("date", Query.Direction.DESCENDING)
                            .limit(1)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                                    // Get the last appointment document
                                    QuerySnapshot querySnapshot = task.getResult();
                                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);

                                    // Delete the last appointment document
                                    db.collection("appointments").document(document.getId())
                                            .delete()
                                            .addOnSuccessListener(aVoid -> {
                                                // Appointment deleted successfully
                                                Log.d("MyAppointment", "Last appointment deleted successfully");
                                                // Update the UI to reflect the deletion
                                                hospital.setText("");
                                                date.setText("");
                                                time.setText("");
                                            })
                                            .addOnFailureListener(e -> {
                                                // Appointment deletion failed
                                                Log.e("MyAppointment", "Error deleting last appointment", e);
                                            });
                                } else {
                                    Log.e("MyAppointment", "Error getting appointments", task.getException());
                                }
                            });
                }



                startActivity(new Intent(myAppointment.this, donorHome.class));
            }
        });


    }
}
