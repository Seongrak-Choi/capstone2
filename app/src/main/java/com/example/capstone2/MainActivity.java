package com.example.capstone2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


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
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        libraryList.clear();
        db.collection("library")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                libraryList.add(new LibraryInfo(document.getData().get("libraryName").toString(),
                                        document.getData().get("libraryCode").toString(),
                                        document.getData().get("librarySeatUrl").toString()));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        btn_login = findViewById(R.id.btn_Login);
        btnNoLogin = findViewById(R.id.btnnologin);
        idtext = findViewById(R.id.idText);
        passwdtext = findViewById(R.id.passwdText);
        mAuth = FirebaseAuth.getInstance();
        btn_signupgo = findViewById(R.id.btn_signup);
        btn_signupgo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { //???????????? ????????? ????????? ???????????? ???????????? ??????
                startsignup();
                Log.e("??????", "??????");
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
                                // ????????? ?????? ??? ????????? ??????
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(MainActivity.this, "????????? ??????",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, LibrarySearchActivity.class);
                                startActivity(intent);
                            } else {
                                // ????????? ????????? ??? ????????? ??????
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                //System.out.println("?????????: "+task.getException()+"????????????");
                                if (task.getException().toString().equals("com.google.firebase.auth.FirebaseAuthInvalidUserException: There is no user record corresponding to this identifier. The user may have been deleted.")) {
                                    Toast.makeText(MainActivity.this, "???????????? ???????????? ????????????.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, task.getException().toString(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        } else {
            Toast.makeText(MainActivity.this, "????????? ?????? ??????????????? ????????? ?????????",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() { //???????????? ????????? ?????? ??? ??? ???????????? ???????????? ??????
        ActivityCompat.finishAffinity(this);
        System.exit(0);
    }

    private void startsignup() {  //?????? ?????? ???????????? ???????????? ??????
        Intent intent = new Intent(this, signup.class);
        startActivity(intent);
    }

}