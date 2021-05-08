package com.example.capstone2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class forgot_pw extends AppCompatActivity {

    EditText emailText;
    Button btn_Ok;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pw);


        emailText = findViewById(R.id.emailtext);
        btn_Ok = findViewById(R.id.btn_ok);

        btn_Ok.setOnClickListener(new View.OnClickListener() { //전송버튼을 눌렀을 경우 실행될 부분
            public void onClick(View v) {
                send();
            }
        });

    }

    private void send() { // 고객이 입력한 email주소가 firebase Authentication에 존재 하는지 확인하고 패스워드 변경을 진행하는 메소드
        String emailAddress = emailText.getText().toString();
        if (emailAddress.length() > 0) {
            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //입력한 E-mail이 계정에 존재할 경우 실행되는 부분
                                AlertDialog.Builder dlg= new AlertDialog.Builder(forgot_pw.this); //알림창으로 메일이 전송되었음을 알려줌
                                dlg.setMessage("입력하신 E-mail로 메일을 전송하였습니다.");
                                dlg.setPositiveButton("확인",null);
                                dlg.show();
                            }
                            else{ //입력한 E-mail이 계정에 존재하지 않을 경우 실행되는 부분
                                Toast.makeText(forgot_pw.this, "입력하신 E-mail로는 회원가입되어 있지 않습니다.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(forgot_pw.this, "이메일을 입력해 주세요",
                    Toast.LENGTH_SHORT).show();
        }
    }


}