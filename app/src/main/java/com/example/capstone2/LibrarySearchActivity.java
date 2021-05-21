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

        adapter = new SearchAdapter(list, this);
        listview = (ListView) findViewById(R.id.listview1);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // 코드 계속 ...

            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) { // 리스트 아이템을 누르면 도서관 콘텐츠액티비티로 이동하면서 position을 넘겨준다
                // get TextView's Text.
                Intent intent = new Intent(LibrarySearchActivity.this, LibraryContentsActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });


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


        listview.setAdapter(adapter);

        librarySearchEditText.addTextChangedListener(new TextWatcher() {  //EditText가 변경될 때 마다 호출 되는 리스너
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {   // EditText 검색기능 추가
                String text = librarySearchEditText.getText().toString();
                search(text);
            }
        });

    }

    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list.addAll(arraylist);
        }
        // 문자 입력을 할때..
        else {
            // 리스트의 모든 데이터를 검색한다.
            for (int i = 0; i < arraylist.size(); i++) {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist.get(i).toLowerCase().contains(charText)) {
                    // 검색된 데이터를 리스트에 추가한다.
                    list.add(arraylist.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }

    public void onBackPressed() { //뒤로가기 버튼이 눌릴 때 전 엑티비로 이동하는 코드
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}

