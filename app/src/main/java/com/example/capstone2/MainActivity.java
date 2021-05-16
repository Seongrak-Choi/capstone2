package com.example.capstone2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    static ArrayList<LibraryInfo> libraryList = new ArrayList<>();
    Button btn_login;
    Button btn_signupgo;
    Button btnNoLogin;
    Button btn_forgot_pw;
    EditText idtext;
    EditText passwdtext;
    private FirebaseAuth mAuth;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        libraryList.add(new LibraryInfo("서울도서관",111314,"N"));
        libraryList.add(new LibraryInfo("고양시립대화도서관",141139,"https://dpds10.goyanglib.or.kr:44443/dh/"));
        libraryList.add(new LibraryInfo("고양시립덕이도서관",141398,"https://dpds.goyanglib.or.kr/SeatMate3_di/SeatMate.php"));
        libraryList.add(new LibraryInfo("고양시립마두도서관",141055,"N"));
        libraryList.add(new LibraryInfo("고양시립백석도서관",141073,"https://dpds.goyanglib.or.kr/seatmate4_bs/seatmate.php"));
        libraryList.add(new LibraryInfo("강서구립가양도서관",111453,"http://222.237.254.8:8800/seatmate/SeatMate.php"));
        libraryList.add(new LibraryInfo("광진정보도서관 ",111036,"http://125.140.75.102:8800/seatmate/seatmate.php"));
        libraryList.add(new LibraryInfo("고척도서관 ",111008,"http://gclib-seat.sen.go.kr/domian5.php"));
        libraryList.add(new LibraryInfo("도봉문화정보도서관",111041,"http://61.102.210.254/seatmate/seatmate.php"));
        libraryList.add(new LibraryInfo("동대문도서관 ",111012,"http://218.48.162.173/domian5.php"));
        libraryList.add(new LibraryInfo("동작도서관 ",111013,"http://djlib-seat.sen.go.kr/domian5.php"));
        libraryList.add(new LibraryInfo("성동구립도서관 ",111035,"http://220.72.218.152:8800/seatmate/SeatMate.php"));



        btn_login = findViewById(R.id.btn_Login);
        btnNoLogin=findViewById(R.id.btnnologin);
        idtext = findViewById(R.id.idText);
        passwdtext = findViewById(R.id.passwdText);
        mAuth = FirebaseAuth.getInstance();
        btn_signupgo = findViewById(R.id.btn_signup);
        btn_signupgo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { //회원가입 버튼을 클릭시 회원가입 화면으로 이동
                startsignup();
                Log.e("클릭", "클릭");
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checksign();
            }
        });

        btnNoLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LibrarySearchActivity.class);
                startActivity(intent);
            }
        });

        btn_forgot_pw = findViewById(R.id.btn_forgot_pw);
        btn_forgot_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, forgot_pw.class);
                startActivity(intent);
            }
        });
    }

    private void checksign() {
        if (idtext.length() > 0 && passwdtext.length() > 0) {
            String email = idtext.getText().toString();
            String password = passwdtext.getText().toString();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // 로그인 성공 시 수행될 작업
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(MainActivity.this, "로그인 성공",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, LibrarySearchActivity.class);
                                startActivity(intent);
                            } else {
                                // 로그인 실패할 시 수행될 작업
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(MainActivity.this, "이메일 또는 비밀번호를 입력해 주세요",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() { //뒤로가기 버튼이 눌릴 때 전 엑티비로 이동하는 코드
        ActivityCompat.finishAffinity(this);
        System.exit(0);
    }

    private void startsignup() {  //회원 가입 화면으로 이동하는 함수
        Intent intent = new Intent(this, signup.class);
        startActivity(intent);
    }

}