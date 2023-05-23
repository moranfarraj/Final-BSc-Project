package com.example.gatheringofgamers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class UserGameAdapter extends RecyclerView.Adapter<UserGameViewHolder> {
    Context context;
    FirebaseFirestore db;
    List<userGames> userGamesList;
    List<Game> games;
    String name;
    String userId;
    int size;
    public UserGameAdapter(Context context, List<userGames> userGamesList,String userId){
        this.context = context;
        this.userId = userId;
        this.userGamesList = userGamesList;
        Log.w(TAG,"SIZE:"+userGamesList.size());
        games = new ArrayList<>();
        db=FirebaseFirestore.getInstance();
    }
    @NonNull
    @NotNull
    @Override
    public UserGameViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        UserGameViewHolder userGameViewHolder = new UserGameViewHolder(LayoutInflater.from(context).inflate(R.layout.user_game_item,parent,false));
        return userGameViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserGameViewHolder holder, int position) {
        int temp = position;
        userGames userGame = userGamesList.get(position);
        holder.gameText.setText(userGame.getName());
        Log.w(TAG,"game name:"+userGame.getName());
        Log.w(TAG,"skill:"+userGame.skillLevel);
        Log.w(TAG,"communication:"+userGame.communicationLevel);
        Log.w(TAG,"competitive:"+userGame.getCompetitiveLevel());
        colorBox(userGame.getCompetitiveLevel(),"competitive",holder);
        colorBox(userGame.getSkillLevel(),"skill",holder);
        colorBox(userGame.getCommunicationLevel(),"communication",holder);
        /*
        name = "";
        db.collection("games").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    size = querySnapshot.size();
                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                    for(DocumentSnapshot document : documents){
                        Game game = new Game(document.getId(),document.get("name").toString());
                        games.add(game);
                        if(game.getId().equals(userGamesList.get(temp).getGameId())){
                            name = game.getName();
                            Log.w(TAG,"userGames:"+userGamesList.size());
                            userGames userGame = userGamesList.get(temp);
                            if(userId.equals(userGame.getUserId())) {
                                holder.gameText.setText(userGame.getName());
                                Log.w(TAG,"user ID:"+userGame.getUserId());
                                Log.w(TAG,"game name:"+userGame.getName());
                                colorBox(userGame.getCompetitiveLevel(),"competitive",holder);
                                colorBox(userGame.getSkillLevel(),"skill",holder);
                                colorBox(userGame.getCommunicationLevel(),"communication",holder);
                            }
                        }
                        Log.w(TAG,"test");
                    }
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return userGamesList.size();
    }
    public void colorBox(String lvl,String Type,UserGameViewHolder viewHolder){
        Log.w(TAG,"type:"+Type+"lvl:"+lvl+"holder:"+viewHolder.toString());
        if(Type.equals("competitive")){
            if(lvl.equals("1")){
                viewHolder.competitiveView1.setBackgroundColor(Color.parseColor("#FF0D98BA"));
            }
            if(lvl.equals("2")){
                viewHolder.competitiveView1.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.competitiveView2.setBackgroundColor(Color.parseColor("#FF0D98BA"));
            }
            if(lvl.equals("3")){
                viewHolder.competitiveView1.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.competitiveView2.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.competitiveView3.setBackgroundColor(Color.parseColor("#FF0D98BA"));
            }
            if(lvl.equals("4")){
                viewHolder.competitiveView1.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.competitiveView2.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.competitiveView3.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.competitiveView4.setBackgroundColor(Color.parseColor("#FF0D98BA"));
            }
            if(lvl.equals("5")){
                viewHolder.competitiveView1.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.competitiveView2.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.competitiveView3.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.competitiveView4.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.competitiveView5.setBackgroundColor(Color.parseColor("#FF0D98BA"));
            }
        }
        else if(Type.equals("skill")){
            if(lvl.equals("1")){
                viewHolder.skillView1.setBackgroundColor(Color.parseColor("#FF0D98BA"));
            }
            if(lvl.equals("2")){
                viewHolder.skillView1.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.skillView2.setBackgroundColor(Color.parseColor("#FF0D98BA"));
            }
            if(lvl.equals("3")){
                viewHolder.skillView1.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.skillView2.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.skillView3.setBackgroundColor(Color.parseColor("#FF0D98BA"));
            }
            if(lvl.equals("4")){
                viewHolder.skillView1.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.skillView2.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.skillView3.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.skillView4.setBackgroundColor(Color.parseColor("#FF0D98BA"));
            }
            if(lvl.equals("5")){
                viewHolder.skillView1.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.skillView2.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.skillView3.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.skillView4.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.skillView5.setBackgroundColor(Color.parseColor("#FF0D98BA"));
            }
        }else if(Type.equals("communication")){
            if(lvl.equals("1")){
                viewHolder.communicationView1.setBackgroundColor(Color.parseColor("#FF0D98BA"));
            }
            if(lvl.equals("2")){
                viewHolder.communicationView1.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.communicationView2.setBackgroundColor(Color.parseColor("#FF0D98BA"));
            }
            if(lvl.equals("3")){
                viewHolder.communicationView1.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.communicationView2.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.communicationView3.setBackgroundColor(Color.parseColor("#FF0D98BA"));
            }
            if(lvl.equals("4")){
                viewHolder.communicationView1.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.communicationView2.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.communicationView3.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.communicationView4.setBackgroundColor(Color.parseColor("#FF0D98BA"));
            }
            if(lvl.equals("5")){
                viewHolder.communicationView1.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.communicationView2.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.communicationView3.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.communicationView4.setBackgroundColor(Color.parseColor("#FF0D98BA"));
                viewHolder.communicationView5.setBackgroundColor(Color.parseColor("#FF0D98BA"));
            }
        }

    }
}
