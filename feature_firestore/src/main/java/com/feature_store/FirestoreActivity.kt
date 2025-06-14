package com.feature_store
// feature_firestore 모듈 내의 올바른 패키지 경로에 있어야 함
// 예: package com.오빠회사이름.feature_firestore.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore // KTX 임포트
//import com.google.firebase.ktx.Firebase           // KTX 임포트
// 필요한 경우 UI 요소 임포트 (예: import android.widget.Button)

class FirestoreActivity : AppCompatActivity() {

    private val db = com.google.firebase.Firebase.firestore // Firestore 인스턴스 (KTX 사용)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1단계에서 레이아웃 파일을 생성했다면 연결
        // 예: setContentView(R.layout.activity_firestore)
        // 만약 feature_firestore 모듈의 R을 참조해야 한다면,
        // 이 모듈의 패키지명을 정확히 이해하고 R 파일을 임포트해야 할 수 있습니다.
        // (보통은 자동으로 잘 됩니다)

        Log.d("FirestoreActivity", "FirestoreActivity가 시작되었습니다 in feature_firestore module.")
        Toast.makeText(this, "Firestore 액티비티 (feature 모듈)!", Toast.LENGTH_SHORT).show()

        // 여기에 Firestore 데이터 읽기/쓰기 로직, UI 핸들링 등을 추가합니다.
        // 예: addSampleDataToFirestore()
    }

    private fun addSampleDataToFirestore() {
        val data = hashMapOf(
            "name" to "Sample User from Feature Module",
            "timestamp" to com.google.firebase.Timestamp.now()
        )
        db.collection("users") // 원하는 컬렉션 이름
            .add(data)
            .addOnSuccessListener {
                Log.d("FirestoreActivity", "데이터 추가 성공: ${it.id}")
            }
            .addOnFailureListener { e ->
                Log.w("FirestoreActivity", "데이터 추가 실패", e)
            }
    }
}