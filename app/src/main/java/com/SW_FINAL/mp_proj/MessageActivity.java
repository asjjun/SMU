package com.SW_FINAL.mp_proj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    private RecyclerView lv_chating;
    private EditText et_send;
    private Button btn_send;
    //private ArrayAdapter<String> arrayAdapter;
    //private ArrayList<String> arr_room = new ArrayList<>();
    ArrayList<MessageInfo> arrayList;
    MessageAdapter messageAdapter;
    private String str_user_name;
    private DatabaseReference reference, roomNameRef;

    private String chat_user;
    private String chat_message;
    Intent intent;
    String roomName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        getSupportActionBar().setElevation(0);
        et_send = (EditText) findViewById(R.id.et_send);
        lv_chating = (RecyclerView) findViewById(R.id.lv_chating);
        btn_send = (Button) findViewById(R.id.btn_send);
        arrayList = new ArrayList<MessageInfo>();
        messageAdapter = new MessageAdapter(arrayList);
        intent = getIntent();
        lv_chating.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        roomNameRef = FirebaseDatabase.getInstance().getReference("chat");
        roomNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    String  name  = snap.getKey();
                    if( Storage.MyroomName.equals(name))
                    {
                        roomName = name;
                    }

                }

                reference = FirebaseDatabase.getInstance().getReference("chat").child(roomName).child("comments");
                reference.addChildEventListener(new ChildEventListener() {
                    @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        chatConversation(dataSnapshot);
                    }

                    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        chatConversation(dataSnapshot);
                    }

                    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        str_user_name = Storage.MyName;


        lv_chating.setAdapter(messageAdapter);
        // ??????????????? ???????????? ???????????? ?????? ?????????
        //lv_chating.setTranscriptMode(RecyclerView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {

                // map??? ????????? name??? ???????????? ????????????, key??? ??? ??????
                Map<String, Object> map = new HashMap<String, Object>();

                reference.push().updateChildren(map);


                // updateChildren??? ???????????? database ?????? ????????????
                Map<String, Object> objectMap = new HashMap<String, Object>();
                objectMap.put("name", str_user_name);
                objectMap.put("message", et_send.getText().toString());

                reference.push().updateChildren(objectMap);

                et_send.setText("");
            }
        });



    }

    // addChildEventListener??? ?????? ?????? ????????????????????? ????????? ?????? ?????????,
    // ????????? ???????????? ?????? Listview??? ?????? ?????????
    private void chatConversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()) {
            chat_message = (String) ((DataSnapshot) i.next()).getValue();
            chat_user = (String) ((DataSnapshot) i.next()).getValue();
            MessageInfo message = new MessageInfo(chat_user, chat_message);
            arrayList.add(message);
        }


        messageAdapter.notifyDataSetChanged();
    }
}