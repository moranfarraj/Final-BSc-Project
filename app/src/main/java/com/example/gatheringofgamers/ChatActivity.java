package com.example.gatheringofgamers;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import org.jetbrains.annotations.NotNull;

public class ChatActivity extends AppCompatActivity {
    FirebaseFirestore db;
    CollectionReference usersRef;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");

        // Set up back button
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> onBackPressed());

        TextView usernameText = findViewById(R.id.chatUserName);
        String userId = getIntent().getStringExtra("userId");
        Task<DocumentSnapshot> task = usersRef.document(userId).get();
        task.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        User user = new User(document.getId(),document.get("username").toString(),document.get("gender").toString(),document.get("country").toString());
                        username = user.getName();
                        usernameText.setText(username);
                    }
                }
            }
        });
    }
}