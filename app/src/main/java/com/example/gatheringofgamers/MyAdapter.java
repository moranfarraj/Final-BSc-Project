package com.example.gatheringofgamers;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Layout;
import android.util.Base64;
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

import java.util.*;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context;
    List<User> users;
    Button addTeammateBut;
    Button reportUserBut;
    String userId;
    RecyclerView recyclerView;
    UserGameAdapter adapter;
    String currUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userGamesRef;
    CollectionReference gamesRef;
    List<userGames> userGamesList;
    List<userGames> specificUserGames;
    List<Game> games;
    int count=0;
    int count2=0;
    int finalSize;

    public MyAdapter(Context context, List<User> users,String user,List<userGames>userGamesList) {
        this.currUser = user;
        this.context = context;
        this.users = users;
        this.userGamesList = userGamesList;
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                return Double.compare(user2.getScore(), user1.getScore());
            }
        });
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
        Log.w(TAG,"POSITION:"+position);
        User user = users.get(position);
        Log.w(TAG,"ALL GAMES:"+userGamesList.size());
        specificUserGames = new ArrayList<>();
        if(userGamesList!=null){
            for (userGames userGame : userGamesList) {
                if (userGame.getUserId().equals(user.getId())) {
                    specificUserGames.add(userGame);
                }
            }
        }
        if (user.getImg() != null) {
            // Decode the Base64 string and convert it into a Bitmap
            byte[] decodedString = Base64.decode(user.getImg(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            // Set the Bitmap in the ImageView
            holder.profile_img.setImageBitmap(decodedByte);
        }
        Log.w(TAG," USER: "+user.getName()+""+user.getId()+" GAMES: "+specificUserGames.size());
        recyclerView = holder.gamesList;
        recyclerView.setLayoutManager((new LinearLayoutManager(this.context,LinearLayoutManager.VERTICAL,false)));
        games = new ArrayList<>();
        userId = users.get(position).getId();
        userGamesRef = db.collection("userGames");
        gamesRef = db.collection("games");
        holder.nameView.setText(users.get(position).getName());
        if(users.get(position).getScore()!=0) {
            if (users.get(position).getScore() > 75) {
                holder.recommendationScore.setText("" + users.get(position).getScore() + "% Match");
                holder.recommendationScore.setTextColor(Color.parseColor("#FF2C8C12"));
            }
            if (users.get(position).getScore() <= 50 ) {
                holder.recommendationScore.setText("" + users.get(position).getScore() + "% Match");
                holder.recommendationScore.setTextColor(Color.RED);
            }
            if (users.get(position).getScore() > 50 && users.get(position).getScore()<=75) {
                holder.recommendationScore.setText("" + users.get(position).getScore() + "% Match");
                holder.recommendationScore.setTextColor(Color.parseColor("#FFBD9710"));
            }
        }
        holder.genderView.setText(users.get(position).getGender());
        holder.locationView.setText(users.get(position).getLocation());
        addTeammateBut = holder.addTeammate;
        addTeammateBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTeammateFunction(view,user);
            }
        });
        reportUserBut = holder.reportUser;
        reportUserBut.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                ReportUserFunction(view,user);
            }
        });
        UserGameAdapter adapter = new UserGameAdapter(context,specificUserGames,userId);
        recyclerView.setAdapter(adapter);
        /*
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
                        gamesRef.document(userGame.getGameId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                              userGame.setName(documentSnapshot.get("name").toString());
                              Log.w(TAG,"NAMES CHECK:"+userGame.getName());
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
                    try {
                        Thread.sleep(500); // Wait for 500 milliseconds (0.5 seconds)
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    UserGameAdapter adapter = new UserGameAdapter(context,userGamesList,userId);
                    recyclerView.setAdapter(adapter);
                }
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void addTeammateFunction(View v,User user){
        String currentUserId = currUser;
        String teammateId = user.getId();
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
    public void ReportUserFunction(View v, User user) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("Are you sure?");

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ReportUser(v,user);
            }
        });

        alertDialogBuilder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void ReportUser(View v,User user){
        Toast.makeText(v.getContext(), "Successfully Reported User", Toast.LENGTH_SHORT).show();
    }
}
