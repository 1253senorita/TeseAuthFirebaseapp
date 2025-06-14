package com.teminator.feature_firestore // 현재 FirestoreActivity.kt 파일의 패키지 선언

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// 올바른 R 클래스 임포트 (build.gradle.kts의 namespace 기준)
import com.salsample.teseauthfirebaseapp.feature_firestore.R //  <-- 이 부분이 변경되었습니다!

open class FirestoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val textView = TextView(this)

        // 이제 이 R은 "com.salsample.teseauthfirebaseapp.feature_firestore.R"을 참조합니다.
        textView.text = getString(R.string.title_activity_firestore)
        textView.textSize = 24f
        setContentView(textView)
        title = getString(R.string.title_activity_firestore)
    }
}