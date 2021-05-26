package com.example.capstone2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.capstone2.MainActivity.libraryList;

public class ContentsActivity extends AppCompatActivity {

    TextView titleText;
    TextView nickNameText;
    TextView dateText;
    TextView contentsText;
    EditText edt_Comment;
    Button btn_CommentWrite;
    final String TAG = "tag";

    private FirebaseUser user;

    PostInfo post = new PostInfo();
    RecyclerView recyclerView;
    ArrayList<CommentInfo> commentList = new ArrayList<>();
    CommentAdapter adapter=new CommentAdapter(commentList);
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    class Ascending implements Comparator<CommentInfo> {  // 게시판 목록을 시간별로 정렬하는 코드
        public int compare(CommentInfo b, CommentInfo a)
        {
            return b.getCreatedAt().compareTo(a.getCreatedAt());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.postmenu,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);
        btn_CommentWrite = findViewById(R.id.btn_commentwrite);
        titleText = findViewById(R.id.contents_xml_titleText);
        nickNameText = findViewById(R.id.contents_xml_nicknameText);
        dateText = findViewById(R.id.contents_xml_dateText);
        contentsText = findViewById(R.id.contents_xml_contentsText);
        edt_Comment = findViewById(R.id.edt_comment);

        Intent intent = getIntent();
        post = (PostInfo) intent.getSerializableExtra("PostInfo");
        int position = intent.getExtras().getInt("position");
        final int libraryListPosition = intent.getExtras().getInt("libraryListPosition");
        titleText.setText(post.getTitle());
        nickNameText.setText(post.getNickName());
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm");
        String date = sdf.format(post.getCreatedAt());
        dateText.setText(date);
        contentsText.setText(post.getContents());
        recyclerView = findViewById(R.id.recycler4);
        recyclerView.setLayoutManager(new LinearLayoutManager(ContentsActivity.this));

        db.collection(libraryList.get(libraryListPosition).getLibraryName())
                .document(post.getDocumentValue())
                .collection("comment")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                commentList.add(new CommentInfo(document.getData().get("contents").toString(),
                                        document.getData().get("nickName").toString(),
                                        document.getData().get("uID").toString(),
                                        new Date(document.getDate("createdAt").getTime())));
                            }
                            Collections.sort(commentList,new Ascending());
                            adapter.setCommentAdapter(commentList);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                });




        btn_CommentWrite.setOnClickListener(new View.OnClickListener() {   ///작성한 댓글을 db에 올리는 작업
            @Override
            public void onClick(View view) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                String comments = edt_Comment.getText().toString();
                String nickName = user.getDisplayName();
                String uID = user.getUid();
                CommentInfo commentUpload = new CommentInfo(comments,nickName,uID,new Date());
                db.collection(libraryList.get(libraryListPosition).getLibraryName())
                        .document(post.getDocumentValue())
                        .collection("comment")
                        .add(commentUpload)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference){
                                edt_Comment.setText("");
                                commentList.clear();

                                db.collection(libraryList.get(libraryListPosition).getLibraryName()) ///댓글을 작성하면서 서버에서 댓글을 다시 불러와서 리사이클뷰에 출력해 준다.
                                        .document(post.getDocumentValue())
                                        .collection("comment")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        commentList.add(new CommentInfo(document.getData().get("contents").toString(),
                                                                document.getData().get("nickName").toString(),
                                                                document.getData().get("uID").toString(),
                                                                new Date(document.getDate("createdAt").getTime())));
                                                    }
                                                    Collections.sort(commentList,new Ascending());
                                                    adapter.setCommentAdapter(commentList);
                                                    recyclerView.setAdapter(adapter);
                                                }
                                            }
                                        });
                            }
                        });
            }
        });

    }
}

