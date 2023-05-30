package com.example.gatheringofgamers;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

import java.util.*;

public class SignupActivity extends AppCompatActivity {

    private Spinner mSpinnerCountries;
    private String userId;
    private FirebaseFirestore mFirestore;

    private List<String> mCountryList;
    private ArrayAdapter<String> mCountryAdapter;
    private String tempUser;

    private EditText dateOfBirthEditText;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

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
        mCountryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mCountryList){
            @Override
            public View getDropDownView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                textView.setTextColor(getResources().getColor(R.color.black));
                return view;
            }
        }; mSpinnerCountries.setAdapter(mCountryAdapter);

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
        dateOfBirthEditText = findViewById(R.id.editTextDateOfBirth);
        dateOfBirthEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        SignupActivity.this,
                        mDateSetListener,
                        year, month, day);
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1; // Month is counted starting from 0 so we add 1
                String monthString = month < 10 ? "0" + month : String.valueOf(month);
                String dayString = day < 10 ? "0" + day : String.valueOf(day);
                String date = monthString + "/" + dayString  + "/" + year;
                dateOfBirthEditText.setText(date);
            }
        };

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
        EditText usernameEditText = findViewById(R.id.editTextUsername); // Updated reference for username field
        EditText passwordEditText = findViewById(R.id.editTextPassword);
        EditText confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword);
        EditText dateOfBirthEditText = findViewById(R.id.editTextDateOfBirth);

        Spinner countriesSpinner = findViewById(R.id.spinner_countries);

        RadioGroup genderRadioGroup = findViewById(R.id.radioGroupGender);

        String username = usernameEditText.getText().toString(); // Updated variable name for username
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
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(dateOfBirth)) {
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
        user.put("username", username); // Updated field for username
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
                        tempUser = username;
                        Button gettingStartedButton = findViewById(R.id.getting_Started_Button);
                        gettingStartedButton.setVisibility(View.VISIBLE);
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
    public void gettingStartedButton(View view){
        CollectionReference usersRef = mFirestore.collection("users");
        Query query = usersRef.whereEqualTo("username", tempUser);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    boolean foundUser = false;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getString("username").equals(tempUser)) {
                            foundUser = true;
                            // Save user ID
                            String userID = document.getId();
                            // Save user name in intent
                            Intent intent = new Intent(SignupActivity.this, gettingStartedActivity.class);
                            intent.putExtra("userID", userID);
                            startActivity(intent);
                            break;
                        }
                    }
                    if (!foundUser) {
                        Toast.makeText(SignupActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignupActivity.this, "Error getting user", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    }

