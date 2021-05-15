package com.example.capstone2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import static com.example.capstone2.MainActivity.libraryList;

public class LibraryContentsActivity extends AppCompatActivity {

    Button goBookSearch;
    Button goBoard;
    TextView libraryTitle;
    int libraryListPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_librarycontent);

        goBookSearch=findViewById(R.id.btn_gobooksearch);
        goBoard=findViewById(R.id.btn_goboard);
        libraryTitle=findViewById(R.id.librarytitle);

        goBookSearch.setOnClickListener(onClickListener);
        goBoard.setOnClickListener(onClickListener);

        Intent intent = getIntent();  //리스트뷰에서 선택된 아이템을 position을 받아와서 해당하는 librarayList의 libraryName을 가져온다.
        libraryListPosition = (int)intent.getSerializableExtra("position");
        libraryTitle.setText(libraryList.get(libraryListPosition).getLibraryName());
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_gobooksearch:
                    movePage(BookSearchActivity.class,libraryListPosition);
                    break;
                case R.id.btn_goboard:
                    movePage(BorderActivity.class,libraryListPosition);
                    break;
            }
        }
    };

    private void movePage(Class c,int libraryListPositionPosition) {  // 액티비티를 이동시켜주는 메소드
        Intent intent = new Intent(this, c);
        intent.putExtra("position",libraryListPositionPosition);
        startActivity(intent);
    }

}
