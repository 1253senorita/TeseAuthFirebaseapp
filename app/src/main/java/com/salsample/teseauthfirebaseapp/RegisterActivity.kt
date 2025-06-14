package com.salsample.teseauthfirebaseapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var emailEditTextMember: EditText
    private lateinit var passwordEditTextMember: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity) // XML 적용

        auth = FirebaseAuth.getInstance()

        emailEditTextMember = findViewById(R.id.emailEditText)
        passwordEditTextMember = findViewById(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signUpButton = findViewById<Button>(R.id.signUpButton)

        // --- 키보드 및 포커스 처리 로직 수정 ---

        // 화면 시작 시 emailEditText에 자동으로 포커스를 주던 부분 삭제
        // emailEditTextMember.requestFocus()
        // val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // imm.showSoftInput(emailEditTextMember, InputMethodManager.SHOW_IMPLICIT)

        // 이메일 입력 필드(emailEditTextMember)에서 '다음' (Next) 액션 처리
        emailEditTextMember.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                // '다음' 액션 시 비밀번호 필드로 포커스 이동

                return@setOnEditorActionListener true // 이벤트 소비됨
            }
            return@setOnEditorActionListener false // 이벤트 소비 안됨
        }

        // 비밀번호 입력 필드(passwordEditTextMember)에서 '완료' (Done) 액션 처리
        passwordEditTextMember.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val email = emailEditTextMember.text.toString()
                val password = passwordEditTextMember.text.toString()
                if (email.isBlank() || password.isBlank()) {
                    Toast.makeText(this, "이메일과 비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    loginUser(email, password)
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        // --- 키보드 및 포커스 처리 로직 수정 끝 ---

        loginButton.setOnClickListener {
            val email = emailEditTextMember.text.toString()
            val password = passwordEditTextMember.text.toString()
            loginUser(email, password)
        }

        signUpButton.setOnClickListener {
            val email = emailEditTextMember.text.toString()
            val password = passwordEditTextMember.text.toString()
            registerUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(this, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        hideKeyboard()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, ModernizeActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "로그인 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun registerUser(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(this, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.length < 6) {
            Toast.makeText(this, "비밀번호는 6자 이상 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        hideKeyboard()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, ModernizeActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "회원가입 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}