package com.salsample.teseauthfirebaseapp


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GithubAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PlayGamesAuthProvider
import com.google.firebase.auth.actionCodeSettings
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val TAG = "MainActivity"

    // 예시 UI 요소 (실제 activity_main.xml에 정의되어 있어야 함)
    private var tvEmail: TextView? = null
    private var tvUid: TextView? = null
    private var btnLogout: Button? = null
    private var btnGoToAuth: Button? = null
    private var loggedInGroup: View? = null
    private var loggedOutGroup: View? = null
    private var btnUpdateProfile: Button? = null // 프로필 업데이트 버튼 예시

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        tvEmail = findViewById(R.id.tv_email)
        tvUid = findViewById(R.id.tv_uid)
        btnLogout = findViewById(R.id.btn_logout)
        btnGoToAuth = findViewById(R.id.btn_go_to_auth)
        loggedInGroup = findViewById(R.id.logged_in_group)
        loggedOutGroup = findViewById(R.id.logged_out_group)
        btnUpdateProfile = findViewById(R.id.btn_update_profile) // activity_main.xml에 ID 필요

        btnLogout?.setOnClickListener {
            signOutUser()
        }

        btnGoToAuth?.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
        }

        btnUpdateProfile?.setOnClickListener {
            // 실제 앱에서는 사용자로부터 입력받은 이름과 사진 URI를 사용해야 합니다.
            val newDisplayName = "New Jane Q. User" // 예시 이름
            val newPhotoUrl = "https://example.com/new-jane-q-user/profile.jpg" // 예시 URL
            updateProfile(newDisplayName, newPhotoUrl.toUri())
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            loggedInGroup?.visibility = View.VISIBLE
            loggedOutGroup?.visibility = View.GONE
            btnUpdateProfile?.visibility = View.VISIBLE // 로그인 시 프로필 업데이트 버튼 표시

            tvEmail?.text = "Email: ${user.email}"
            tvUid?.text =
                "UID: ${user.uid}\nDisplay Name: ${user.displayName}\nPhoto URL: ${user.photoUrl}"


            if (!user.isEmailVerified) {
                Log.d(TAG, "User email not verified.")
                // 필요시 이메일 인증 요청 버튼 등을 여기에 추가
            }

        } else {
            loggedInGroup?.visibility = View.GONE
            loggedOutGroup?.visibility = View.VISIBLE
            btnUpdateProfile?.visibility = View.GONE // 로그아웃 시 프로필 업데이트 버튼 숨김

            tvEmail?.text = "로그인되지 않았습니다."
            tvUid?.text = ""
        }
    }

    private fun signOutUser() {
        auth.signOut()
        Log.d(TAG, "User signed out.")
        updateUI(null)
    }

    private fun checkCurrentUser() {
        val user = Firebase.auth.currentUser
        if (user != null) {
            Log.d(TAG, "Current user: ${user.email}")
        } else {
            Log.d(TAG, "No user is signed in.")
        }
    }

    private fun getUserProfile() {
        val user = Firebase.auth.currentUser
        user?.let {
            val name = it.displayName
            val email = it.email
            val photoUrl = it.photoUrl
            val emailVerified = it.isEmailVerified
            val uid = it.uid
            Log.d(
                TAG,
                "User Profile: Name=$name, Email=$email, PhotoURL=$photoUrl, Verified=$emailVerified, UID=$uid"
            )
        }
    }

    private fun getProviderData() {
        val user = Firebase.auth.currentUser
        user?.let {
            for (profile in it.providerData) {
                val providerId = profile.providerId
                val uid = profile.uid
                val name = profile.displayName
                val email = profile.email
                val photoUrl = profile.photoUrl
                Log.d(
                    TAG,
                    "Provider Data: ID=$providerId, UID=$uid, Name=$name, Email=$email, PhotoURL=$photoUrl"
                )
            }
        }
    }

    // 프로필 업데이트 함수 (이름과 사진 URI를 파라미터로 받도록 수정)
    private fun updateProfile(displayName: String?, photoUri: Uri?) {
        val user = Firebase.auth.currentUser

        if (user == null) {
            Toast.makeText(baseContext, "업데이트할 사용자가 로그인되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
            Log.w(TAG, "updateProfile: No user signed in to update.")
            return // 함수 종료
        }

        val profileUpdates = userProfileChangeRequest {
            // displayName과 photoUri가 null이 아닐 경우에만 업데이트 요청에 포함
            displayName?.let { this.displayName = it }
            photoUri?.let { this.photoUri = it }
        }

        user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated successfully.")
                    Toast.makeText(baseContext, "프로필이 업데이트되었습니다.", Toast.LENGTH_SHORT).show()
                    // UI 새로고침 (변경된 프로필 정보 반영)
                    updateUI(user) // 현재 사용자 정보로 UI 다시 그림
                } else {
                    Log.w(TAG, "User profile update failed.", task.exception)
                    Toast.makeText(
                        baseContext,
                        "프로필 업데이트 실패: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun updateEmail() {
        // [START update_email]
        val user = Firebase.auth.currentUser
        if (user == null) {
            Toast.makeText(baseContext, "이메일을 업데이트할 사용자가 로그인되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        // 실제 앱에서는 사용자로부터 새 이메일 주소를 입력받아야 합니다.
        val newEmail = "user_new_email@example.com" // 예시 새 이메일

        user.updateEmail(newEmail)
            .addOnCompleteListener { task -> // 이 addOnCompleteListener의 람다 시작
                if (task.isSuccessful) {
                    Log.d(TAG, "User email address updated.")
                    Toast.makeText(
                        baseContext,
                        "이메일 주소가 업데이트되었습니다. 새 이메일로 다시 로그인해주세요.",
                        Toast.LENGTH_LONG
                    ).show()


                }
            }
    }
}