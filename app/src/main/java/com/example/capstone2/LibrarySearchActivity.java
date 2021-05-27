package com.example.capstone2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.capstone2.MainActivity.libraryList;

public class LibrarySearchActivity extends AppCompatActivity {
    private Button btnSearch;
    private Button btn_MyInfo;
    private Button btn_Logout;
    private EditText librarySearchEditText;
    private ListView listview;
    private List<String> list;
    private ImageView imageView;
    private ArrayList<String> arraylist;
    private SearchAdapter adapter;
    private FirebaseAuth authUI;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_librarysearch);
        list = new ArrayList<String>();
        imageView = findViewById(R.id.imageview);
        Glide.with(this).load(R.drawable.library).into(imageView);
        for (int i = 0; i < libraryList.size(); i++) {
            list.add(libraryList.get(i).getLibraryName());
        }

        btn_MyInfo = findViewById(R.id.btn_myinfo);
        btn_Logout = findViewById(R.id.btn_logout);

        arraylist = new ArrayList<String>();
        arraylist.addAll(list);

        adapter = new SearchAdapter(this,list);
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);


        librarySearchEditText = findViewById(R.id.librarysearchedittext);
        user = FirebaseAuth.getInstance().getCurrentUser();

        btnSearch = findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text = librarySearchEditText.getText().toString();
                if (text.length() > 0) {
                    listview.setFilterText(text);
                    InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                } else {
                    listview.clearTextFilter();
                }
            }
        });

        btn_MyInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (user != null) {
                    Intent intent = new Intent(LibrarySearchActivity.this, MyInfoActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LibrarySearchActivity.this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_Logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (user != null) {
                    authUI.getInstance()
                            .signOut();
                    Toast.makeText(LibrarySearchActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LibrarySearchActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LibrarySearchActivity.this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        librarySearchEditText.addTextChangedListener(new TextWatcher() {  //EditText가 변경될 때 마다 호출 되는 리스너
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {   // EditText 검색기능 추가
                String text = librarySearchEditText.getText().toString()
                        .toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }
        });

    }

    public void onBackPressed() { //뒤로가기 버튼이 눌릴 때 전 엑티비로 이동하는 코드
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}

