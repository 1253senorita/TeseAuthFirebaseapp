plugins {
    id("com.android.application")
    id("com.google.gms.google-services") // Firebase 사용 시 필수
    id("org.jetbrains.kotlin.android") // Kotlin 플러그인 명확하게 지정
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.0-RC"
    id("com.google.firebase.crashlytics") version "3.0.3"
    id("com.google.firebase.firebase-perf") version "1.4.2"

    id("com.github.ben-manes.versions") version "0.52.0"

}

android {
    namespace = "com.salsample.teseauthfirebaseapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.salsample.teseauthfirebaseapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    // signingConfigs {
    // You can leave this empty if you're only using the default debug signing
    // Or be explicit:
    // debug {
    //     // Default debug signing config is usually handled by AS
    // }
    // }

    buildTypes {
        getByName("debug") {
            // signingConfig = signingConfigs.getByName("debug") // Usually implicit
        }
        release { // 'getByName("release")' is equivalent to just 'release' here
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Temporarily use the debug signing for release
            // THIS IS NOT FOR PRODUCTION
            signingConfig = signingConfigs.getByName("debug")
        }
    }


    composeOptions {
        kotlinCompilerExtensionVersion = "1.6.0" // 최신 버전 적용
    }

    packaging.resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
}

dependencies {
    // Firebase BOM을 사용하여 모든 Firebase 라이브러리 버전 자동 관리
    implementation(libs.firebase.bom.v33140)

    // Firebase 라이브러리 (BOM을 통해 버전 자동 적용)
    implementation("com.google.firebase:firebase-auth")

    implementation("com.firebaseui:firebase-ui-auth:9.0.0")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-perf")



    // ... other dependencies
    val composeVersion = "1.6.8" // Or the latest stable version

    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion") // If you use Material Design
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")


   // implementation("com.facebook.android:facebook-android-sdk:8.x")
    // Firebase UI Auth (BOM과 별도로 관리)


    // Import the BoM for the Firebase platform


    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies



    // AndroidX 및 기타 라이브러리 (변경 없음)
    implementation(libs.androidx.core.ktx.v1120)
    implementation(libs.androidx.appcompat.v161)
    implementation(libs.material.v1110)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.constraintlayout.v214)
    implementation(libs.firebase.auth.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.v115)
    androidTestImplementation(libs.androidx.espresso.core.v351)

    // Jetpack Compose 의존성 (변경 없음)
    implementation(libs.ui)
    implementation(libs.androidx.material)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)





    // Facebook SDK (필요한 경우)
    implementation(libs.facebook.android.sdk.v8x)
    implementation(libs.facebook.login)
    implementation(libs.facebook.android.sdk.v1600) // 최신 버전 확인 필요



    // 만약 Retrofit 라이브러리가 필요했다면:
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // 예시: Gson 변환기와 함께 사용


    implementation(libs.okhttp.lib)
    implementation(libs.firebase.perf.lib)
    implementation(libs.androidx.core.ktx)
    implementation ("androidx.core:core-splashscreen:1.0.0")
    implementation (project(":feature_firestore"))

}

    






