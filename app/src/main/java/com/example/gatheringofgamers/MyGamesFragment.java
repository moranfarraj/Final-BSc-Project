package com.example.gatheringofgamers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;

public class MyGamesFragment extends Fragment {

    private static final String ARG_USER_ID = "user_id";
    private FirebaseFirestore db;
    private String userId;
    private Button addGameButton;

    public MyGamesFragment() {
        // Required empty public constructor
    }

    public static MyGamesFragment newInstance(String userId) {
        MyGamesFragment fragment = new MyGamesFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_games, container, false);
        Button button = view.findViewById(R.id.addGameButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGame(v);
            }
        });
        TableLayout tableLayout = view.findViewById(R.id.tableLayout);

        Query query = db.collection("userGames").whereEqualTo("userId", userId);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> gameIds = new ArrayList<>();
                List<String> competitiveLevels = new ArrayList<>();
                List<String> skillLevels = new ArrayList<>();
                List<String> communicationLevels = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Add the gameID field and other fields from each matching document to their respective lists
                    // Add the gameID field and other fields from each matching document to their respective lists
                    gameIds.add(document.getString("gameId"));
                    competitiveLevels.add(String.valueOf(document.getLong("competitiveLevel")));
                    skillLevels.add(String.valueOf(document.getLong("skillLevel")));
                    communicationLevels.add(String.valueOf(document.getLong("communicationLevel")));

                }

                // Now you have a list of game IDs that the user plays, and you can use them to retrieve the corresponding game documents from the games collection
                for (String gameId : gameIds) {
                    db.collection("games").document(gameId).get().addOnCompleteListener(gameTask -> {
                        if (gameTask.isSuccessful()) {
                            DocumentSnapshot gameDocument = gameTask.getResult();
                            // Extract the game name from the game document
                            String gameName = gameDocument.getString("name");
                            // Find the index of the game in the list of game IDs
                            int gameIndex = gameIds.indexOf(gameId);
                            // Extract the other fields from the corresponding index in their respective lists
                            String competitiveLevel = competitiveLevels.get(gameIndex);
                            String skillLevel = skillLevels.get(gameIndex);
                            String communicationLevel = communicationLevels.get(gameIndex);

                            // Create a new row in the table and populate it with the game name and other fields
                            TableRow tableRow = new TableRow(getContext());
                            TextView gameNameView = new TextView(getContext());
                            gameNameView.setText(gameName);
                            TextView competitiveLevelView = new TextView(getContext());
                            competitiveLevelView.setText(competitiveLevel);
                            TextView skillLevelView = new TextView(getContext());
                            skillLevelView.setText(skillLevel);
                            TextView communicationLevelView = new TextView(getContext());
                            communicationLevelView.setText(communicationLevel);

// Set layout params for each view with weight 1
                            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
                            competitiveLevelView.setLayoutParams(layoutParams);
                            skillLevelView.setLayoutParams(layoutParams);
                            communicationLevelView.setLayoutParams(layoutParams);

                            tableRow.addView(gameNameView);
                            tableRow.addView(competitiveLevelView);
                            tableRow.addView(skillLevelView);
                            tableRow.addView(communicationLevelView);
                            tableLayout.addView(tableRow);
                        } else {
                            // Handle the error
                        }
                    });
                }
            } else {
                // Handle the error
            }
        });

        return view;
    }

    public void addGame(View view){
        Intent intent = new Intent(view.getContext(), gettingStartedActivity.class);
        intent.putExtra("userID", userId);
        startActivity(intent);
    }

}
