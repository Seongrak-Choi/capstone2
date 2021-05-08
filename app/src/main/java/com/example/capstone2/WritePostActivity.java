package com.example.capstone2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;


public class WritePostActivity extends AppCompatActivity {

    final static String TAG = "WritePostActivity";
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrtie_post);

        Button checkBtn = findViewById(R.id.checkbtn);

        checkBtn.setOnClickListener(onClickListener);

    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.checkbtn:
                    profileUpdate();
                    finish();
                    break;
            }
        }
    };


    public void profileUpdate() {
        final String title = ((EditText) findViewById(R.id.titleEditText)).getText().toString();
        final String contents = ((EditText) findViewById(R.id.contentsEditText)).getText().toString();

        if (title.length() > 0 && contents.length() > 0) {
            user = FirebaseAuth.getInstance().getCurrentUser();
            WriteInfo wrtieInfo = new WriteInfo(title, contents, user.getDisplayName(), new Date());
            uploader(wrtieInfo);

        }

    }

    private void uploader(WriteInfo writeInfo) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("post")
                .add(writeInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(WritePostActivity.this, "업로드 성공", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        startsignup(loginAfter.class);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(WritePostActivity.this, "업로드 실패", Toast.LENGTH_SHORT).show();
                        System.out.println(e.toString());
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void startsignup(Class c) {  //원하는 activity로 이동하는 함수
        Intent intent = new Intent(this, c);
        startActivityForResult(intent,0);
    }
}
