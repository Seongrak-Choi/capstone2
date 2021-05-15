package com.example.capstone2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.example.capstone2.MainActivity.libraryList;

public class LibrarySearchActivity extends AppCompatActivity {
    private Button btnSearch;
    private EditText librarySearchEditText;
    private ListView listview;
    private List<String> list;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_librarysearch);
        list = new ArrayList<String>();

        for (int i = 0; i < libraryList.size(); i++) {
            list.add(libraryList.get(i).getLibraryName());
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listview = (ListView) findViewById(R.id.listview1);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // 코드 계속 ...

            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) { // 리스트 아이템을 누르면 도서관 콘텐츠액티비티로 이동하면서 position을 넘겨준다
                // get TextView's Text.
                Intent intent = new Intent(LibrarySearchActivity.this, LibraryContentsActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });

        librarySearchEditText = findViewById(R.id.librarysearchedittext);
        btnSearch = findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text = librarySearchEditText.getText().toString();
                if (text.length() > 0) {
                    listview.setFilterText(text);
                } else {
                    listview.clearTextFilter();
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
                if (text.length() > 0) {
                    listview.setFilterText(text);
                } else {
                    listview.clearTextFilter();
                }
            }
        });

    }

}

