package com.salsample.teseauthfirebaseapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
// SplashActivity.kt
// ... (import 문 등은 동일)

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = Firebase.auth

        lifecycleScope.launch {
            delay(2000) // 2초 딜레이

            val userToReload = auth.currentUser
            if (userToReload != null) {
                userToReload.reload()?.addOnCompleteListener { task ->
                    // reload 성공 여부와 관계없이 현재 사용자 상태를 다시 확인하여 분기
                    // (reload 실패 시에도 로컬 캐시된 currentUser를 기반으로 동작할 수 있도록)
                    val refreshedCurrentUser = auth.currentUser // reload 후 최신 상태



                    if (refreshedCurrentUser != null) {
                        startActivity(Intent(this@SplashActivity, ActivityMain::class.java))
                        // startActivity(Intent(this@SplashActivity, ModernizeActivity::class.java)) // 필요시
                    } else {
                        startActivity(Intent(this@SplashActivity, AuthActivity::class.java))
                    }
                    finish() // SplashActivity 종료
                }
            } else {
                // reload 할 사용자가 없음 (처음부터 로그인 안 된 상태)
                startActivity(Intent(this@SplashActivity, AuthActivity::class.java))
                finish() // SplashActivity 종료
            }
        }
    }
}