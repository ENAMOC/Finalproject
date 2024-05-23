package com.example.rescuemeeee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class admin_register extends AppCompatActivity {

    private EditText editTextAdminUsername, editTextAdminPassword, editTextAdminName, editTextAdminMobileNumber;
    private Button buttonAdminRegister;
    private DatabaseReference adminRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        adminRef = database.getReference("admins");

        // Initialize views
        editTextAdminUsername = findViewById(R.id.editTextAdminUsername);
        editTextAdminPassword = findViewById(R.id.editTextAdminPassword);
        editTextAdminName = findViewById(R.id.editTextAdminName);
        editTextAdminMobileNumber = findViewById(R.id.editTextAdminMobileNumber);
        buttonAdminRegister = findViewById(R.id.buttonAdminRegister);

        // Set click listener for the Register button
        buttonAdminRegister.setOnClickListener(v -> registerUser());
    }

    // Method to register user
    private void registerUser() {
        String username =  editTextAdminUsername.getText().toString().trim();
        String password = editTextAdminPassword.getText().toString().trim();
        String name = editTextAdminName.getText().toString().trim();
        String mobileNumber = editTextAdminMobileNumber.getText().toString().trim();

        // Validate user input
        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || mobileNumber.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if password meets minimum length requirement
        if (password.length() < 8) {
            Toast.makeText(this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if mobile number is exactly 11 digits
        if (mobileNumber.length() != 11) {
            Toast.makeText(this, "Mobile number must be 11 digits long", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if username already exists
       adminRef.child(username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    // Username already exists
                    Toast.makeText(admin_register.this, "Username already exists", Toast.LENGTH_SHORT).show();
                } else {
                    // Save admin data to Firebase Realtime Database
                    User user = new User(username, password, name, mobileNumber);
                    adminRef.child(username).setValue(user)
                            .addOnSuccessListener(aVoid -> {
                                // Show success message
                                Toast.makeText(admin_register.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                // Clear input fields
                                editTextAdminUsername.setText("");
                                editTextAdminPassword.setText("");
                                editTextAdminName.setText("");
                                editTextAdminMobileNumber.setText("");
                            })
                            .addOnFailureListener(e -> {
                                // Show failure message
                                Toast.makeText(admin_register.this, "Failed to register user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            } else {
                // Error getting username
                Toast.makeText(admin_register.this, "Error checking username: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
