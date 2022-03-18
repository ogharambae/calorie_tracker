package com.bcit.calories_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mode = "Login";

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d("DEBUG/LoginActivity", "Login Already");
            finish();
        } else {
            Log.d("DEBUG/LoginActivity", "Not Login");
            setContentView(R.layout.activity_login);
            updateLoginSignupUI();
        }
    }

    // Switch login/sign up UI
    public void updateLoginSignupUI() {
        EditText cfPasswordEditText = findViewById(R.id.cfpassword_login_value);
        TextView cfPasswordTextView = findViewById(R.id.cfpassword_login_label);
        TextView navigateTextView = findViewById(R.id.navigate_login_text);
        Button button = findViewById(R.id.button_login);

        if (mode.equals("Login")) {
            cfPasswordEditText.setVisibility(View.GONE);
            cfPasswordTextView.setVisibility(View.GONE);
            navigateTextView.setText(R.string.login_singup_navigate_text);
            button.setText(R.string.login_login_button);
        } else {
            cfPasswordEditText.setVisibility(View.VISIBLE);
            cfPasswordTextView.setVisibility(View.VISIBLE);
            navigateTextView.setText(R.string.login_login_navigate_text);
            button.setText(R.string.login_signup_button);
        }
    }

    public void onClickSwitchLoginSignUp(View view) {
        if (mode.equals("Login")) {
            mode = "SignUp";
        } else {
            mode = "Login";
        }
        this.updateLoginSignupUI();
    }

    public void onClickButton(View view) {
        if (mode.equals("Login")) {
            this.onClickSignIn(view);
        } else {
            this.onClickSignUp(view);
        }
    }

    public void onClickSignIn(View view) {
        EditText emailEditText = findViewById(R.id.email_login_value);
        EditText passwordEditText = findViewById(R.id.password_login_value);
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Do nothing if email or password empty
        if (email.trim().equals("") || password.trim().equals("")) {
            Toast.makeText(LoginActivity.this,
                    "Please fill in all the fields!",
                    Toast.LENGTH_SHORT).show();
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
                            finish();
                        } else {
                            try {
                                throw task.getException();
                            } catch (Exception e) {
                                String message = e.getMessage() + ". Please try again!";
                                Toast.makeText(LoginActivity.this, message,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void onClickSignUp(View view) {
        EditText emailEditText = findViewById(R.id.email_login_value);
        EditText passwordEditText = findViewById(R.id.password_login_value);
        EditText cfPasswordEditText = findViewById(R.id.cfpassword_login_value);
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String cfPassword = cfPasswordEditText.getText().toString();

        // Do nothing if email, password, or confirm password empty
        if (email.trim().equals("") || password.trim().equals("") || cfPassword.trim().equals("")) {
            Toast.makeText(LoginActivity.this,
                    "Please fill in all the fields!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Do nothing if email or password empty
        if (!password.equals(cfPassword)) {
            Toast.makeText(LoginActivity.this,
                    "Password and Confirm Password are not matched. Please try again!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Sign In using email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("DEBUG/LoginActivity", "createUserWithEmail:success");
                            finish();
                        } else {
                            try {
                                throw task.getException();
                            } catch (Exception e) {
                                String message = e.getMessage() + ". Please try again!";
                                Toast.makeText(LoginActivity.this, message,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}