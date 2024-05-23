package com.example.rescuemeeee;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class user_register extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword, editTextName, editTextMobileNumber;
    private Button buttonRegister;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");

        // Initialize views
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextName = findViewById(R.id.editTextName);
        editTextMobileNumber = findViewById(R.id.editTextMobileNumber);
        buttonRegister = findViewById(R.id.buttonRegister);

        // Set click listener for the Register button
        buttonRegister.setOnClickListener(v -> registerUser());
    }

    // Method to register user
    private void registerUser() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String mobileNumber = editTextMobileNumber.getText().toString().trim();

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
        usersRef.child(username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    // Username already exists
                    Toast.makeText(user_register.this, "Username already exists", Toast.LENGTH_SHORT).show();
                } else {
                    // Save user data to Firebase Realtime Database
                    User user = new User(username, password, name, mobileNumber);
                    usersRef.child(username).setValue(user)
                            .addOnSuccessListener(aVoid -> {
                                // Show success message
                                Toast.makeText(user_register.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                // Clear input fields
                                editTextUsername.setText("");
                                editTextPassword.setText("");
                                editTextName.setText("");
                                editTextMobileNumber.setText("");
                            })
                            .addOnFailureListener(e -> {
                                // Show failure message
                                Toast.makeText(user_register.this, "Failed to register user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            } else {
                // Error getting username
                Toast.makeText(user_register.this, "Error checking username: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
