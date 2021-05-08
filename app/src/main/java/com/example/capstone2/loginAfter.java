package com.example.capstone2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class loginAfter extends AppCompatActivity {

    TextView nickText;
    Button addBtn;
    SwipeRefreshLayout mSwipeRefreshLayout;
    final ArrayList<PostInfo> postList = new ArrayList<>();
    RecyclerView recyclerView;
    PostAdapter adapter=new PostAdapter(postList);

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "loginAfter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_after);


        nickText = findViewById(R.id.nicktext);
        addBtn = findViewById(R.id.addbtn);
        mSwipeRefreshLayout = findViewById(R.id.swipe_layout);
        recyclerView = findViewById(R.id.recycler1);
        recyclerView.setLayoutManager(new LinearLayoutManager(loginAfter.this));



        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            nickText.setText(name + "님 환영합니다.");
        }


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(loginAfter.this, WritePostActivity.class);
                startActivity(intent);
            }
        });


        db.collection("post")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                postList.add(new PostInfo(document.getData().get("title").toString(),
                                        document.getData().get("contents").toString(),
                                        document.getData().get("nickName").toString(),
                                        new Date(document.getDate("createdAt").getTime())));

                                // 리사이클러뷰에 LinearLayoutManager 객체 지정.

                                // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
                                adapter.setPostAdapter(postList);
                                recyclerView.setAdapter(adapter);

                                adapter.setOnItemClickListener(new PostAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, int position) {
                                        startsignup(ContentsActivity.class, postList, position);
                                    }
                                });

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                db.collection("post")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    postList.clear();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        postList.add(new PostInfo(document.getData().get("title").toString(),
                                                document.getData().get("contents").toString(),
                                                document.getData().get("nickName").toString(),
                                                new Date(document.getDate("createdAt").getTime())));

                                        // 리사이클러뷰에 LinearLayoutManager 객체 지정.

                                        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
                                        adapter.setPostAdapter(postList);
                                        recyclerView.setAdapter(adapter);

                                        adapter.setOnItemClickListener(new PostAdapter.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(View v, int position) {
                                                startsignup(ContentsActivity.class, postList, position);
                                            }
                                        });

                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });


    }

    @Override
    public void onBackPressed() { //뒤로가기 버튼이 눌릴 때 전 엑티비로 이동하는 코드
        Intent intent = new Intent(loginAfter.this, MainActivity.class);
        startActivity(intent);
    }


    private void send() { // 고객이 입력한 email주소가 firebase Authentication에 존재 하는지 확인하고 패스워드 변경을 진행하는 메소드
        String uid = user.getUid();
        String name = user.getDisplayName();
        String email = user.getEmail();
        Toast.makeText(loginAfter.this, name + "/" + email + "/" + uid,
                Toast.LENGTH_SHORT).show();
    }


    private void startsignup(Class c, ArrayList<PostInfo> postList, int position) {  //회원 가입 화면으로 이동하는 함수
        Intent intent = new Intent(this, c);
        intent.putExtra("PostInfo", postList);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}
