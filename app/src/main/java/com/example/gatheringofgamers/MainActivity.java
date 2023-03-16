package com.example.gatheringofgamers;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    // Get a reference to the Firestore database
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
// Add the user to the "users" collection in Firestore
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }
    public void onButtonClick(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
    public void loginButton(View view){
        Intent intent = new Intent(this, mainPageActivity.class);
        startActivity(intent);
    }
}