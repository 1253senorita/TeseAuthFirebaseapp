package com.salsample.teseauthfirebaseapp

//package com.google.firebase.quickstart.auth.kotlin

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseException
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
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
import com.google.firebase.Firebase
//import com.google.firebase.quickstart.auth.R
import java.util.concurrent.TimeUnit

/**
 * Created by harshitdwivedi on 14/03/18.
 */
abstract class ModernizeActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modernize)
    }

    private fun checkCurrentUser() {

        val user = Firebase.auth.currentUser
        if (user != null) {
            // User is signed in
        } else {
            // No user is signed in
        }

    }

    private fun getUserProfile() {

        val user = Firebase.auth.currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val name = it.displayName
            val email = it.email
            val photoUrl = it.photoUrl

            // Check if user's email is verified
            val emailVerified = it.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            val uid = it.uid
        }

    }

    private fun getProviderData() {

        val user = Firebase.auth.currentUser
        user?.let {
            for (profile in it.providerData) {
                // Id of the provider (ex: google.com)
                val providerId = profile.providerId

                // UID specific to the provider
                val uid = profile.uid

                // Name, email address, and profile photo Url
                val name = profile.displayName
                val email = profile.email
                val photoUrl = profile.photoUrl
            }
        }

    }

    private fun updateProfile() {

        val user = Firebase.auth.currentUser

        val profileUpdates = userProfileChangeRequest {
            displayName = "Jane Q. User"
            photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                }
            }

    }

    private fun updateEmail() {

        val user = Firebase.auth.currentUser

        user!!.updateEmail("user@example.com")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User email address updated.")
                }
            }

    }

    private fun updatePassword() {

        val user = Firebase.auth.currentUser
        val newPassword = "SOME-SECURE-PASSWORD"

        user!!.updatePassword(newPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User password updated.")
                }
            }

    }

    private fun sendEmailVerification() {

        val user = Firebase.auth.currentUser

        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                }
            }

    }

    private fun sendEmailVerificationWithContinueUrl() {

        val auth = Firebase.auth
        val user = auth.currentUser!!

        val url = "http://www.example.com/verify?uid=" + user.uid
        val actionCodeSettings = ActionCodeSettings.newBuilder()
            .setUrl(url)
            .setIOSBundleId("com.example.ios")
            // The default for this is populated with the current android package name.
            .setAndroidPackageName("com.example.android", false, null)
            .build()

        user.sendEmailVerification(actionCodeSettings)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                }
            }


        auth.setLanguageCode("fr")
        // To apply the default app language instead of explicitly setting it.
        // auth.useAppLanguage()

    }

    private fun sendPasswordReset() {

        val emailAddress = "user@example.com"

        Firebase.auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                }
            }

    }

    private fun deleteUser() {

        val user = Firebase.auth.currentUser!!

        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User account deleted.")
                }
            }

    }

    private fun reauthenticate() {

        val user = Firebase.auth.currentUser!!

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        val credential = EmailAuthProvider
            .getCredential("user@example.com", "password1234")

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
            .addOnCompleteListener { Log.d(TAG, "User re-authenticated.") }

    }

    private fun authWithGithub() {
        // [START auth_with_github]
        val token = "<GITHUB-ACCESS-TOKEN>"
        val credential = GithubAuthProvider.getCredential(token)
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful)

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful) {
                    Log.w(TAG, "signInWithCredential", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }

                // ...
            }
        // [END auth_with_github]
    }

    private fun linkAndMerge(credential: AuthCredential) {
        val auth = Firebase.auth


        val prevUser = auth.currentUser
        auth.signInWithCredential(credential)
            .addOnSuccessListener { result ->
                val currentUser = result.user
                // Merge prevUser and currentUser accounts and data
                // ...
            }
            .addOnFailureListener {
                // ...
            }

    }

    private fun unlink(providerId: String) {

        Firebase.auth.currentUser!!.unlink(providerId)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Auth provider unlinked from account
                    // ...
                }
            }

    }

    private fun buildActionCodeSettings() {

        val actionCodeSettings = actionCodeSettings {
            // URL you want to redirect back to. The domain (www.example.com) for this
            // URL must be whitelisted in the Firebase Console.
            url = "https://www.example.com/finishSignUp?cartId=1234"
            // This must be true
            handleCodeInApp = true
            setIOSBundleId("com.example.ios")
            setAndroidPackageName(
                "com.example.android",
                true, // installIfNotAvailable
                "12", // minimumVersion
            )
        }

    }

    private fun sendSignInLink(email: String, actionCodeSettings: ActionCodeSettings) {

        Firebase.auth.sendSignInLinkToEmail(email, actionCodeSettings)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                }
            }

    }

    private fun verifySignInLink() {

        val auth = Firebase.auth
        val intent = intent
        val emailLink = intent.data.toString()

        // Confirm the link is a sign-in with email link.
        if (auth.isSignInWithEmailLink(emailLink)) {
            // Retrieve this from wherever you stored it
            val email = "someemail@domain.com"

            // The client SDK will parse the code from the link for you.
            auth.signInWithEmailLink(email, emailLink)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Successfully signed in with email link!")
                        val result = task.result
                        // You can access the new user via result.getUser()
                        // Additional user info profile *not* available via:
                        // result.getAdditionalUserInfo().getProfile() == null
                        // You can check if the user is new or existing:
                        // result.getAdditionalUserInfo().isNewUser()
                    } else {
                        Log.e(TAG, "Error signing in with email link", task.exception)
                    }
                }
        }

    }

    private fun linkWithSignInLink(email: String, emailLink: String) {

        // Construct the email link credential from the current URL.
        val credential = EmailAuthProvider.getCredentialWithLink(email, emailLink)

        // Link the credential to the current user.
        Firebase.auth.currentUser!!.linkWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Successfully linked emailLink credential!")
                    val result = task.result
                    // You can access the new user via result.getUser()
                    // Additional user info profile *not* available via:
                    // result.getAdditionalUserInfo().getProfile() == null
                    // You can check if the user is new or existing:
                    // result.getAdditionalUserInfo().isNewUser()
                } else {
                    Log.e(TAG, "Error linking emailLink credential", task.exception)
                }
            }

    }

    private fun reauthWithLink(email: String, emailLink: String) {

        // Construct the email link credential from the current URL.
        val credential = EmailAuthProvider.getCredentialWithLink(email, emailLink)

        // Re-authenticate the user with this credential.
        Firebase.auth.currentUser!!.reauthenticateAndRetrieveData(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // User is now successfully reauthenticated
                } else {
                    Log.e(TAG, "Error reauthenticating", task.exception)
                }
            }

    }

    private fun differentiateLink(email: String) {

        Firebase.auth.fetchSignInMethodsForEmail(email)
            .addOnSuccessListener { result ->
                val signInMethods = result.signInMethods!!
                if (signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)) {
                    // User can sign in with email/password
                } else if (signInMethods.contains(EmailAuthProvider.EMAIL_LINK_SIGN_IN_METHOD)) {
                    // User can sign in with email/link
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting sign in methods for user", exception)
            }

    }

    private fun getGoogleCredentials() {
        val googleIdToken = ""
        // [START auth_google_cred]
        val credential = GoogleAuthProvider.getCredential(googleIdToken, null)
        // [END auth_google_cred]
    }

    /*
private fun getFbCredentials() {
    val token = AccessToken.getCurrentAccessToken()
    val credential = token?.let {
        FacebookAuthProvider.getCredential(it.token)
    }
}
*/
    private fun getEmailCredentials() {
        val email = ""
        val password = ""

        val credential = EmailAuthProvider.getCredential(email, password)

    }

    private fun signOut() {

        Firebase.auth.signOut()

    }

    private fun testPhoneVerify() {
        // [START auth_test_phone_verify]
        val phoneNum = "+16505554567"
        val testVerificationCode = "123456"

        // Whenever verification is triggered with the whitelisted number,
        // provided it is not set for auto-retrieval, onCodeSent will be triggered.
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber(phoneNum)
            .setTimeout(30L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onCodeSent(
                    verificationId: String,
                    forceResendingToken: PhoneAuthProvider.ForceResendingToken,
                ) {
                    // Save the verification id somewhere
                    // ...

                    // The corresponding whitelisted code above should be used to complete sign-in.
                    this@ModernizeActivity.enableUserManuallyInputCode()
                }

                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    // Sign in with the credential
                    // ...
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // ...
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        // [END auth_test_phone_verify]
    }

    private fun enableUserManuallyInputCode() {
        // No-op
    }

    private fun testPhoneAutoRetrieve() {
        // [START auth_test_phone_auto]
        // The test phone number and code should be whitelisted in the console.
        val phoneNumber = "+16505554567"
        val smsCode = "123456"

        val firebaseAuth = Firebase.auth
        val firebaseAuthSettings = firebaseAuth.firebaseAuthSettings

        // Configure faking the auto-retrieval with the whitelisted numbers.
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNumber, smsCode)

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Instant verification is applied and a credential is directly returned.
                    // ...
                }

                // [START_EXCLUDE]
                override fun onVerificationFailed(e: FirebaseException) {
                }
                // [END_EXCLUDE]
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        // [END auth_test_phone_auto]
    }

    private fun gamesMakeGoogleSignInOptions() {
        // [START games_google_signin_options]
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
            .requestServerAuthCode(getString(R.string.default_web_client_id))
            .build()
        // [END games_google_signin_options]
    }

    // [START games_auth_with_firebase]
    // Call this both in the silent sign-in task's OnCompleteListener and in the
    // Activity's onActivityResult handler.
    private fun firebaseAuthWithPlayGames(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithPlayGames:" + acct.id!!)

        val auth = Firebase.auth
        val credential = PlayGamesAuthProvider.getCredential(acct.serverAuthCode!!)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }

                // ...
            }
    }
    // [END games_auth_with_firebase]

    private fun gamesGetUserInfo() {
        val auth = Firebase.auth

        // [START games_get_user_info]
        val user = auth.currentUser
        user?.let {
            val playerName = it.displayName

            // The user's Id, unique to the Firebase project.
            // Do NOT use this value to authenticate with your backend server, if you
            // have one; use FirebaseUser.getIdToken() instead.
            val uid = it.uid
        }

        // [END games_get_user_info]
    }

    private fun updateUI(user: FirebaseUser?) {
        // No-op
    }
}