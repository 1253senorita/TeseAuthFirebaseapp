package com.salsample.teseauthfirebaseapp



import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult

class AuthActivity : AppCompatActivity() {

    // Activity Result API를 사용하여 결과를 처리
    private val signInLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            FirebaseAuthUIActivityResultContract()
        ) { result: FirebaseAuthUIAuthenticationResult ->
            onSignInResult(result)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)  // 연결할 xml 레이아웃

        // 인증 제공자 설정 : 이메일 인증의 경우 예시
        val providers = listOf(
            AuthUI.IdpConfig.EmailBuilder().build()
            // 원한다면 Google, Facebook 등 다른 제공자도 추가할 수 있음
        )

        // Firebase UI의 로그인 인텐트를 생성하여 실행
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            // 로그인 성공: FirebaseUser 등을 통해 사용자 정보를 사용할 수 있음
        } else {
            // 로그인 실패 또는 취소 처리
        }
    }
}