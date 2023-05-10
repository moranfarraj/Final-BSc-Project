package com.example.gatheringofgamers;

import android.app.DownloadManager;
import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context;
    List<User> users;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userGamesRef;
    CollectionReference gamesRef;

    public MyAdapter(Context context, List<User> users) {
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
        List<String> games = new ArrayList<>();
        userGamesRef = db.collection("userGames");
        gamesRef = db.collection("games");
        List<userGames> userGamesList = new ArrayList<>();
        holder.nameView.setText("Name:"+users.get(position).getName());
        holder.genderView.setText("Gender:"+users.get(position).getGender());
        holder.locationView.setText("Location:"+users.get(position).getLocation());
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
                    for (DocumentSnapshot document : documents) {
                        userGames userGame = new userGames(document.get("userId").toString(), document.get("gameId").toString(), document.get("communicationLevel").toString(),
                                document.get("competitiveLevel").toString(), document.get("skillLevel").toString());
                        userGamesList.add(userGame);

                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
                for(userGames userGame : userGamesList){
                    String gameId = userGame.getGameId();
                    gamesRef.get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            for (QueryDocumentSnapshot document1 : task1.getResult()) {
                                // Access the data in the document
                                if(document1.getId().equals(gameId)) {
                                    String name = document1.get("name").toString();
                                    games.add(name);
                                }
                                // Do something with the data
                            }
                        } else {
                            games.add("No Games");
                            Log.d(TAG, "Error getting documents: ", task1.getException());
                        }
                    });
                }
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.context, android.R.layout.simple_list_item_1, games);
        holder.gamesList.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
