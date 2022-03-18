package com.bcit.calories_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            this.updateUI();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.updateUI();
    }

    public void updateUI() {
        Log.d("DEBUG/MainActivity", "here");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d("DEBUG/MainActivity", "here inside");
            TextView textView = findViewById(R.id.textView_main);
            if (textView != null) {
                textView.setText("Welcome " + currentUser.getEmail());
            }
        }
    }

    public void onClickLogout(View view) {
        mAuth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}