package com.example.rescuemeeee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_test extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");

        // Initialize views
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        // Set click listener for the Login button
        buttonLogin.setOnClickListener(v -> loginUser());

        // Find the "Create Account" TextView
        TextView textViewCreateAccount = findViewById(R.id.textViewCreateAccount);

        // Set OnClickListener for "Create Account" TextView
        textViewCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });

        // Find the "Switch to admin" TextView
        TextView textViewSwitchToAdmin = findViewById(R.id.textViewSwitchToAdmin);

        // Set OnClickListener for "Switch to admin" TextView
        textViewSwitchToAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click for "Switch to admin" text view
                switchToAdminActivity();
            }
        });
    }

    // Method to open UserRegisterActivity when "Create Account" text view is clicked
    private void openRegisterActivity() {
        Intent intent = new Intent(activity_test.this, user_register.class);
        startActivity(intent);
    }

    // Method to login user
    private void loginUser() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Validate user input
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in both username and password fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if username and password match an entry in the database
        usersRef.child(username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    // Username exists, check password
                    String storedPassword = task.getResult().child("password").getValue(String.class);
                    if (password.equals(storedPassword)) {
                        // Username and password match
                        Toast.makeText(activity_test.this, "User Login Successful", Toast.LENGTH_SHORT).show();
                        // Proceed to user welcome activity
                        Intent intent = new Intent(activity_test.this, users_welcome.class);
                        startActivity(intent);
                    } else {
                        // Incorrect password
                        Toast.makeText(activity_test.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Username doesn't exist
                    Toast.makeText(activity_test.this, "Username not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Error retrieving data
                Toast.makeText(activity_test.this, "Error checking credentials: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to switch to AdminLogin activity
    private void switchToAdminActivity() {
        Intent intent = new Intent(activity_test.this, admin_login.class);
        startActivity(intent);
    }
}
