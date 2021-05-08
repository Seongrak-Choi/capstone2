package com.example.capstone2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
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
import com.google.firebase.auth.UserProfileChangeRequest;

public class signup extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button btn_Signup;
    EditText idtext;
    EditText passwdtext;
    EditText passwdchecktext;
    EditText nickName;
    private static final String TAG = "SignupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        idtext = findViewById(R.id.idText);
        passwdtext = findViewById(R.id.passwdText);
        passwdchecktext = findViewById(R.id.passwdCheckText);
        nickName=findViewById(R.id.nickname);
        btn_Signup = findViewById(R.id.btn_signup);
        btn_Signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signup();
                Log.e("클릭", "클릭");
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // reload();
        }
    }

    private void signup() {
        String email = idtext.getText().toString();
        String password = passwdtext.getText().toString();
        String passwordcheck = passwdchecktext.getText().toString();

        if (email.length() > 0 && password.length() > 0 && passwordcheck.length() > 0 && nickName.length()>0)
            if (password.equals(passwordcheck)) { // 비밀번호와 비밀번호확인이 일치한지 묻는 if문
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) { //회원가입이 성공적으로 되었으면 진행되는 부분
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    user = FirebaseAuth.getInstance().getCurrentUser(); // 닉네임 등록하는 부분 76~92
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(nickName.getText().toString())
                                            .build();
                                    if (user != null) {
                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d(TAG, "User profile updated.");
                                                        }
                                                    }
                                                });
                                    } else {
                                        startToast("로그인 되어 있지 않습니다.");
                                    }

                                    startToast("회원가입에 성공하였습니다.");
                                    finish();
                                } else { //회원가입이 실패했을 경우 진행되는 부분
                                    // If sign in fails, display a message to the user.
                                    if (task.getException() != null) // 실패한 이유가 null이 아닐 경우 실행되는 부분
                                        startToast(task.getException().toString()); // 실패원인을 토스트 메세지로 출력해 준다.
                                }
                            }
                        });
            } else {
                startToast("비밀번호가 일치하지 않습니다.");
            }
        else {
            startToast("이메일과 비밀번호 또는 닉네임을 입력해 주세요");
        }
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}
