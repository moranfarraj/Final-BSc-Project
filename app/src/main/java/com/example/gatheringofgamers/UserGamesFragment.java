package com.example.gatheringofgamers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class UserGamesFragment extends Fragment {

    private static final String ARG_USER_ID = "user_id";
    private FirebaseFirestore db;
    private String userId;
    RecyclerView recyclerView;
    UserGameAdapter adapter;
    static List<userGames> userGamesList;
    private Button addGameButton;

    public UserGamesFragment() {
        // Required empty public constructor
    }

    public static UserGamesFragment newInstance(String userId,List<userGames> userGames) {
        UserGamesFragment fragment = new UserGamesFragment();
        Bundle args = new Bundle();
        userGamesList = new ArrayList<>();
        args.putString(ARG_USER_ID, userId);
        for(userGames userGame : userGames){
            userGamesList.add(userGame);
        }
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
        View view = inflater.inflate(R.layout.fragment_user_games, container, false);
        //TableLayout tableLayout = view.findViewById(R.id.tableLayout);

        db.collection("games").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                    for (DocumentSnapshot document : documents) {
                        for (userGames userGame : userGamesList) {
                            if (document.getId().equals(userGame.getGameId())) {
                                userGame.setName(document.get("name").toString());
                            }
                        }
                    }
                    recyclerView = view.findViewById(R.id.games_recycler);
                    recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
                    adapter = new UserGameAdapter(view.getContext(),userGamesList,userId);
                    recyclerView.setAdapter(adapter);
                }
                else{
                }
            }
        });
//
//        Query query = db.collection("userGames").whereEqualTo("userId", userId);
//        query.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                List<String> gameIds = new ArrayList<>();
//                List<String> competitiveLevels = new ArrayList<>();
//                List<String> skillLevels = new ArrayList<>();
//                List<String> communicationLevels = new ArrayList<>();
//                for (QueryDocumentSnapshot document : task.getResult()) {
//                    // Add the gameID field and other fields from each matching document to their respective lists
//                    // Add the gameID field and other fields from each matching document to their respective lists
//                    gameIds.add(document.getString("gameId"));
//                    competitiveLevels.add(String.valueOf(document.getLong("competitiveLevel")));
//                    skillLevels.add(String.valueOf(document.getLong("skillLevel")));
//                    communicationLevels.add(String.valueOf(document.getLong("communicationLevel")));
//
//                }
//
//                // Now you have a list of game IDs that the user plays, and you can use them to retrieve the corresponding game documents from the games collection
//                for (String gameId : gameIds) {
//                    db.collection("games").document(gameId).get().addOnCompleteListener(gameTask -> {
//                        if (gameTask.isSuccessful()) {
//                            DocumentSnapshot gameDocument = gameTask.getResult();
//                            // Extract the game name from the game document
//                            String gameName = gameDocument.getString("name");
//                            // Find the index of the game in the list of game IDs
//                            int gameIndex = gameIds.indexOf(gameId);
//                            // Extract the other fields from the corresponding index in their respective lists
//                            String competitiveLevel = competitiveLevels.get(gameIndex);
//                            String skillLevel = skillLevels.get(gameIndex);
//                            String communicationLevel = communicationLevels.get(gameIndex);
//
//                            // Create a new row in the table and populate it with the game name and other fields
//                            TableRow tableRow = new TableRow(getContext());
//                            TextView gameNameView = new TextView(getContext());
//                            gameNameView.setText(gameName);
//                            TextView competitiveLevelView = new TextView(getContext());
//                            competitiveLevelView.setText(competitiveLevel);
//                            TextView skillLevelView = new TextView(getContext());
//                            skillLevelView.setText(skillLevel);
//                            TextView communicationLevelView = new TextView(getContext());
//                            communicationLevelView.setText(communicationLevel);
//
//// Set layout params for each view with weight 1
//                            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
//                            competitiveLevelView.setLayoutParams(layoutParams);
//                            skillLevelView.setLayoutParams(layoutParams);
//                            communicationLevelView.setLayoutParams(layoutParams);
//
//                            tableRow.addView(gameNameView);
//                            tableRow.addView(competitiveLevelView);
//                            tableRow.addView(skillLevelView);
//                            tableRow.addView(communicationLevelView);
//                            tableLayout.addView(tableRow);
//                        } else {
//                            // Handle the error
//                        }
//                    });
//                }
//            } else {
//                // Handle the error
//            }
//        });

        return view;
    }
}
