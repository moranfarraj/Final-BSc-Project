package com.example.gatheringofgamers;

import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USER_ID = "userId";


    // TODO: Rename and change types of parameters
    private FirebaseFirestore db;
    private User currUser;
    private String userId;
    private String selectedCountry;
    private List<String> mCountryList;
    private ArrayAdapter<String> mCountryAdapter;


    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userId Parameter 1.

     * @return A new instance of fragment Tab2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String userId) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString(ARG_USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment,container,false);
        mCountryList = new ArrayList<>();
        Spinner mSpinnerCountries = view.findViewById(R.id.spinner_countries);
        mCountryAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, mCountryList);
        mSpinnerCountries.setAdapter(mCountryAdapter);
        CollectionReference usersRef = db.collection("users");
        CollectionReference countriesRef = db.collection("countries");
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
                    Toast.makeText(view.getContext(), "Error getting countries", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set spinner item selected listener
        mSpinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get selected country
                selectedCountry = parent.getItemAtPosition(position).toString();
                Toast.makeText(view.getContext(), "Selected country: " + selectedCountry, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Button searchBut = view.findViewById(R.id.search_button);
        searchBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchTeammate(view);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void SearchTeammate(View v){
        String gender ="";
        RadioButton mRadio = v.findViewById(R.id.radioButtonMale);
        RadioButton fRadio = v.findViewById(R.id.radioButtonFemale);
        RadioButton oRadio = v.findViewById(R.id.radioButtonOther);
        if(mRadio.isChecked())
            gender = "Male";
        else if (fRadio.isChecked())
            gender="Female";
        else if (oRadio.isChecked())
            gender="Other";
        else
            gender="All";

        Query query;
        if(!gender.equals("All")){
            query = db.collection("users")
                    .whereEqualTo("gender", gender)
                    .whereEqualTo("country", selectedCountry);
        }
        else {
            query = db.collection("users")
                    .whereEqualTo("country", selectedCountry);
        }

// Execute the query
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<User> users = new ArrayList<>();
                    QuerySnapshot querySnapshot = task.getResult();
                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                    for (DocumentSnapshot document : documents) {
                        User user = new User(document.getId(),document.get("username").toString(),document.get("gender").toString(),document.get("country").toString());
                        users.add(user);
                    }
                    RecyclerView recyclerView = v.findViewById(R.id.user_list_recycler);
                    recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext(),LinearLayoutManager.HORIZONTAL,false));
                    recyclerView.setAdapter(new MyAdapter(v.getContext(),users,userId));
                } else {
                    // Handle any errors
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }
}