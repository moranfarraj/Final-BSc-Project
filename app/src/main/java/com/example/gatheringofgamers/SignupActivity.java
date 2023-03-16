package com.example.gatheringofgamers;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private Spinner mSpinnerCountries;

    private FirebaseFirestore mFirestore;

    private List<String> mCountryList;
    private ArrayAdapter<String> mCountryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        // Initialize Firestore
        mFirestore = FirebaseFirestore.getInstance();

        // Initialize country list
        mCountryList = new ArrayList<>();

        // Initialize spinner
        mSpinnerCountries = findViewById(R.id.spinner_countries);

        // Set spinner adapter
        mCountryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mCountryList);
        mSpinnerCountries.setAdapter(mCountryAdapter);

        // Query countries collection
        CollectionReference countriesRef = mFirestore.collection("countries");
        Query query = countriesRef.orderBy("name", Query.Direction.ASCENDING);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Add countries to list and notify adapter
                    for (DocumentSnapshot document : task.getResult().getDocuments()) {
                        String country = document.getString("name");
                        mCountryList.add(country);
                    }
                    mCountryAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(SignupActivity.this, "Error getting countries", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set spinner item selected listener
        mSpinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get selected country
                String selectedCountry = parent.getItemAtPosition(position).toString();
                Toast.makeText(SignupActivity.this, "Selected country: " + selectedCountry, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    public void SignupButton(View view) {
        CollectionReference usersRef = mFirestore.collection("users");
        EditText nameEditText = findViewById(R.id.editTextName);
        EditText emailEditText = findViewById(R.id.editTextEmail);
        EditText passwordEditText = findViewById(R.id.editTextPassword);
        EditText confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword);
        EditText dateOfBirthEditText = findViewById(R.id.editTextDateOfBirth);

        Spinner countriesSpinner = findViewById(R.id.spinner_countries);

        RadioGroup genderRadioGroup = findViewById(R.id.radioGroupGender);

        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String dateOfBirth = dateOfBirthEditText.getText().toString();

        String country = countriesSpinner.getSelectedItem().toString();

        int genderRadioButtonId = genderRadioGroup.getCheckedRadioButtonId();
        String gender = "";
        if (genderRadioButtonId == R.id.radioButtonMale) {
            gender = "Male";
        } else if (genderRadioButtonId == R.id.radioButtonFemale) {
            gender = "Female";
        } else if (genderRadioButtonId == R.id.radioButtonOther) {
            gender = "Other";
        }

        // Validate the user's input
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(dateOfBirth)) {
            // Display an error message to the user
            Toast.makeText(getApplicationContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            // Display an error message to the user
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new user object
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("password", password);
        user.put("country", country);
        user.put("gender", gender);
        user.put("dateOfBirth", dateOfBirth);

        // Add the user object to Firestore
        usersRef.add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Display a success message to the user
                        Toast.makeText(getApplicationContext(), "User created successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Display an error message to the user
                        Toast.makeText(getApplicationContext(), "Failed to create user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void backToLogin(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    }

