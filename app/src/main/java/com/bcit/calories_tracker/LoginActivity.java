package com.bcit.calories_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d("DEBUG/LoginActivity", "Login Already");
            finish();
        } else {
            Log.d("DEBUG/LoginActivity", "Not Login");
            setContentView(R.layout.activity_login);
        }
    }

    public void onClickSignIn(View view) {
        EditText emailEditText = findViewById(R.id.email_login_value);
        EditText passwordEditText = findViewById(R.id.password_login_value);
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Do nothing if email or password empty
        if (email.trim().equals("") || password.trim().equals("")) {
            return;
        }

        // Sign In using email and password
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("DEBUG/LoginActivity", "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("DEBUG/LoginActivity", "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    /**
     * Sign UP Firebase
     mAuth.createUserWithEmailAndPassword("test@test.com", "12345678")
     .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
    if (task.isSuccessful()) {
    // Sign in success, update UI with the signed-in user's information
    Log.d("DEBUG/LoginActivity", "createUserWithEmail:success");
    FirebaseUser user = mAuth.getCurrentUser();
    Log.d("DEBUG/LoginActivity", user.getEmail());
    //                        updateUI(user);
    } else {
    // If sign in fails, display a message to the user.
    Log.d("DEBUG/LoginActivity", "createUserWithEmail:failure", task.getException());
    Toast.makeText(LoginActivity.this, "Authentication failed.",
    Toast.LENGTH_SHORT).show();
    //                        updateUI(null);
    }
    }
    });
     */
}