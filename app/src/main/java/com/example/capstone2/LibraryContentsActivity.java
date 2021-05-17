package com.example.capstone2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import static com.example.capstone2.MainActivity.libraryList;

public class LibraryContentsActivity extends AppCompatActivity {

    Button goBookSearch;
    Button goBoard;
    Button btn_Seat;
    Button btn_useInformation;
    TextView libraryTitle;
    int libraryListPosition;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_librarycontent);

        goBookSearch = findViewById(R.id.btn_gobooksearch);
        goBoard = findViewById(R.id.btn_goboard);
        libraryTitle = findViewById(R.id.librarytitle);
        btn_Seat = findViewById(R.id.btn_seat);
        btn_useInformation = findViewById(R.id.btn_useinformation);

        goBookSearch.setOnClickListener(onClickListener);
        goBoard.setOnClickListener(onClickListener);
        btn_Seat.setOnClickListener(onClickListener);
        btn_useInformation.setOnClickListener(onClickListener);

        Intent intent = getIntent();  //리스트뷰에서 선택된 아이템을 position을 받아와서 해당하는 librarayList의 libraryName을 가져온다.
        libraryListPosition = (int) intent.getSerializableExtra("position");
        libraryTitle.setText(libraryList.get(libraryListPosition).getLibraryName());


    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_gobooksearch:
                    movePage(BookSearchActivity.class, libraryListPosition);
                    break;
                case R.id.btn_goboard:
                    if (user != null) {
                        movePage(BorderActivity.class, libraryListPosition);
                    } else {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(LibraryContentsActivity.this); //알림창으로 메일이 전송되었음을 알려줌
                        dlg.setMessage("게시판을 이용하시려면 로그인을 해야 합니다.");
                        dlg.setPositiveButton("확인", null);
                        dlg.show();
                    }
                    break;
                case R.id.btn_seat:
                    movePage(SeatActivity.class, libraryListPosition);
                    break;
                case R.id.btn_useinformation:
                    movePage(LibraryInformationActivity.class, libraryListPosition);
                    break;
            }
        }
    };

    public void onBackPressed() { //뒤로가기 버튼이 눌릴 때 전 엑티비로 이동하는 코드
        Intent intent = new Intent(this, LibrarySearchActivity.class);
        startActivity(intent);
    }


    private void movePage(Class c, int libraryListPositionPosition) {  // 액티비티를 이동시켜주는 메소드
        Intent intent = new Intent(this, c);
        intent.putExtra("position", libraryListPositionPosition);
        startActivity(intent);
    }

}
