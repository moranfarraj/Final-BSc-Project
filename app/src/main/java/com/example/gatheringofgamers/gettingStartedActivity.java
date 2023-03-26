package com.example.gatheringofgamers;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
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


public class gettingStartedActivity extends AppCompatActivity {
    private FirebaseFirestore mFirestore;
    private CollectionReference gamesRef ;
    private CollectionReference UserGames;
    private CollectionReference users;
    private List<String> gameList;
    private ArrayAdapter<String> gamesAdapter;
    private String user;
    private String userID;
    private String gameID;
    private Spinner gamesSpinner;
    private SeekBar competitiveLevelSeekBar;
    private SeekBar levelSeekBar;
    private SeekBar communicationLevelSeekBar;
    private String tempUser;
    private FirebaseUser currUser;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gettingstarted1);
        //Firebase connections
        mFirestore = FirebaseFirestore.getInstance();
        gamesRef = mFirestore.collection("games");
        UserGames = mFirestore.collection("userGames");
        //Receiving the user's ID
        userID = getIntent().getStringExtra("userID");
        //Receiving the user's name for saving and display
        DocumentReference userRef = mFirestore.collection("users").document(userID);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    user = documentSnapshot.getString("username");
                    TextView welcomeTextView = findViewById(R.id.welcome_text);
                    welcomeTextView.setText("Welcome, " + user);
                    // do something with the username
                } else {
                    TextView welcomeTextView = findViewById(R.id.welcome_text);
                    welcomeTextView.setText("Welcome, Error!");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // error retrieving document
            }
        });

        //getting the userId from username

        gameList = new ArrayList<>();
        // Initialize spinner
        gamesSpinner= findViewById(R.id.game_spinner);
        competitiveLevelSeekBar = findViewById(R.id.competitive_level_seekbar);
        communicationLevelSeekBar = findViewById(R.id.communication_level_seekbar);
        levelSeekBar = findViewById(R.id.level_seekbar);
        // Set spinner adapter
        gamesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, gameList);
        gamesSpinner.setAdapter(gamesAdapter);
        gamesRef = mFirestore.collection("games");
        Query query = gamesRef.orderBy("name", Query.Direction.ASCENDING);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Add countries to list and notify adapter
                    for (DocumentSnapshot document : task.getResult().getDocuments()) {
                        String game = document.getString("name");
                        gameList.add(game);
                    }
                    gamesAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(gettingStartedActivity.this, "Error getting games", Toast.LENGTH_SHORT).show();
                }
            }
        });
        gamesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get selected country
                String selectedGame = parent.getItemAtPosition(position).toString();
                Toast.makeText(gettingStartedActivity.this, "Selected game: " + selectedGame, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void saveUserGame(View view){
        String selectedGame = gamesSpinner.getSelectedItem().toString();
        int competitiveLevel = competitiveLevelSeekBar.getProgress();
        int skillLevel = levelSeekBar.getProgress();
        int communicationlevel = communicationLevelSeekBar.getProgress();
        //CHECK THE GAMES NAME WHEN SAVED! ITS NULL!!!
        Query query = gamesRef.whereEqualTo("name",selectedGame);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    boolean foundGame = false;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getString("name").equals(selectedGame)) {
                            foundGame = true;
                            // Save user ID
                            gameID = document.getId();
                            Map<String, Object> gameSelection = new HashMap<>();
                            gameSelection.put("userId", userID);
                            gameSelection.put("gameId", gameID);
                            gameSelection.put("competitiveLevel", competitiveLevel);
                            gameSelection.put("skillLevel", skillLevel);
                            gameSelection.put("communicationLevel",communicationlevel);

                            UserGames.add(gameSelection)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            // Display a success message to the user
                                            Toast.makeText(getApplicationContext(), "Details added successfully", Toast.LENGTH_SHORT).show();
                                            Button goToProfileButton = findViewById(R.id.go_to_profile);
                                            goToProfileButton.setVisibility(View.VISIBLE);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Display an error message to the user
                                            Toast.makeText(getApplicationContext(), "Failed to add details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            break;
                        }
                    }
                    if (!foundGame) {
                        Toast.makeText(gettingStartedActivity.this, "Game not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(gettingStartedActivity.this, "Error getting game", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void goToProfile(View view){
        Intent intent = new Intent(getApplicationContext(), mainPageActivity.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }
    }

