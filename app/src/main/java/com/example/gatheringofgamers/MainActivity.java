package com.example.gatheringofgamers;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
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
    public void loginButton(View view) {
        EditText usernameEditText = findViewById(R.id.username_edit_text);
        EditText passwordEditText = findViewById(R.id.password_edit_text);

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Query the Firestore collection "users" for a document with the given username and password
        db.collection("users")
                .whereEqualTo("username", username)
                .whereEqualTo("password", password)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot userSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String username = userSnapshot.getId();
                            // Username and password are correct, start the main page activity
                            Intent intent = new Intent(getApplicationContext(), mainPageActivity.class);
                            intent.putExtra("username", username);
                            startActivity(intent);
                        } else {
                            // Display an error message to the user
                            Toast.makeText(getApplicationContext(), "Incorrect username or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Display an error message to the user
                        Toast.makeText(getApplicationContext(), "Failed to login: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}