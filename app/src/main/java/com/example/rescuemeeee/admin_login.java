package com.example.rescuemeeee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class admin_login extends AppCompatActivity {

    private EditText editTextAdminUsername, editTextAdminPassword;
    private Button buttonAdminLogin;
    private TextView textViewCreateAdminAccount;

    // Firebase Realtime Database
    private DatabaseReference adminsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        // Initialize Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        adminsRef = database.getReference("admins");

        // Initialize views
        editTextAdminUsername = findViewById(R.id.editTextAdminUsername);
        editTextAdminPassword = findViewById(R.id.editTextAdminPassword);
        buttonAdminLogin = findViewById(R.id.buttonAdminLogin);
        textViewCreateAdminAccount = findViewById(R.id.textViewCreateAdminAccount); // Added

        // Set click listener for the Admin Login button
        buttonAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Admin Login button click
                adminLogin();
            }
        });

        // Set OnClickListener for "Create Admin Account" TextView
        textViewCreateAdminAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click for "Create Admin Account" text view
                createAdminAccount();
            }
        });

        // Find the "Switch to User Login" TextView
        TextView textViewSwitchToUserLogin = findViewById(R.id.textViewSwitchToUserLogin);

        // Set OnClickListener for "Switch to User Login" TextView
        textViewSwitchToUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click for "Switch to User Login" text view
                switchToUserLogin();
            }
        });
    }

    // Method to handle Admin Login
    private void adminLogin() {
        String adminUsername = editTextAdminUsername.getText().toString().trim();
        String adminPassword = editTextAdminPassword.getText().toString().trim();

        // Validate admin input
        if (adminUsername.isEmpty() || adminPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in both admin username and password fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if username and password match an entry in the "admins" record in the database
        adminsRef.child(adminUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Admin username exists
                    String storedPassword = dataSnapshot.child("password").getValue(String.class);
                    if (adminPassword.equals(storedPassword)) {
                        // Username and password match
                        Toast.makeText(admin_login.this, "Admin Login Successful", Toast.LENGTH_SHORT).show();
                        // Proceed to admin welcome activity
                        Intent intent = new Intent(admin_login.this, admin_welcome.class);
                        startActivity(intent);
                        finish(); // Finish the current activity
                    } else {
                        // Incorrect password
                        Toast.makeText(admin_login.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Admin username doesn't exist
                    Toast.makeText(admin_login.this, "Admin username not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Error handling
                Toast.makeText(admin_login.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to switch to User Login activity
    private void switchToUserLogin() {
        Intent intent = new Intent(admin_login.this, activity_test.class);
        startActivity(intent);
        finish(); // Finish the current activity to prevent going back to admin login
    }

    // Method to handle "Create Admin Account" action
    private void createAdminAccount() {
        Intent intent = new Intent(admin_login.this, admin_register.class);
        startActivity(intent);
    }
}
