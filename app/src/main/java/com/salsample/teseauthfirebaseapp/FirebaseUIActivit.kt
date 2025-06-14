package com.salsample.teseauthfirebaseapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI // FirebaseUI 인증 라이브러리의 메인 클래스
import com.firebase.ui.auth.AuthUI.IdpConfig // 각 인증 제공자(Identity Provider) 설정을 위한 클래스
import com.firebase.ui.auth.AuthUI.IdpConfig.EmailBuilder // 이메일 인증 제공자 설정을 위한 빌더 클래스
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract // FirebaseUI 인증 결과를 받기 위한 ActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult // FirebaseUI 인증 결과 객체
import com.google.firebase.auth.ActionCodeSettings // 이메일 링크 인증 시 추가 설정을 위한 클래스
import com.google.firebase.auth.FirebaseAuth // Firebase 인증 서비스의 메인 SDK 클래스

/**
 * FirebaseUI를 사용하여 다양한 인증 방법을 제공하는 기본 추상 Activity.
 * 이 Activity를 상속받아 실제 인증 기능을 구현할 수 있습니다.
 */
abstract class FirebaseUIActivity : AppCompatActivity() {

    // FirebaseUI 인증 흐름(로그인/회원가입 화면)을 시작하고 그 결과를 처리하기 위한 ActivityResultLauncher.
    // FirebaseAuthUIActivityResultContract는 FirebaseUI의 인증 결과를 받을 수 있도록 미리 정의된 계약입니다.
    // 인증 화면이 종료되면 람다 표현식 { firebaseAuthUIAuthenticationResult -> this.onSignInResult(firebaseAuthUIAuthenticationResult) } 부분이 호출됩니다.
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { firebaseAuthUIAuthenticationResult -> // 결과 타입을 명확히 함 (원래 res로 되어 있었음)
        // FirebaseUI 인증 화면으로부터 결과를 받으면 onSignInResult 메소드를 호출합니다.
        this.onSignInResult(firebaseAuthUIAuthenticationResult)
    }

    /**
     * Activity가 생성될 때 호출됩니다.
     * 여기서는 화면의 레이아웃을 설정합니다.
     * @param savedInstanceState Activity가 이전에 저장한 상태가 있다면 그 상태를 담고 있는 Bundle 객체.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // activity_firebase_ui.xml 파일을 현재 Activity의 화면으로 설정합니다.
        // 이 XML에는 보통 FirebaseUI 인증을 시작하는 버튼 등이 위치합니다.
        setContentView(R.layout.activity_firebase_ui)
    }

    /**
     * FirebaseUI 인증 화면을 시작하기 위한 Intent를 생성하고 실행합니다.
     * 이 메소드가 호출되면 FirebaseUI가 제공하는 로그인 방법 선택 화면 또는
     * 단일 제공자일 경우 해당 제공자의 로그인 화면이 나타납니다.
     */
    private fun createSignInIntent() {
        // 사용할 인증 제공자(IdP - Identity Provider) 목록을 설정합니다.
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(), // 이메일/비밀번호 인증 제공자
            AuthUI.IdpConfig.PhoneBuilder().build(), // 전화번호 인증 제공자
            AuthUI.IdpConfig.GoogleBuilder().build(), // Google 계정 인증 제공자
            // AuthUI.IdpConfig.FacebookBuilder().build(), // Facebook 계정 인증 제공자 (별도 SDK 설정 필요)
            // AuthUI.IdpConfig.TwitterBuilder().build(),  // Twitter 계정 인증 제공자 (별도 SDK 설정 필요)
        )

        // FirebaseUI 인증 Intent를 생성합니다.
        val signInIntent = AuthUI.getInstance() // AuthUI 싱글턴 인스턴스를 가져옵니다.
            .createSignInIntentBuilder() // 로그인 Intent 빌더를 생성합니다.
            .setAvailableProviders(providers) // 사용할 인증 제공자 목록을 설정합니다.
            // .setLogo(R.drawable.my_logo) // 선택 사항: 로고 설정
            // .setTheme(R.style.MyTheme)   // 선택 사항: 테마 설정
            .build() // 최종적으로 Intent를 빌드합니다.

        // 생성된 signInIntent를 사용하여 FirebaseUI 인증 화면을 시작합니다.
        // 결과는 위에서 정의한 signInLauncher를 통해 onSignInResult로 전달됩니다.
        signInLauncher.launch(signInIntent)
    }

    /**
     * FirebaseUI 인증 흐름의 결과를 처리합니다.
     * @param result FirebaseUI 인증 시도 후 반환되는 결과 객체 (FirebaseAuthUIAuthenticationResult).
     */
    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse // 인증 응답에 대한 상세 정보를 담고 있는 객체.
        if (result.resultCode == RESULT_OK) {
            // 인증에 성공한 경우.
            // 성공적으로 로그인되었으므로, 현재 사용자 정보를 가져올 수 있습니다.
            val user = FirebaseAuth.getInstance().currentUser
            // TODO: 로그인 성공 후 처리 로직 (예: 메인 화면으로 이동, 사용자 정보 저장 등)
            // 예시: Toast.makeText(this, "로그인 성공: ${user?.email}", Toast.LENGTH_SHORT).show()
        } else {
            // 인증에 실패하거나 사용자가 취소한 경우.
            if (response == null) {
                // 사용자가 뒤로 가기 버튼 등으로 인증 흐름을 취소한 경우 (response가 null).
                // TODO: 사용자 취소 처리 로직
                // 예시: Toast.makeText(this, "로그인이 취소되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                // 그 외의 오류로 인해 로그인에 실패한 경우.
                // response.getError().getErrorCode() 등을 통해 오류 코드를 확인하고 처리할 수 있습니다.
                // TODO: 로그인 실패 처리 로직
                // 예시: Toast.makeText(this, "로그인 실패: ${response.error?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 현재 로그인된 사용자를 로그아웃시킵니다.
     * FirebaseUI를 통해 로그아웃을 처리합니다.
     */
    private fun signOut() {
        AuthUI.getInstance()
            .signOut(this) // 현재 Activity context를 전달하여 로그아웃을 시작합니다.
            .addOnCompleteListener {
                // 로그아웃 작업이 완료된 후 호출됩니다.
                // TODO: 로그아웃 완료 후 처리 로직 (예: 로그인 화면으로 이동)
                // 예시: Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
            }
    }

    /**
     * 현재 로그인된 사용자의 계정을 삭제합니다.
     * FirebaseUI를 통해 계정 삭제를 처리합니다.
     * 이 작업은 민감하므로 사용자에게 재인증을 요구할 수 있습니다.
     */
    private fun delete() {
        AuthUI.getInstance()
            .delete(this) // 현재 Activity context를 전달하여 계정 삭제를 시작합니다.
            .addOnCompleteListener { task -> // 계정 삭제 작업의 성공/실패 여부를 task로 받음
                if (task.isSuccessful) {
                    // 계정 삭제 성공
                    // TODO: 계정 삭제 성공 후 처리 로직
                    // 예시: Toast.makeText(this, "계정이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    // 계정 삭제 실패
                    // TODO: 계정 삭제 실패 처리 로직
                    // 예시: Toast.makeText(this, "계정 삭제 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    /**
     * FirebaseUI 인증 화면에 커스텀 테마와 로고를 적용하는 예시입니다.
     */
    private fun themeAndLogo() {
        // 이 예시에서는 특정 제공자를 설정하지 않고, 테마와 로고만 적용한 인증 화면을 띄웁니다.
        // 실제 사용 시에는 createSignInIntent처럼 providers를 적절히 설정해야 합니다.
        val providers = emptyList<AuthUI.IdpConfig>()

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers) // 사용할 제공자 목록 (여기서는 비어있음, 실제로는 필요에 따라 채워야 함)
            .setLogo(R.drawable.my_great_logo) // 표시할 로고 drawable 리소스 ID 설정
            .setTheme(R.style.MySuperAppTheme) // 적용할 커스텀 테마 ID 설정 (styles.xml에 정의 필요)
            .build()
        signInLauncher.launch(signInIntent)
    }

    /**
     * FirebaseUI 인증 화면에 개인정보처리방침 및 서비스 약관 URL을 설정하는 예시입니다.
     * 설정된 URL은 로그인 화면의 하단 등에 링크로 표시될 수 있습니다.
     */
    private fun privacyAndTerms() {
        // 이 예시에서는 특정 제공자를 설정하지 않고, 개인정보처리방침 및 서비스 약관 URL만 설정합니다.
        // 실제 사용 시에는 createSignInIntent처럼 providers를 적절히 설정해야 합니다.
        val providers = emptyList<AuthUI.IdpConfig>()

        // [START auth_fui_pp_tos]
        // FirebaseUI 로그인 Intent 빌더를 사용하여 개인정보처리방침 및 서비스 약관 URL을 설정합니다.
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers) // 사용할 제공자 목록 (여기서는 비어있음)
            .setTosAndPrivacyPolicyUrls( // 서비스 약관 URL과 개인정보처리방침 URL을 각각 설정합니다.
                "https://example.com/terms.html", // 서비스 약관 URL
                "https://example.com/privacy.html", // 개인정보처리방침 URL
            )
            .build() // Intent를 빌드합니다.

        // 생성된 signInIntent를 사용하여 FirebaseUI 인증 화면을 시작합니다.
        signInLauncher.launch(signInIntent)
        // [END auth_fui_pp_tos]
    }

    /**
     * 비밀번호 없는 이메일 링크 인증을 시작하는 함수입니다.
     * 사용자에게 이메일로 로그인 링크를 보내고, 사용자가 그 링크를 클릭하면 앱으로 돌아와 로그인됩니다.
     * 이 함수는 open으로 선언되어 하위 클래스에서 재정의(override)하거나 직접 호출할 수 있습니다.
     */
    open fun emailLink() {
        // [START auth_fui_email_link]
        // 이메일 링크 인증에 사용될 추가 설정을 구성합니다. (ActionCodeSettings)
        val actionCodeSettings = ActionCodeSettings.newBuilder()
            .setAndroidPackageName( // 이메일 링크를 처리할 안드로이드 앱의 패키지 이름을 지정합니다.
                "...", // TODO: 실제 앱의 패키지 이름으로 변경해야 합니다.
                true, // 플레이 스토어에서 앱을 설치할 수 없는 경우 설치하도록 유도할지 여부.
                null, // 앱이 설치되어 있지 않을 경우 요구되는 최소 버전 (null이면 버전 체크 안 함).
            )
            .setHandleCodeInApp(true) // 이메일 링크를 앱 내에서 처리할지 여부 (반드시 true로 설정).
            .setUrl("https://google.com") // Firebase 콘솔의 인증 설정에서 승인(whitelist)된 URL. 링크 클릭 후 리디렉션될 수 있는 URL.
            .build()

        // 이메일 링크 인증 제공자만 포함하는 provider 목록을 생성합니다.
        val providers = listOf(
            EmailBuilder() // 이메일 인증 제공자 빌더를 사용합니다.
                .enableEmailLinkSignIn() // 이메일 링크 인증을 활성화합니다.
                .setActionCodeSettings(actionCodeSettings) // 위에서 정의한 ActionCodeSettings를 설정합니다.
                .build(), // 최종적으로 이메일 링크 인증 제공자 설정을 빌드합니다.
        )

        // FirebaseUI 로그인 Intent를 생성합니다.
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers) // 이메일 링크 인증 제공자만 사용하도록 설정합니다.
            .build()

        // 생성된 signInIntent를 사용하여 FirebaseUI 인증 화면을 시작합니다.
        // 사용자에게 이메일 주소를 입력받는 화면이 나타납니다.
        signInLauncher.launch(signInIntent)
        // [END auth_fui_email_link]
    }

    /**
     * 앱이 이메일 링크를 통해 실행되었을 때 해당 링크를 처리하여 로그인을 완료하는 함수입니다.
     * 보통 Activity의 onCreate나 onNewIntent에서 호출하여 딥링크(deep link)로 전달된 이메일 링크를 감지하고 처리합니다.
     * 이 함수는 open으로 선언되어 하위 클래스에서 재정의(override)하거나 직접 호출할 수 있습니다.
     */
    open fun catchEmailLink() {
        // 이메일 링크 인증을 위한 provider 목록. 실제로는 사용되지 않을 수 있지만,
        // setAvailableProviders에 빈 리스트라도 전달해야 하는 경우가 있을 수 있습니다. (FirebaseUI 버전에 따라 다를 수 있음)
        val providers: List<IdpConfig> = emptyList()

        // [START auth_fui_email_link_catch]
        // 현재 Activity를 시작시킨 Intent가 FirebaseUI가 처리할 수 있는 이메일 링크 관련 Intent인지 확인합니다.
        if (AuthUI.canHandleIntent(intent)) {
            // Intent에서 추가 정보(extras)를 가져옵니다. 없으면 함수를 종료합니다.
            val extras = intent.extras ?: return
            // 추가 정보에서 "email_link_sign_in" 키로 실제 이메일 로그인 링크 문자열을 가져옵니다.
            val link = extras.getString("email_link_sign_in")
            if (link != null) { // 링크가 유효하다면
                // FirebaseUI 로그인 Intent를 생성합니다.
                val signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setEmailLink(link) // 직접 이메일 링크를 설정하여 해당 링크로 로그인을 시도합니다.
                    .setAvailableProviders(providers) // 사용할 제공자 목록 (여기서는 크게 의미 없을 수 있음)
                    .build()

                // 생성된 signInIntent를 사용하여 FirebaseUI 인증 화면을 시작합니다.
                // 이 경우, FirebaseUI는 제공된 링크를 사용하여 자동으로 로그인을 시도합니다.
                signInLauncher.launch(signInIntent)
            }
        }
        // [END auth_fui_email_link_catch]
    }
}