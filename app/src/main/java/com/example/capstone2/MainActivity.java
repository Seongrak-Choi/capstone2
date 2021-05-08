package com.example.capstone2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity {
    Button btn_login;
    Button btn_signupgo;
    Button btn_forgot_pw;
    EditText idtext;
    EditText passwdtext;
    private FirebaseAuth mAuth;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_login = findViewById(R.id.btn_Login);
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
                Log.e("클릭", "클릭");
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
                                Intent intent = new Intent(MainActivity.this, loginAfter.class);
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
        finish();
    }

    private void startsignup() {  //회원 가입 화면으로 이동하는 함수
        Intent intent = new Intent(this, signup.class);
        startActivity(intent);
    }


}