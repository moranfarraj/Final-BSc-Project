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
                        }
                        Log.w(TAG,"test");
                    }
                }
            }
        });
        Log.w(TAG,"userGames:"+userGamesList.size());
            userGames userGame = userGamesList.get(position);
            if(userId.equals(userGame.getUserId())) {
                holder.gameText.setText(userGame.getName());
                colorBox(userGame.getCompetitiveLevel(),"competitive",holder);
                colorBox(userGame.getSkillLevel(),"skill",holder);
                colorBox(userGame.getCommunicationLevel(),"communication",holder);
            }
    }


    @Override
    public int getItemCount() {
        return userGamesList.size();
    }
    public void colorBox(String lvl,String Type,UserGameViewHolder viewHolder){
        if(Type.equals("competitive")){
            if(lvl.equals("1")){
                viewHolder.competitiveView1.setBackgroundColor(Color.RED);
            }
            if(lvl.equals("2")){
                viewHolder.competitiveView1.setBackgroundColor(Color.RED);
                viewHolder.competitiveView2.setBackgroundColor(Color.RED);
            }
            if(lvl.equals("3")){
                viewHolder.competitiveView1.setBackgroundColor(Color.RED);
                viewHolder.competitiveView2.setBackgroundColor(Color.RED);
                viewHolder.competitiveView3.setBackgroundColor(Color.RED);
            }
            if(lvl.equals("4")){
                viewHolder.competitiveView1.setBackgroundColor(Color.RED);
                viewHolder.competitiveView2.setBackgroundColor(Color.RED);
                viewHolder.competitiveView3.setBackgroundColor(Color.RED);
                viewHolder.competitiveView4.setBackgroundColor(Color.RED);
            }
            if(lvl.equals("5")){
                viewHolder.competitiveView1.setBackgroundColor(Color.RED);
                viewHolder.competitiveView2.setBackgroundColor(Color.RED);
                viewHolder.competitiveView3.setBackgroundColor(Color.RED);
                viewHolder.competitiveView4.setBackgroundColor(Color.RED);
                viewHolder.competitiveView5.setBackgroundColor(Color.RED);
            }
        }
        if(Type.equals("skill")){
            if(lvl.equals("1")){
                viewHolder.skillView1.setBackgroundColor(Color.GREEN);
            }
            if(lvl.equals("2")){
                viewHolder.skillView1.setBackgroundColor(Color.GREEN);
                viewHolder.skillView2.setBackgroundColor(Color.GREEN);
            }
            if(lvl.equals("3")){
                viewHolder.skillView1.setBackgroundColor(Color.GREEN);
                viewHolder.skillView2.setBackgroundColor(Color.GREEN);
                viewHolder.skillView3.setBackgroundColor(Color.GREEN);
            }
            if(lvl.equals("4")){
                viewHolder.skillView1.setBackgroundColor(Color.GREEN);
                viewHolder.skillView2.setBackgroundColor(Color.GREEN);
                viewHolder.skillView3.setBackgroundColor(Color.GREEN);
                viewHolder.skillView4.setBackgroundColor(Color.GREEN);
            }
            if(lvl.equals("5")){
                viewHolder.skillView1.setBackgroundColor(Color.GREEN);
                viewHolder.skillView2.setBackgroundColor(Color.GREEN);
                viewHolder.skillView3.setBackgroundColor(Color.GREEN);
                viewHolder.skillView4.setBackgroundColor(Color.GREEN);
                viewHolder.skillView5.setBackgroundColor(Color.GREEN);
            }
        }if(Type.equals("communication")){
            if(lvl.equals("1")){
                viewHolder.communicationView1.setBackgroundColor(Color.BLUE);
            }
            if(lvl.equals("2")){
                viewHolder.communicationView1.setBackgroundColor(Color.BLUE);
                viewHolder.communicationView2.setBackgroundColor(Color.BLUE);
            }
            if(lvl.equals("3")){
                viewHolder.competitiveView1.setBackgroundColor(Color.BLUE);
                viewHolder.competitiveView2.setBackgroundColor(Color.BLUE);
                viewHolder.competitiveView3.setBackgroundColor(Color.BLUE);
            }
            if(lvl.equals("4")){
                viewHolder.communicationView1.setBackgroundColor(Color.BLUE);
                viewHolder.communicationView2.setBackgroundColor(Color.BLUE);
                viewHolder.communicationView3.setBackgroundColor(Color.BLUE);
                viewHolder.communicationView4.setBackgroundColor(Color.BLUE);
            }
            if(lvl.equals("5")){
                viewHolder.communicationView1.setBackgroundColor(Color.BLUE);
                viewHolder.communicationView2.setBackgroundColor(Color.BLUE);
                viewHolder.communicationView3.setBackgroundColor(Color.BLUE);
                viewHolder.communicationView4.setBackgroundColor(Color.BLUE);
                viewHolder.communicationView5.setBackgroundColor(Color.BLUE);
            }
        }

    }
}
