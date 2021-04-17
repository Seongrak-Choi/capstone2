package com.example.capstone2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_signup.setOnClickListener({
            val intent = Intent(this, signup::class.java)
            startActivity(intent)

        })
        //최성락 안드로이드 스튜디오에서 git연동 테스트 중입니다.
        //박정민 안드로이드 스튜디오에서 git연동 테스트 중입니다.
        //윤유일 안드로이드 스튜디오에서 git연동 테스트 중입니다.
    }
}
