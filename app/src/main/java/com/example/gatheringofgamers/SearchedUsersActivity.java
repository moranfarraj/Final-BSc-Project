package com.example.gatheringofgamers;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class SearchedUsersActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private String userId;
    List<User> usersToShow;
    List<userGames> userGamesList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.searched_users);
        usersToShow = new ArrayList<>();
        userGamesList = new ArrayList<>();
        userId = getIntent().getStringExtra("userID");
        usersToShow = (List<User>) getIntent().getSerializableExtra("userList");
        userGamesList = (List<userGames>) getIntent().getSerializableExtra("userGamesList");
        Log.w(TAG,"users:"+usersToShow.size());
        RecyclerView recyclerView = findViewById(R.id.searched_users_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        RecyclerView.Adapter adapter = new MyAdapter(this, usersToShow, userId,userGamesList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        ImageButton backBut = findViewById(R.id.back_button);
        backBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
