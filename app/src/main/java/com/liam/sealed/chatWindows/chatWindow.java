package com.liam.sealed.chatWindows;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.liam.sealed.main.MainActivity;
import com.liam.sealed.R;
import com.liam.sealed.adapters.Messages;
import com.liam.sealed.modelClass.Message;
import com.liam.sealed.utility.EncryptionHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatWindow extends AppCompatActivity {
    String reciverimg, reciverUid,reciverName,SenderUID;
    CircleImageView profile;
    TextView reciverNName;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    public  static String senderImg;
    public  static String reciverIImg;
    CardView sendbtn;
    EditText textmsg;

    String senderRoom,reciverRoom;
    RecyclerView messageAdpter;
    ArrayList<Message> messagesArrayList;
    Messages mmessages;
    ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatwindow);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        reciverName = getIntent().getStringExtra("nameeee");
        reciverimg = getIntent().getStringExtra("reciverImg");
        reciverUid = getIntent().getStringExtra("uid");
        messagesArrayList = new ArrayList<>();
        sendbtn = findViewById(R.id.sendbtnn);
        textmsg = findViewById(R.id.textmsg);
        reciverNName = findViewById(R.id.recivername);
        profile = findViewById(R.id.profileimgg);
        messageAdpter = findViewById(R.id.msgadpter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageAdpter.setLayoutManager(linearLayoutManager);
        mmessages = new Messages(chatWindow.this,messagesArrayList);
        messageAdpter.setAdapter(mmessages);
        Picasso.get().load(reciverimg).into(profile);
        reciverNName.setText(""+reciverName);
        SenderUID =  firebaseAuth.getUid();
        senderRoom = SenderUID+reciverUid;
        reciverRoom = reciverUid+SenderUID;
        backButton = findViewById(R.id.backButton);

        DatabaseReference  reference = database.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference  chatreference = database.getReference().child("chats").child(senderRoom).child("messages");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start MainActivity
                Intent intent = new Intent(chatWindow.this, MainActivity.class);
                startActivity(intent);
                finish(); // Optional: finish current activity to prevent coming back to it with back button
            }
        });
        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Message messages = dataSnapshot.getValue(Message.class);
                    messagesArrayList.add(messages);
                }
                mmessages.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderImg= snapshot.child("profilepic").getValue().toString();
                reciverIImg=reciverimg;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = textmsg.getText().toString();

                if (message.isEmpty()){
                    Toast.makeText(chatWindow.this, "Enter The Message First", Toast.LENGTH_SHORT).show();
                    return;
                }
                textmsg.setText("");
                Date date = new Date();
                Message messagess = new Message(message,SenderUID,date.getTime());

                database=FirebaseDatabase.getInstance();
                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.getReference().child("chats")
                                        .child(reciverRoom)
                                        .child("messages")
                                        .push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                            }
                        });
            }
        }); */

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = textmsg.getText().toString();

                if (message.isEmpty()) {
                    Toast.makeText(chatWindow.this, "Enter The Message First", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    // Encrypt the message
                    String encryptedMessage = EncryptionHelper.encrypt(message);
                    textmsg.setText("");
                    Date date = new Date();
                    Message messagess = new Message(encryptedMessage, SenderUID, date.getTime());

                    // Firebase operations
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference messageRef = database.getReference().child("chats").child(senderRoom).child("messages");
                    messageRef.push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // Duplicate for the receiver room
                            database.getReference().child("chats").child(reciverRoom).child("messages")
                                    .push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // Optional: Implement if needed
                                        }
                                    });
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(chatWindow.this, "Error encrypting message: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}