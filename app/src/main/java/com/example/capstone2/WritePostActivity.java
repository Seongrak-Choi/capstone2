package com.example.capstone2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import static com.example.capstone2.MainActivity.libraryList;


public class WritePostActivity extends AppCompatActivity {

    final static String TAG = "WritePostActivity";
    private FirebaseUser user;
    int libraryListPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrtie_post);
        Button checkBtn = findViewById(R.id.checkbtn);
        checkBtn.setOnClickListener(onClickListener);

        Intent intent = getIntent();  //리스트뷰에서 선택된 아이템을 position을 받아와서 해당하는 librarayList의 libraryName을 가져온다.
        libraryListPosition = (int)intent.getSerializableExtra("position");

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


    public void profileUpdate() { //작성한 글에 계정 닉네임과 작성한 시간을 writeInfo 객체로 생성 후 uploader메소드로 넘겨주는 메소드
        final String title = ((EditText) findViewById(R.id.titleEditText)).getText().toString();
        final String contents = ((EditText) findViewById(R.id.contentsEditText)).getText().toString();

        if (title.length() > 0 && contents.length() > 0) { //제목이나 내용에 아무런 내용이 없다면 업로드 되지 않게 하는  if문
            user = FirebaseAuth.getInstance().getCurrentUser();
            WriteInfo writeInfo = new WriteInfo(title, contents, user.getDisplayName(), new Date());
            uploader(writeInfo);
        }

    }

    private void uploader(WriteInfo writeInfo) { //서버 db에 작성한 내용을 업로드하는 메소드
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(libraryList.get(libraryListPosition).getLibraryName())
                .add(writeInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(WritePostActivity.this, "업로드 성공", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        startsignup(BorderActivity.class);
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
        intent.putExtra("position",libraryListPosition);
        startActivity(intent);
    }
}
