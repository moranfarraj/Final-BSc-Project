package com.example.gatheringofgamers;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class UserProfileFragment extends Fragment {

    private static final String ARG_USER_ID = "user_id";
    private FirebaseFirestore db;
    private RadioButton genderRadio;
    private EditText dateText;
    private Spinner countrySpinner;
    private String userId;
    private List<String> CountryList;
    private ArrayAdapter<String> adapter;
    private String username;


    public UserProfileFragment() {
        // Required empty public constructor
    }

    public static UserProfileFragment newInstance(String userId) {
        UserProfileFragment fragment = new UserProfileFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        DocumentReference usersRef = db.collection("users").document(userId);
        usersRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Log.w(TAG,"Checking is user exists:"+documentSnapshot.get("username"));
                    username = documentSnapshot.getString("username");
                    String userGender = documentSnapshot.getString("gender");
                    String userBirthDate = documentSnapshot.getString("dateOfBirth");
                    String userCountry = documentSnapshot.getString("country");
                    TextView gender = view.findViewById(R.id.test);

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
}

