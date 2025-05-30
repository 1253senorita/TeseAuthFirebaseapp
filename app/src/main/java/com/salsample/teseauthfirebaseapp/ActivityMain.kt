package com.salsample.teseauthfirebaseapp



import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ActivityMain : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    private lateinit var welcomeTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var uidTextView: TextView
    private lateinit var signOutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // activity_main.xml (또는 main_activity.xml) 연결

        // Firebase Auth 인스턴스 초기화
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser

        // UI 요소 초기화
        welcomeTextView = findViewById(R.id.welcomeTextView)
        emailTextView = findViewById(R.id.emailTextView)
        uidTextView = findViewById(R.id.uidTextView)
        signOutButton = findViewById(R.id.signOutButton)

        // 현재 사용자 정보 표시
        if (currentUser != null) {
            welcomeTextView.text = "환영합니다, ${currentUser?.displayName ?: "사용자"}님!"
            emailTextView.text = "이메일: ${currentUser?.email}"
            uidTextView.text = "UID: ${currentUser?.uid}"
        } else {
            // 사용자가 없는 경우 (이론적으로는 로그인 후 화면이므로 이 경우는 드묾)
            // 로그인 화면으로 다시 보내거나 앱을 종료할 수 있습니다.
            Toast.makeText(this, "사용자 정보를 불러올 수 없습니다. 다시 로그인해주세요.", Toast.LENGTH_LONG).show()
            // 예시: 로그인 액티비티로 이동 (LoginActivity.class는 실제 로그인 액티비티 이름으로 변경)
            // startActivity(Intent(this, LoginActivity::class.java))
            // finish()
        }

        // 로그아웃 버튼 리스너 설정
        signOutButton.setOnClickListener {
            signOutUser()
        }
    }

    private fun signOutUser() {
        auth.signOut()
        // 로그아웃 후 로그인 화면으로 이동
        // 예를 들어 LoginActivity가 로그인 화면이라면:
        // val intent = Intent(this, LoginActivity::class.java)
        // intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // 이전 액티비티 스택 클리어
        // startActivity(intent)
        Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
        finish() // 현재 액티비티 종료 (로그인 화면이 이전 스택에 있다면 그 화면으로 돌아감)
    }
}