package com.example.gatheringofgamers;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class ProfileInfoFragment extends Fragment {

    private static final String ARG_USER_ID = "user_id";
    private FirebaseFirestore db;
    private RadioButton genderRadio;
    private EditText dateText;
    private Spinner countrySpinner;
    private String userId;
    private List<String> CountryList;
    private ArrayAdapter<String> adapter;
    private String username;


    public ProfileInfoFragment() {
        // Required empty public constructor
    }

    public static ProfileInfoFragment newInstance(String userId) {
        ProfileInfoFragment fragment = new ProfileInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        if (getArguments() != null) {
            userId = getArguments().getString(ARG_USER_ID);
        }

    }
    public int calculateAge(String birthDate) {
        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        }
        // Convert the birthDate string to a LocalDate
        LocalDate localBirthDate = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            localBirthDate = LocalDate.parse(birthDate, formatter);
        }
        // Get the current date
        LocalDate now = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            now = LocalDate.now();
        }
        // Calculate the period between the two dates
        Period period = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            period = Period.between(localBirthDate, now);
        }
        // Return the years part of the period
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return period.getYears();
        } else {
            return -1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_info, container, false);
        Button editProfileButton = view.findViewById(R.id.editProfileButton);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile(view);
            }
        });

        // Initialize and disable the countrySpinner here
        countrySpinner = view.findViewById(R.id.spinner_countries);
        countrySpinner.setEnabled(false);

        DocumentReference usersRef = db.collection("users").document(userId);
        usersRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    username = documentSnapshot.getString("username");
                    String userGender = documentSnapshot.getString("gender");
                    String userBirthDate = documentSnapshot.getString("dateOfBirth");
                    String userCountry = documentSnapshot.getString("country");
                    TextView ageTextView = view.findViewById(R.id.age_text);
                    int age = calculateAge(userBirthDate);

                    if(userGender.equals("Male")){
                        genderRadio = view.findViewById(R.id.radioButtonMale);
                        genderRadio.setChecked(true);
                    }else if(userGender.equals("Female")){
                        genderRadio = view.findViewById(R.id.radioButtonFemale);
                        genderRadio.setChecked(true);
                    }else if(userGender.equals("Other")){
                        genderRadio = view.findViewById(R.id.radioButtonOther);
                        genderRadio.setChecked(true);
                    }
                    else{

                    }
                    ageTextView.setText("Age: "+String.valueOf(age));
                    TextView gender = view.findViewById(R.id.test);
                    dateText = view.findViewById(R.id.editTextDateOfBirth);
                    dateText.setText(userBirthDate);
                    countrySpinner = view.findViewById(R.id.spinner_countries);
                    countrySpinner.setClickable(false);
                    CountryList = new ArrayList<>();
                    adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, CountryList);
                    countrySpinner.setAdapter(adapter);
                    CollectionReference countriesRef = db.collection("countries");
                    Query query = countriesRef.orderBy("name", Query.Direction.ASCENDING);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                // Add countries to list and notify adapter
                                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                    String country = document.getString("name");
                                    CountryList.add(country);
                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(view.getContext(), "Error getting countries", Toast.LENGTH_SHORT).show();
                            }
                            int position = -1;
                            for (int i = 0; i < adapter.getCount(); i++) {
                                if (adapter.getItem(i).equals(userCountry)) {
                                    position = i;
                                    break;
                                }
                            }
                            if (position != -1) {
                                countrySpinner.setSelection(position);
                            }
                            else{
                                countrySpinner.setSelection(-1);
                            }
                        }
                    });
                    // replace with the name of the country you're looking for



                } else {

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        return view;
    }
    public void editProfile(View view){
        Button button = view.findViewById(R.id.editProfileButton);
        RadioButton mRadio = view.findViewById(R.id.radioButtonMale);
        RadioButton oRadio = view.findViewById(R.id.radioButtonOther);
        RadioButton fRadio = view.findViewById(R.id.radioButtonFemale);
        if(button.getText().equals("Edit Profile")){

            mRadio.setEnabled(true);
            fRadio.setEnabled(true);
            oRadio.setEnabled(true);
            dateText.setEnabled(true);
            countrySpinner.setEnabled(true); // Enable the countrySpinner
            button.setText("Save Changes");
        }
        else if(button.getText().equals("Save Changes")){
            String gender="";
            if(mRadio.isChecked())
                gender="Male";
            if(fRadio.isChecked())
                gender="Female";
            if(oRadio.isChecked())
                gender="Other";
            String dateOfBirth = dateText.getText().toString();
            String selectedCountry = countrySpinner.getSelectedItem().toString(); // Get the selected country
            DocumentReference usersRef = db.collection("users").document(userId);
            Map<String, Object> updates = new HashMap<>();
            updates.put("gender", gender);
            updates.put("dateOfBirth", dateOfBirth);
            updates.put("country", selectedCountry); // Add the country to Firestore

            usersRef.update(updates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "User document updated successfully!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating user document", e);
                        }
                    });
            mRadio.setEnabled(false);
            fRadio.setEnabled(false);
            oRadio.setEnabled(false);
            dateText.setEnabled(false);
            countrySpinner.setEnabled(false); // Disable the countrySpinner again
            button.setText("Edit Profile");
        }
    }

    }

