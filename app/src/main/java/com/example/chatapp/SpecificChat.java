package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class SpecificChat extends AppCompatActivity {

    EditText mgetmessage;

    ImageButton msendmeaagebtn;

    CardView msendmessagecarddview;
    androidx.appcompat.widget.Toolbar mtoolbarspicificchat;
    ImageView mimageviewodspecificuser;
    TextView mnameofspecificuser, mlastseen;

    private String enteredmessage;
    Intent intent;
    String mreceivername, sendername, mreceiveruid, msenderuid;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String senderroom, receiverroom;

    ImageButton mbackbuttonspecific;

    RecyclerView mmessagerrecyclerview;

    String currenttime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;


    String myUid;

    MessageAdapter messageAdapter;
    ArrayList<Message> messageArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_chat);


       mgetmessage = findViewById(R.id.getmessage);
       msendmeaagebtn = findViewById(R.id.imageviewsendmessage);
       msendmessagecarddview = findViewById(R.id.cardviewofsendmessage);
        mlastseen = findViewById(R.id.latseenofspecificuser);

       mtoolbarspicificchat = findViewById(R.id.toolbarofspecificchat);
       mnameofspecificuser = findViewById(R.id.nameofspecificuser);
       mimageviewodspecificuser = findViewById(R.id.specificimageinimageview);
       mbackbuttonspecific = findViewById(R.id.backbuttonofspecificchat);

        messageArrayList = new ArrayList<>();
        mmessagerrecyclerview = findViewById(R.id.recyclerviewofspecific);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        mmessagerrecyclerview.setLayoutManager(linearLayoutManager);
        messageAdapter = new MessageAdapter(SpecificChat.this, messageArrayList);
        mmessagerrecyclerview.setAdapter(messageAdapter);


         intent = getIntent();

         setSupportActionBar(mtoolbarspicificchat);
         mtoolbarspicificchat.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Toast.makeText(getApplicationContext(), "Toolbar is clicked", Toast.LENGTH_SHORT).show();
             }
         });


         firebaseAuth = FirebaseAuth.getInstance();
         firebaseDatabase = FirebaseDatabase.getInstance();
         calendar = Calendar.getInstance();
         simpleDateFormat = new SimpleDateFormat("hh:mm a");


         msenderuid = firebaseAuth.getUid();
        mreceivername = getIntent().getStringExtra("name");
        mreceiveruid = getIntent().getStringExtra("receiveruid");











         senderroom = msenderuid+mreceiveruid;
         receiverroom = mreceiveruid+msenderuid;


         //String onlineStatus = ""+firebaseDatabase.getReference().child("onlineStatus").get();

        /**String status = "online"+snapshot1.child("status").getValue();
         //String onlineStatus = intent.getStringExtra("status");
         if(status.equals("online"))
         {
         mlastseen.setText("online");

         }
         else
         {
         //convert timestamp to proper time date

         Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
         calendar.setTimeInMillis(Long.parseLong(status));
         String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
         mlastseen.setText("Last seen at:"+ dateTime);


         /**FirebaseDatabase database = FirebaseDatabase.getInstance();
         DatabaseReference lastSeenData = database.getReference("message");
         DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy ");
         Date date = new Date();
         lastSeenData.setValue(dateFormat.format(date));
         mlastseen.setText("Last seen at:" +date);


         }**/


















        DatabaseReference databaseReference = firebaseDatabase.getReference().child("chats")
                .child(senderroom).child("message");
        messageAdapter = new MessageAdapter(SpecificChat.this, messageArrayList);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageArrayList.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    Message message = snapshot1.getValue(Message.class);
                    messageArrayList.add(message);


                    long timeFromServer = message.getTimestamp();

                    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                    calendar.setTimeInMillis(timeFromServer );
                    long tes = calendar.getTimeInMillis();
                    DateFormat.format("yyyy/MM/dd HH:mm:ss", calendar);
                    CharSequence now = DateUtils.getRelativeTimeSpanString(tes, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);


                    String status = ""+firebaseDatabase.getReference().child("status").get();

                    if(status.equals("online"))
                    {
                        mlastseen.setText("Online");
                    }
                    else
                    {
                        mlastseen.setText("Last seen at:"+now);
                    }


                }
                messageAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mbackbuttonspecific.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //for image means dp
        mnameofspecificuser.setText(mreceivername);
        String uri = intent.getStringExtra("imageuri");
        if(uri.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "null is received", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Picasso.get().load(uri).into(mimageviewodspecificuser);
        }


        msendmeaagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredmessage = mgetmessage.getText().toString();

                if(enteredmessage.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Enter Message first", Toast.LENGTH_SHORT).show();
                }

                else{
                    Date date = new Date();
                    currenttime = simpleDateFormat.format(calendar.getTime());
                    Message message = new Message(enteredmessage, firebaseAuth.getUid(), date.getTime(), currenttime);
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference().child("chats")
                            .child(senderroom).child("message").push()
                            .setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            firebaseDatabase.getReference().child("chats").child(receiverroom)
                                    .child("message").push()
                                    .setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                }
                            });

                        }
                    });

                    mgetmessage.setText(null);
                }
            }
        });




    }






    @Override
    protected void onStart() {
        super.onStart();

       messageAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onStop() {
        super.onStop();

        if(messageAdapter!=null)
        {
            messageAdapter.notifyDataSetChanged();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        //get  timestamp
        //String timestamp = String.valueOf(System.currentTimeMillis());
        //set offline with last seen

    }


    @Override
    protected void onResume() {
        super.onResume();

    }
}