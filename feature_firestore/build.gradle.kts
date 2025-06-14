plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    // 필요하다면, kapt 또는 ksp (데이터 바인딩, 룸 등 사용 시)
}

android {
    namespace = "com.salsample.teseauthfirebaseapp.feature_firestore" // 메인 앱과 네임스페이스 규칙 일관성 유지 권장
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false // 필요에 따라 true로 변경하고 Proguard 규칙 설정
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        // 프로젝트 전체 Java 버전 통일 (예: :app 모듈과 동일하게)
        sourceCompatibility = JavaVersion.VERSION_1_8 // 또는 JavaVersion.VERSION_11 (앱 모듈과 통일)
        targetCompatibility = JavaVersion.VERSION_1_8 // 또는 JavaVersion.VERSION_11 (앱 모듈과 통일)
    }
    kotlinOptions {
        jvmTarget = "1.8" // 또는 "11" (앱 모듈과 통일)
    }
    // buildFeatures { // 예시: ViewBinding 사용 시
    //     viewBinding = true
    // }
}

dependencies {
    implementation(libs.androidx.core.ktx) // libs.versions.toml에 정의된 별칭 사용 (버전 X)
    implementation(libs.androidx.appcompat) // libs.versions.toml에 정의된 별칭 사용 (버전 X)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout.v214)

    // ... 다른 기본 의존성들 (예: AppCompat, Core KTX) ...

    // Firebase Firestore KTX
    implementation("com.google.firebase:firebase-firestore-ktx")

    // 만약 Firebase BoM (Bill of Materials)을 프로젝트 루트의 build.gradle이나
    // settings.gradle에 선언했다면, 위처럼 직접 버전을 명시하지 않아도 됩니다.
    // 예: implementation(platform("com.google.firebase:firebase-bom:LATEST_VERSION")) // 루트에 선언
    //     implementation("com.google.firebase:firebase-firestore-ktx") // 여기서는 버전 없이

    // UI를 위한 의존성 (Activity에 UI가 있다면 필요)
    implementation("androidx.appcompat:appcompat:1.7.0") // 예시 버전, 최신 버전 확인
    implementation("androidx.core:core-ktx:1.16.0")     // 예시 버전
    implementation("com.google.android.material:material:1.12.0")
    implementation(libs.firebase.common.ktx)
    implementation(libs.firebase.firestore.ktx) // Material Design 컴포넌트 사용 시
    // 만약 Jetpack Compose를 사용한다면 관련 의존성 추가




}// libs.versions.toml에 정의된 별칭 사용 (버전 X)