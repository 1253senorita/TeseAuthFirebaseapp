package com.salsample.teseauthfirebaseapp



import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@SuppressLint("CustomSplashScreen") // 전용 스플래시이므로 경고 무시
class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash) // 레이아웃이 있다면 설정

        // setContentView(R.layout.activity_splash) // 레이아웃이 있다면 설정


        auth = Firebase.auth

        // 일정 시간 후 또는 작업 완료 후 화면 전환
        Handler(Looper.getMainLooper()).postDelayed({
            checkUserStatus()
        }, 2000) // 2초 딜레이 (예시)
    }

    private fun checkUserStatus() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // 이미 로그인된 사용자: MainActivity로 이동
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // 로그인되지 않은 사용자: AuthActivity (로그인/등록)로 이동
            startActivity(Intent(this, AuthActivity::class.java))
        }
        finish() // 스플래시 액티비티 종료
    }
}