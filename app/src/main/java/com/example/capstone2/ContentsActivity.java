package com.example.capstone2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ContentsActivity extends AppCompatActivity {

    TextView titleText;
    TextView nickNameText;
    TextView dateText;
    TextView contentsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);

        titleText=findViewById(R.id.contents_xml_titleText);
        nickNameText=findViewById(R.id.contents_xml_nicknameText);
        dateText=findViewById(R.id.contents_xml_dateText);
        contentsText=findViewById(R.id.contents_xml_contentsText);

        Intent intent = getIntent();
        ArrayList<PostInfo> postList = (ArrayList<PostInfo>)intent.getSerializableExtra("PostInfo");
        int position = intent.getExtras().getInt("position");
        titleText.setText(postList.get(position).getTitle());
        nickNameText.setText(postList.get(position).getNickName());
        dateText.setText(postList.get(position).getCreatedAt().toString());
        contentsText.setText(postList.get(position).getContents());
    }
}