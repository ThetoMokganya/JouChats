package com.illinodes.jouchats;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.illinodes.jouchats.Adapter.MessageAdapter;
import com.illinodes.jouchats.Model.Chat;
import com.illinodes.jouchats.Model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity
{
    //Declarations
    CircleImageView profile_image;
    private TextView username;

    FirebaseUser fuser;
    DatabaseReference reference;

    private ImageButton btn_send;
    private EditText text_send;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    RecyclerView recyclerView;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //Show Toolbar and set Title of Page
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MessageActivity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);

        //Send user message
        intent = getIntent();
        final String userid = intent.getStringExtra("userid");
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        btn_send.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String msg = text_send.getText().toString();

                if (!msg.equals(""))
                {
                    sendMessage(fuser.getUid(), userid, msg);
                }else
                    {
                        Toast.makeText(MessageActivity.this,
                                "You can't send an empty message",
                                Toast.LENGTH_SHORT).show();
                    }
                text_send.setText("");
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());

                if (user.getImageURL().equals("default"))
                {
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                } else
                    {
                        Glide.with(MessageActivity.this)
                                .load(user.getImageURL())
                                .into(profile_image);
                    }
                readMessage(fuser.getUid(), userid, user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        });
    }

    private void sendMessage(String sender, String receiver, String message)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);


        reference.child("Chats").push().setValue(hashMap);
    }

    private void readMessage(final String myid, final String userid, final String imageurl)
    {
        mChat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid)
                            && chat.getSender().equals(userid)
                            || chat.getReceiver().equals(userid)
                            && chat.getSender().equals(myid))
                    {
                        mChat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this,
                            mChat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        });
    }

    //Checks user's status whether online or offline
    public void Status(String status)
    {
        reference = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(fuser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Status("Online");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Status("Offline");
    }
}
