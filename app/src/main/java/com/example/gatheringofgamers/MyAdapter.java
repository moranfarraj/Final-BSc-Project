package com.example.gatheringofgamers;

import android.app.DownloadManager;
import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context;
    List<User> users;
    Button addTeammateBut;
    String userId;
    RecyclerView recyclerView;
    UserGameAdapter adapter;
    String currUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userGamesRef;
    CollectionReference gamesRef;
    List<userGames> userGamesList;
    List<Game> games;
    int count=0;
    int count2=0;
    int finalSize;

    public MyAdapter(Context context, List<User> users,String user) {
        this.currUser = user;
        this.context = context;
        this.users = users;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.user_view,parent,false));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        User user = users.get(position);
        recyclerView = holder.gamesList;
        recyclerView.setLayoutManager((new LinearLayoutManager(this.context,LinearLayoutManager.VERTICAL,false)));
        games = new ArrayList<>();
        userId = users.get(position).getId();
        userGamesRef = db.collection("userGames");
        gamesRef = db.collection("games");
        userGamesList = new ArrayList<>();
        holder.nameView.setText(users.get(position).getName());
        holder.genderView.setText(users.get(position).getGender());
        holder.locationView.setText(users.get(position).getLocation());
        addTeammateBut = holder.addTeammate;
        addTeammateBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTeammateFunction(view);
            }
        });

        Query query;
        //CHECK HERE!!!!
        Log.d(TAG, "Querying userGames collection...");
        query = userGamesRef.whereEqualTo("userId",users.get(position).getId());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                    finalSize = documents.size();
                    for (DocumentSnapshot document : documents) {
                        userGames userGame = new userGames(document.get("userId").toString(), document.get("gameId").toString(), document.get("communicationLevel").toString(),
                                document.get("competitiveLevel").toString(), document.get("skillLevel").toString());
                        gamesRef.document(userGame.gameId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                              userGame.setName(documentSnapshot.get("name").toString());
                                count++;
                                if(count == finalSize){
                                    UserGameAdapter adapter = new UserGameAdapter(context,userGamesList,userId);
                                    recyclerView.setAdapter(adapter);
                                }
                            }
                        });
                        userGamesList.add(userGame);
                       Log.w(TAG,"size test:"+ userGamesList.size());

                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
                if(count == finalSize){
                    UserGameAdapter adapter = new UserGameAdapter(context,userGamesList,userId);
                    recyclerView.setAdapter(adapter);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void addTeammateFunction(View v){
        String currentUserId = currUser;
        String teammateId = userId;
        FriendRequest friendRequest = new FriendRequest(currentUserId,teammateId,"pending");
        db.collection("friendRequests").add(friendRequest)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.w(TAG,"Successfully added user"+teammateId);
                        Toast.makeText(v.getContext(), "Successfully added user!", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.w(TAG,"Error adding user"+teammateId);
                    }
                });
    }
}
