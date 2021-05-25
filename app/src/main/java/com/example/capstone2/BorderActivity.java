package com.example.capstone2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import static com.example.capstone2.MainActivity.libraryList;

class Ascending implements Comparator<PostInfo>{  // 게시판 목록을 시간별로 정렬하는 코드
    public int compare(PostInfo a, PostInfo b)
    {
        return b.getCreatedAt().compareTo(a.getCreatedAt());
    }
}

public class BorderActivity extends AppCompatActivity {

    TextView libraryName;
    Button addBtn;
    int libraryListPosition;
    SwipeRefreshLayout mSwipeRefreshLayout;
    final ArrayList<PostInfo> postList = new ArrayList<>();
    RecyclerView recyclerView;
    PostAdapter adapter=new PostAdapter(postList);
    private LinearLayout rl;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "loginAfter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boarder);

        Intent intent = getIntent();  //리스트뷰에서 선택된 아이템을 position을 받아와서 해당하는 librarayList의 libraryName을 가져온다.
        libraryListPosition = (int)intent.getSerializableExtra("position");

        libraryName = findViewById(R.id.titleText);
        addBtn = findViewById(R.id.addbtn);
        mSwipeRefreshLayout = findViewById(R.id.swipe_layout);
        recyclerView = findViewById(R.id.recycler1);
        recyclerView.setLayoutManager(new LinearLayoutManager(BorderActivity.this));

        libraryName.setText(libraryList.get(libraryListPosition).getLibraryName()+" 게시판");

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BorderActivity.this, WritePostActivity.class);
                intent.putExtra("position",libraryListPosition);
                startActivity(intent);
            }
        });


        db.collection(libraryList.get(libraryListPosition).getLibraryName())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                postList.add(new PostInfo(document.getData().get("title").toString(),
                                        document.getData().get("contents").toString(),
                                        document.getData().get("nickName").toString(),
                                        document.getId(),
                                        new Date(document.getDate("createdAt").getTime())));

                                // 리사이클러뷰에 LinearLayoutManager 객체 지정.

                                // 리사이클러뷰에 SimpleTextAdapter 객체 지정.

                            }

                            adapter.setOnItemClickListener(new PostAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position) { //게시글이 눌렸을 때 ContentsActivity로 이동할 수 있게 리스너 장착
                                    startsignup(ContentsActivity.class, postList.get(position), position,libraryListPosition);
                                }
                            });

                            Collections.sort(postList,new Ascending());
                            adapter.setPostAdapter(postList);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() { //게시판을 당겼을 때 새로고침 되는 부분
                db.collection(libraryList.get(libraryListPosition).getLibraryName())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    postList.clear();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        postList.add(new PostInfo(document.getData().get("title").toString(),
                                                document.getData().get("contents").toString(),
                                                document.getData().get("nickName").toString(),
                                                document.getId().toString(),
                                                new Date(document.getDate("createdAt").getTime())));

                                        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.

                                    }
                                    adapter.setOnItemClickListener(new PostAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View v, int position) {
                                            startsignup(ContentsActivity.class, postList.get(position), position,libraryListPosition);
                                        }
                                    });

                                    Collections.sort(postList,new Ascending());
                                    adapter.setPostAdapter(postList);
                                    recyclerView.setAdapter(adapter);
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
    public boolean dispatchTouchEvent(MotionEvent ev) { // 키패드가 올라왔을 때 다른 화면을 터치하면 키보드가 사라지는 코드인데 실제로 작동하지 않음;;; 수정 필요
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }


    public void onBackPressed() { //뒤로가기 버튼이 눌릴 때 전 엑티비로 이동하는 코드
        Intent intent = new Intent(this, LibraryContentsActivity.class);
        intent.putExtra("position", libraryListPosition);
        startActivity(intent);
    }


    private void startsignup(Class c, PostInfo post, int position , int libraryListPosition) {  //다른 엑티비티 화면으로 이동하는 함수
        Intent intent = new Intent(this, c);
        intent.putExtra("PostInfo", post);
        intent.putExtra("position", position);
        intent.putExtra("libraryListPosition", libraryListPosition);
        startActivity(intent);
    }

}
