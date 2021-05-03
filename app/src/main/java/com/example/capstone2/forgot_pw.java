package com.example.capstone2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

        btn_Ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                send();
            }
        });

    }

    private void send() {
        String emailAddress = emailText.getText().toString();
        if (emailAddress.length() > 0) {
            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(forgot_pw.this, "이메일을 전송했습니다.",
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