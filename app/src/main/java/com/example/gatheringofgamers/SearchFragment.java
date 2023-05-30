package com.example.gatheringofgamers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Base64;
import android.util.Log;
import android.widget.*;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.firestore.*;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static java.lang.Math.abs;

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
    CollectionReference friendsRef;
    CollectionReference gamesRef;
    ArrayList gameList;
    Spinner gamesSpinner;
    List<Game> games;
    List<userGames> userGamesList;
    ArrayAdapter<String> gamesAdapter;
    private User currUser;
    private String userId;
    private String selectedCountry;
    private String selectedGame;
    private List<String> mCountryList;
    private List<String> gamesList;
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
        friendsRef = db.collection("friendRequests");
        gamesRef = db.collection("games");
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
        List<Game> games = new ArrayList<>();
        userGamesList = new ArrayList<>();
        Spinner mSpinnerCountries = view.findViewById(R.id.spinner_countries);
        mCountryAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, mCountryList) {
            @Override
            public View getDropDownView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                textView.setTextColor(getResources().getColor(R.color.black));
                return view;
            }
        };

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
        gameList = new ArrayList<>();
        gamesSpinner = view.findViewById(R.id.game_spinner);
        gamesAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, gameList){
            @Override
            public View getDropDownView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                textView.setTextColor(getResources().getColor(R.color.black));
                return view;
            }
        };
        gamesSpinner.setAdapter(gamesAdapter);
        Query gameQuery = gamesRef.orderBy("name",Query.Direction.ASCENDING);
        gameQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot document : task.getResult().getDocuments()){
                        Game game = new Game(document.getId(),document.get("name").toString());
                        games.add(game);
                        gameList.add(game.getName());
                    }
                    gamesAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(view.getContext(), "Error getting games", Toast.LENGTH_SHORT).show();

                }
            }
        });
        gamesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get selected country
                selectedGame = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        db.collection("userGames").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot document : task.getResult().getDocuments()){
                    userGames userGame = new userGames(document.get("userId").toString(),document.get("gameId").toString(),document.get("communicationLevel").toString(),
                            document.get("competitiveLevel").toString(),document.get("skillLevel").toString());
                    userGamesList.add(userGame);
                }
            }
        });

        // Set spinner item selected listener
        mSpinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get selected country
                selectedCountry = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button searchBut = view.findViewById(R.id.search_button);
        searchBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchTeammate(view,games,userGamesList);
            }
        });
        Button recommendBut = view.findViewById(R.id.recommendation_button);
        recommendBut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                RecommendedTeammate(view,games,userGamesList);
            }
        });
        CheckBox locationPreferenceBut = view.findViewById(R.id.radioButtonNoPreference);
        locationPreferenceBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeLocationPreference(view);
            }
        });
        CheckBox gamePreferenceBut = view.findViewById(R.id.radioButtonNoPreferenceGame);
        gamePreferenceBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeGamePreference(view);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void SearchTeammate(View v,List<Game> games,List<userGames> userGamesList){
        ProgressDialog progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        CheckBox locationPreference = v.findViewById(R.id.radioButtonNoPreference);
        CheckBox gamePreference = v.findViewById(R.id.radioButtonNoPreferenceGame);

        List<userGames> userGamesTemp = new ArrayList<>();
        List<String> usersID = new ArrayList<>();
        String selectedGameId="";
        //getting the ID of the selectedGame.

            for (Game game : games) {
                if (game.getName().equals(selectedGame)) {
                    selectedGameId = game.getId();
                }
            }
            //Adding the name of each game.
            for (userGames userGame : userGamesList) {
                for (Game game : games) {
                    if (userGame.getGameId().equals(game.getId())) {
                        userGame.setName(game.getName());
                    }
                }
            }
        if(!gamePreference.isChecked()) {
            //adding the users that play the selected game.
            for (userGames userGame : userGamesList) {
                if (userGame.getGameId().equals(selectedGameId)) {
                    Log.w(TAG, "user: " + userGame.getUserId() + "game id:" + userGame.getName());
                    usersID.add(userGame.getUserId());
                }
            }

            //creating new userGames list with all the games of the users that play the selected game.
            for (userGames userGame : userGamesList) {
                for (String id : usersID) {
                    if (userGame.getUserId().equals(id)) {
                        userGamesTemp.add(userGame);
                    }
                }
            }
        }
        else{
            for(userGames userGame : userGamesList){
                userGamesTemp.add(userGame);
            }
        }

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
        if(!gender.equals("All") && locationPreference.isChecked()){
            query = db.collection("users")
                    .whereEqualTo("gender", gender);
        }
        else if(!gender.equals("All") && !locationPreference.isChecked()){
            query = db.collection("users")
                    .whereEqualTo("gender", gender)
                    .whereEqualTo("country", selectedCountry);
        }
        else if(gender.equals("All") && !locationPreference.isChecked()){
            query = db.collection("users")
                    .whereEqualTo("country", selectedCountry);
        }
        else{
            query = db.collection("users");
        }


// Execute the query


        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            int count = 0;
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<User> users = new ArrayList<>();
                    QuerySnapshot querySnapshot = task.getResult();
                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                    int totalUsers = documents.size();
                    Log.w(TAG,"total users:"+totalUsers);
                    for (DocumentSnapshot document : documents) {
                        User user = new User(document.getId(), document.get("username").toString(), document.get("gender").toString(), document.get("country").toString());
                        String encodedImage = document.getString("image");
                        user.setImg(encodedImage);
                        if (!user.getId().equals(userId)) {
                            // check if the document with from = userId and to = user.getId() OR from = user.getId() and to = userId exists in the collection friendsRef
                            friendsRef.whereEqualTo("from", userId).whereEqualTo("to", user.getId())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            if (!queryDocumentSnapshots.isEmpty()) {
                                                count++;
                                            } else {
                                                // document with from = userId and to = user.getId() doesn't exist, check the opposite
                                                friendsRef.whereEqualTo("from", user.getId()).whereEqualTo("to", userId)
                                                        .get()
                                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                if (!queryDocumentSnapshots.isEmpty()) {
                                                                    // document with from = user.getId() and to = userId exists
                                                                } else {
                                                                    Log.w(TAG, "User:" + user.getId());
                                                                    if(!gamePreference.isChecked()) {
                                                                        for (userGames tempUserGame : userGamesTemp) {
                                                                            if (user.getId().equals(tempUserGame.getUserId())) {
                                                                                if(!users.contains(user)) {
                                                                                    users.add(user);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    else{
                                                                        users.add(user);
                                                                    }
//                                                                    RecyclerView.Adapter adapter = new MyAdapter(v.getContext(), users, userId);
//                                                                    recyclerView.setAdapter(adapter);
//                                                                    adapter.notifyDataSetChanged();
                                                                }
                                                                count++;
                                                                if(count == totalUsers){
                                                                    List<User> userList = new ArrayList<>();
                                                                    for(User user : users){
                                                                        userList.add(user);
                                                                    }
                                                                    progressDialog.dismiss();
                                                                    Intent intent = new Intent(v.getContext(), SearchedUsersActivity.class);
                                                                    intent.putExtra("userList", (Serializable) userList);
                                                                    intent.putExtra("userGamesList", (Serializable) userGamesTemp);
                                                                    intent.putExtra("userID",userId);
                                                                    startActivity(intent);
                                                                }

                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                        else {
                            count++;
                        }
                    }
                    if(count==totalUsers) {
                        Log.w(TAG, "users size sent:" + users.size());
                        List<User> userList = new ArrayList<>();
                        progressDialog.dismiss();
                        Intent intent = new Intent(v.getContext(), SearchedUsersActivity.class);
                        intent.putExtra("userGamesList", (Serializable) userGamesTemp);
                        intent.putExtra("userList", (Serializable) userList);
                        startActivity(intent);
                    }
//                    RecyclerView.Adapter adapter =new MyAdapter(v.getContext(), users, userId);
//                    recyclerView.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
                } else {
                    // Handle any errors
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }
    public void RecommendedTeammate(View view,List<Game> games,List<userGames> userGamesList) {
        ProgressDialog progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        List<userGames> userGamesHelp= new ArrayList<>();
        List<userGames> userGamesRecommended = new ArrayList<>();
        List<String> usersID = new ArrayList<>();
        List<userGames> userGamesTemp = new ArrayList<>();
        String gameId = "";
        userGames myGame = null;
        //getting the game id of the selectedGame
        for (Game game : games) {
            if (selectedGame.equals(game.getName())) {
                gameId = game.getId();
            }
        }
        //adding the name of the game in userGame.
        for(userGames userGame : userGamesList){
            for(Game game : games){
                if(userGame.getGameId().equals(game.getId())){
                    userGame.setName(game.getName());
                }
            }
        }
        //adding the users that play the selected game.
        for(userGames userGame : userGamesList){
            if(userGame.getGameId().equals(gameId)){
                usersID.add(userGame.getUserId());
            }
        }
        //creating new userGames list with all the games of the users that play the selected game.
        for(userGames userGame : userGamesList){
            for(String id : usersID){
                if(userGame.getUserId().equals(id)){
                    userGamesTemp.add(userGame);
                }
            }
        }
        //
        for (userGames userGame : userGamesTemp) {
                if (userGame.getGameId().equals(gameId))
                    if (userGame.getUserId().equals(userId))
                        myGame = new userGames(userGame.getUserId(), userGame.getGameId(), userGame.getCommunicationLevel(), userGame.getCompetitiveLevel(), userGame.getSkillLevel());
        }
        if(myGame == null){
            progressDialog.dismiss();
            Toast.makeText(view.getContext(), "You do not play this game!", Toast.LENGTH_SHORT).show();
        }
        else {
            for (userGames userGame : userGamesTemp) {
                if(userGame.getGameId().equals(gameId)) {
                    int competitiveLevel = Integer.parseInt(userGame.getCompetitiveLevel());
                    int myCompetitiveLevel = Integer.parseInt(myGame.getCompetitiveLevel());
                    int competitiveScore = (5 - abs(myCompetitiveLevel - competitiveLevel));
                    int skillLevel = Integer.parseInt(userGame.getSkillLevel());
                    int mySkillLevel = Integer.parseInt(myGame.getSkillLevel());
                    int skillScore = (5 - abs(mySkillLevel - skillLevel));
                    int communicationLevel = Integer.parseInt(userGame.getCommunicationLevel());
                    int myCommunicationLevel = Integer.parseInt(myGame.getCommunicationLevel());
                    int communicationScore = (5 - abs(myCommunicationLevel - communicationLevel));
                    double finalScore = (competitiveScore + skillScore + communicationScore) * (100 / 15);
                    userGame.setScore(finalScore);
                }
            }
            db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                int count = 0;

                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<User> users = new ArrayList<>();
                        QuerySnapshot querySnapshot = task.getResult();
                        List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                        int totalUsers = documents.size();
                        Log.w(TAG, "total users:" + totalUsers);
                        for (DocumentSnapshot document : documents) {
                            User user = new User(document.getId(), document.get("username").toString(), document.get("gender").toString(), document.get("country").toString());
                            String encodedImage = document.getString("image");
                            user.setImg(encodedImage);
                            if (!user.getId().equals(userId)) {
                                // check if the document with from = userId and to = user.getId() OR from = user.getId() and to = userId exists in the collection friendsRef
                                friendsRef.whereEqualTo("from", userId).whereEqualTo("to", user.getId())
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                if (!queryDocumentSnapshots.isEmpty()) {
                                                    count++;
                                                } else {
                                                    // document with from = userId and to = user.getId() doesn't exist, check the opposite
                                                    friendsRef.whereEqualTo("from", user.getId()).whereEqualTo("to", userId)
                                                            .get()
                                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                    if (!queryDocumentSnapshots.isEmpty()) {
                                                                        // document with from = user.getId() and to = userId exists
                                                                    } else {
                                                                        Log.w(TAG, "User:" + user.getId());
                                                                        for (userGames temp : userGamesTemp) {
                                                                            if (user.getId().equals(temp.getUserId())) {
                                                                                Log.w(TAG, "User TEST ID:" + user.getId());
                                                                                if(temp.getName().equals(selectedGame)){
                                                                                    user.setScore(temp.getScore());
                                                                                }
                                                                                if(!users.contains(user)) {
                                                                                    users.add(user);
                                                                                }
                                                                            }
                                                                        }
//                                                                    RecyclerView.Adapter adapter = new MyAdapter(v.getContext(), users, userId);
//                                                                    recyclerView.setAdapter(adapter);
//                                                                    adapter.notifyDataSetChanged();
                                                                    }
                                                                    count++;
                                                                    if (count == totalUsers) {
                                                                        Log.w(TAG, "recommended users size sent:" + users.size());
                                                                        List<User> userList = new ArrayList<>();
                                                                        for (User user : users) {
                                                                            userList.add(user);
                                                                        }
                                                                        progressDialog.dismiss();
                                                                        Intent intent = new Intent(view.getContext(), SearchedUsersActivity.class);
                                                                        intent.putExtra("userList", (Serializable) userList);
                                                                        intent.putExtra("userGamesList", (Serializable) userGamesTemp);
                                                                        intent.putExtra("userID", userId);
                                                                        startActivity(intent);
                                                                    }

                                                                }
                                                            });
                                                }
                                            }
                                        });
                            } else {
                                count++;
                            }
                        }
                        if (count == totalUsers) {
                            Log.w(TAG, "recommended users size sent:" + users.size());
                            List<User> userList = new ArrayList<>();
                            progressDialog.dismiss();
                            Intent intent = new Intent(view.getContext(), SearchedUsersActivity.class);
                            intent.putExtra("userList", (Serializable) userList);
                            intent.putExtra("userGamesList", (Serializable) userGamesTemp);
                            startActivity(intent);
                        }
//                    RecyclerView.Adapter adapter =new MyAdapter(v.getContext(), users, userId);
//                    recyclerView.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
                    } else {
                        // Handle any errors
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                }
            });
        }
    }

    public void ChangeLocationPreference(View v){
        CardView countryCard = v.findViewById(R.id.country_cardview);
        Spinner mSpinnerCountries = v.findViewById(R.id.spinner_countries);
        CheckBox locationPreference = v.findViewById(R.id.radioButtonNoPreference);
        if(locationPreference.isChecked()) {
            mSpinnerCountries.setEnabled(false);
            mSpinnerCountries.setClickable(false);
            countryCard.setEnabled(false);
        }
        else{
            mSpinnerCountries.setEnabled(true);
            mSpinnerCountries.setClickable(true);
            countryCard.setEnabled(true);
        }
    }
    public void ChangeGamePreference(View v){
        CardView gameCard = v.findViewById(R.id.game_cardview);
        Spinner mSpinnerGames = v.findViewById(R.id.game_spinner);
        Button recommendedButton = v.findViewById(R.id.recommendation_button);
        CheckBox gamePreference = v.findViewById(R.id.radioButtonNoPreferenceGame);
        LinearLayout communicationLayout = v.findViewById(R.id.communication_layout);
        LinearLayout skillLayout = v.findViewById(R.id.skill_layout);
        LinearLayout competitiveLayout = v.findViewById(R.id.competitive_layout);
        if(gamePreference.isChecked()) {
            recommendedButton.setClickable(false);
            recommendedButton.setEnabled(false);
            mSpinnerGames.setEnabled(false);
            mSpinnerGames.setClickable(false);
            gameCard.setEnabled(false);
            gameCard.setClickable(false);
            communicationLayout.setVisibility(View.GONE);
            skillLayout.setVisibility(View.GONE);
            competitiveLayout.setVisibility(View.GONE);

        }
        else{
            recommendedButton.setClickable(true);
            recommendedButton.setEnabled(true);
            mSpinnerGames.setEnabled(true);
            mSpinnerGames.setClickable(true);
            gameCard.setEnabled(true);
            gameCard.setClickable(true);
            communicationLayout.setVisibility(View.GONE);
            skillLayout.setVisibility(View.GONE);
            competitiveLayout.setVisibility(View.GONE);
        }
    }
}

/*          query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    final List<User> users = new ArrayList<>();
                    QuerySnapshot querySnapshot = task.getResult();
                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                    for (DocumentSnapshot document : documents) {
                        User user = new User(document.getId(), document.get("username").toString(), document.get("gender").toString(), document.get("country").toString());
                        users.add(user);
                    }

                    // Get the IDs of users who have sent you friend requests, you have sent friend requests, and your current friends
                    CollectionReference friendRequestsRef = db.collection("friendRequests");
                    friendRequestsRef.whereEqualTo("from", userId).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        List<String> friendRequestsIds = new ArrayList<>();
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            friendRequestsIds.add(document.getId());
                                        }

                                        // Remove these users from 'users' list
                                        Iterator<User> iterator = users.iterator();
                                        while (iterator.hasNext()) {
                                            User user = iterator.next();
                                            if (user.getId().equals(userId) || friendRequestsIds.contains(user.getId())) {
                                                iterator.remove();
                                            }
                                        }

                                        RecyclerView recyclerView = v.findViewById(R.id.user_list_recycler);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext(), LinearLayoutManager.HORIZONTAL, false));
                                        RecyclerView.Adapter adapter =new MyAdapter(v.getContext(), users, userId);
                                        recyclerView.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();

                                    } else {
                                        Log.w(TAG, "Error getting friend requests.", task.getException());
                                    }
                                }
                            });
                } else {
                    // Handle any errors
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }
}*/