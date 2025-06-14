package com.salsample.teseauthfirebaseapp


import android.content.Intent
import android.os.Bundle
import android.widget.Button // Button 임포트 확인
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
    private lateinit var firestoreExampleButton: Button
    private lateinit var registerEmailButton: Button // << 새로운 버튼 선언 추가

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser

        if (currentUser == null) {
            Toast.makeText(this, "로그인이 필요합니다. 로그인 화면으로 이동합니다.", Toast.LENGTH_LONG).show()
            // 로그인 화면으로 즉시 이동시키려면 여기서 RegisterActivity를 시작할 수도 있습니다.
            // 또는 별도의 LoginActivity를 만들어 그곳으로 보낼 수도 있습니다.
            // 여기서는 RegisterActivity가 로그인/회원가입을 모두 처리한다고 가정합니다.
            startActivity(Intent(this, RegisterActivity::class.java)) // 로그인/회원가입 화면으로 이동
            finish()
            return
        }

        // --- 자동 화면 전환 로직 시작 ---
        // Firestore 액티비티 자동 전환 로직은 그대로 두거나, 필요에 따라 수정합니다.
        // 현재는 로그인만 되어있으면 무조건 FirestoreActivity로 가려고 합니다.
        // 이 로직을 주석 처리하거나 false로 하면 ActivityMain의 UI가 보이게 됩니다.
        val autoStartFirestore = false // false로 변경하여 ActivityMain UI를 먼저 보도록 함 (테스트 목적)

        if (autoStartFirestore) {
            try {
                val intent = Intent(this, Class.forName("com.teminator.feature_firestore.FirestoreActivity"))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
                return
            } catch (e: ClassNotFoundException) {
                Toast.makeText(this, "주요 기능 파이어 스토어 화면을 찾을 수 없습니다. 일반 메인 화면을 표시합니다.", Toast.LENGTH_LONG).show()
            }
        }
        // --- 자동 화면 전환 로직 끝 ---

        setContentView(R.layout.activity_main) // XML 레이아웃 설정
        setupMainActivityUI()
    }

    private fun setupMainActivityUI() {
        welcomeTextView = findViewById(R.id.welcomeTextView)
        emailTextView = findViewById(R.id.emailTextView)
        uidTextView = findViewById(R.id.uidTextView)
        signOutButton = findViewById(R.id.signOutButton)
        firestoreExampleButton = findViewById(R.id.firestoreExampleButton)
        registerEmailButton = findViewById(R.id.register_email) // << 버튼 ID로 초기화

        if (currentUser != null) {
            // 환영 메시지를 조금 더 자연스럽게 수정
            welcomeTextView.text = if (currentUser?.displayName.isNullOrEmpty()) {
                "환영합니다!"
            } else {
                "환영합니다, ${currentUser?.displayName}님!"
            }
            emailTextView.text = "이메일: ${currentUser?.email}"
            uidTextView.text = "UID: ${currentUser?.uid}"
        } else {
            // 이 경우는 onCreate 상단에서 이미 처리되어 거의 발생하지 않음
            welcomeTextView.text = "로그인이 필요합니다."
            emailTextView.text = ""
            uidTextView.text = ""
        }

        signOutButton.setOnClickListener {
            signOutUser()
        }

        firestoreExampleButton.setOnClickListener {
            try {
                // 패키지명 오타 수정 (" com.teminator..." -> "com.teminator...")
                val intent = Intent(this, Class.forName("com.teminator.feature_firestore.FirestoreActivity"))
                startActivity(intent)
            } catch (e: ClassNotFoundException) {
                Toast.makeText(this, "Firestore 예제 기능을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }

        // "등록 이메일 비밀번호" 버튼 클릭 리스너 설정
        registerEmailButton.setOnClickListener {
            // RegisterActivity는 보통 로그인 되지 않은 상태에서 접근하므로,
            // 현재 로그인된 사용자를 로그아웃 시키고 이동하거나,
            // 아니면 그냥 화면만 전환해줄 수 있습니다.
            // 여기서는 로그아웃 없이 화면만 전환합니다 (RegisterActivity 내부에서 로그인/회원가입 처리)
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            // ActivityMain을 finish() 할지는 선택사항입니다.
            // RegisterActivity에서 성공적으로 로그인/회원가입 후 다시 ActivityMain으로 돌아오게 하려면 finish() 안함
            // 만약 RegisterActivity가 새로운 메인 화면 역할을 한다면 finish() 할 수도 있음
        }
    }

    private fun signOutUser() {
        auth.signOut()
        Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
        // 로그아웃 후 RegisterActivity (로그인/회원가입 화면)으로 이동
        val intent = Intent(this, RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // 이전 스택 모두 제거
        startActivity(intent)
        finish() // 현재 ActivityMain 종료
    }

    override fun onStart() {
        super.onStart()
        // 현재는 onCreate에서 로그인 상태를 확인하고 처리하고 있으므로,
        // onStart의 AuthStateListener는 중복 처리가 될 수 있습니다.
        // 만약 앱 사용 중에 실시간으로 다른 기기에서 로그아웃 되는 등의 상황을 감지해야 한다면
        // 이 리스너를 신중하게 관리해야 합니다. (예: 리스너 등록 및 해제)
        // 여기서는 주석 처리하거나, 필요에 따라 로직을 정교하게 다듬습니다.
        /*
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null && !isFinishing) {
                // 사용자가 로그아웃된 상태이고, Activity가 종료 중이 아니라면
                // 로그인 화면으로 이동하는 로직을 추가할 수 있습니다.
                // 다만, signOutUser()에서 이미 RegisterActivity로 보내고 있으므로 중복될 수 있습니다.
                if (this::class.java.simpleName == "ActivityMain") { // 현재 액티비티가 ActivityMain일 때만 동작하도록
                    val intent = Intent(this, RegisterActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }
        */
    }
}