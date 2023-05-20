package com.example.gatheringofgamers;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class ChatActivity extends AppCompatActivity {
    FirebaseFirestore db;
    CollectionReference usersRef;
    String username;
    EditText messageContext;
    DatabaseReference reference ;
    List<Chat> mchat;
    RecyclerView recyclerView;
    String myId;
    String userId;
    MessagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.chatRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        usersRef = db.collection("users");
        messageContext = findViewById(R.id.inputEditText);
        // Set up back button
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> onBackPressed());


        TextView usernameText = findViewById(R.id.chatUserName);
        userId = getIntent().getStringExtra("userId");
        myId = getIntent().getStringExtra("MyId");
        Log.w(TAG,"user:"+myId);
        ImageButton sendMsgButton = findViewById(R.id.sendButton);

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
                readMessage(myId,userId);
            }

        });
        sendMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = messageContext.getText().toString();
                if(!msg.equals("")) {
                    SendMessage(myId, userId,msg);
                }
                else{
                    Toast.makeText(ChatActivity.this, "You cannot send an empty message.", Toast.LENGTH_SHORT).show();
                }
                messageContext.setText("");
            }
        });


//        List<String> messages = new ArrayList<>();
//        messages.add("Hello");
//        messages.add("are u here?");
//        messages.add("test");
//        RecyclerView recyclerView = findViewById(R.id.chatRecyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
//        adapter = new MessagesAdapter(messages,this);
//        recyclerView.setAdapter(adapter);

    }

    public void SendMessage(String sender, String receiver,String message){
        reference = FirebaseDatabase.getInstance("https://gathering-of-gamers-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Chats");
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        reference.push().setValue(hashMap);
    }
    private void readMessage(String myid,String userid){
        mchat =  new ArrayList<>();
        reference = FirebaseDatabase.getInstance("https://gathering-of-gamers-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    Log.w(TAG,"this is the chat:"+chat.getMessage()+"sender:"+chat.getSender()+"receiver:"+chat.getReceiver());
                    if(chat.getReceiver()!=null && chat.getSender() !=null && chat.getMessage() !=null) {
                        if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                            mchat.add(chat);
                        }
                    }
                }
                adapter = new MessagesAdapter(ChatActivity.this,mchat,myId,userId);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}
