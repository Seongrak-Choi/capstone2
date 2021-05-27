package com.example.capstone2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.w3c.dom.Text;

public class MyInfoActivity extends AppCompatActivity {
    private FirebaseAuth authUI;
    private FirebaseUser user;
    private TextView txtChangeNickname;
    private TextView txtCurrentID;
    private EditText edtChangeNickname;
    private EditText edtChaangePasswd;
    private Button btnChangeNickname;
    private Button btnChangePasswd;
    private Button btnDelte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        txtChangeNickname = findViewById(R.id.txt_currentnickname);
        txtCurrentID = findViewById(R.id.txt_currentID);
        edtChangeNickname = findViewById(R.id.edt_changenickname);
        edtChaangePasswd = findViewById(R.id.edt_changepasswd);
        btnChangeNickname = findViewById(R.id.btn_changenickname);
        btnChangePasswd = findViewById(R.id.btn_changepasswd);
        btnDelte = findViewById(R.id.btn_delete);
        user = FirebaseAuth.getInstance().getCurrentUser();
        txtChangeNickname.setText(user.getDisplayName());
        txtCurrentID.setText(user.getEmail());

        btnChangeNickname.setOnClickListener(onClickListener);
        btnChangePasswd.setOnClickListener(onClickListener);
        btnDelte.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_changenickname:
                    if (edtChangeNickname.getText().toString().length() > 0) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(edtChangeNickname.getText().toString())
                                .build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = getIntent();
                                            finish();
                                            startActivity(intent);
                                            Toast.makeText(MyInfoActivity.this, "닉네임이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(MyInfoActivity.this, "변경하실 닉네임을 입력하세요", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_changepasswd:
                    if (edtChaangePasswd.getText().toString().length() > 0) {
                        user.updatePassword(edtChaangePasswd.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(MyInfoActivity.this, "패스워드가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(MyInfoActivity.this, "변경하실 패스워드를 입력하세요", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_delete:
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MyInfoActivity.this);
                    alert_confirm.setMessage("정말 계정을 삭제 할까요?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    authUI.getInstance()
                                            .signOut();
                                    user.delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(MyInfoActivity.this, "계정이 삭제 되었습니다.", Toast.LENGTH_LONG).show();
                                                    finish();
                                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                }
                                            });
                                }
                            }
                    );
                    alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MyInfoActivity.this, "취소", Toast.LENGTH_LONG).show();
                        }
                    });
                    alert_confirm.show();

            }
        }
    };
}
