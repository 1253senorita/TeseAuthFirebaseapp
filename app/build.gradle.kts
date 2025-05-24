plugins {

    id("com.android.application")
    //alias(libs.plugins.android.application)


    // alias(libs.plugins.hilt)
    // kotlin("kapt") // Hilt와 함께 kapt 사용 시

    alias(libs.plugins.kotlin.android)
    //alias(libs.plugins.google.gms.google.services) // Firebase 사용 시 필수
    id("com.google.gms.google-services")


    alias(libs.plugins.androidx.compose.compiler)
}

android {
    namespace = "com.salsample.teseauthfirebaseapp"
    compileSdk = 35 // 사용하는 compileSdk 버전

    defaultConfig {
        applicationId = "com.salsample.teseauthfirebaseapp"
        minSdk = 24 // 사용하는 minSdk 버전
        targetSdk = 34 // 사용하는 targetSdk 버전
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true // Jetpack Compose 사용 시
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8" // 사용하는 Compose Compiler 버전에 맞게 설정 (Kotlin 버전과 호환되어야 함)
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.ui.auth) // 필요하다면 추가

    // Jetpack Compose 의존성 (예시)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.compose.ui)
    //implementation(libs.androidx.compose.material) // 또는 libs.androidx.compose.material3
    //implementation(libs.androidx.compose.ui.tooling.preview)
    //debugImplementation(libs.androidx.compose.ui.tooling)

    // 기타 필요한 의존성들
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    // ...
    implementation(platform(libs.firebase.bom.v33140))
    implementation(libs.firebase.analytics)

}



