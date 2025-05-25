plugins {
    id("com.android.application")
    id("com.google.gms.google-services") // Firebase 사용 시 필수
    id("org.jetbrains.kotlin.android") // Kotlin 플러그인 명확하게 지정
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.20"
}

android {
    namespace = "com.salsample.teseauthfirebaseapp"
    compileSdk = 35

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

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }

    packaging.resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
}

dependencies {
    implementation(platform(libs.firebase.bom.v33130))
    implementation(libs.google.firebase.auth)
    implementation(libs.google.firebase.analytics)
    implementation(libs.firebase.ui.auth.v900)

    implementation(libs.androidx.core.ktx.v1120)
    implementation(libs.androidx.appcompat.v161)
    implementation(libs.material.v1110)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.constraintlayout.v214)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.v115)
    androidTestImplementation(libs.androidx.espresso.core.v351)

    // Jetpack Compose 의존성
    implementation(platform(libs.androidx.compose.bom.v20240100))
    implementation(libs.ui)
    implementation(libs.androidx.material)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)

    // Facebook SDK (필요한 경우)
    implementation(libs.facebook.android.sdk.v8x)
    implementation(libs.facebook.login)
    implementation(libs.facebook.android.sdk.v1600) // 최신 버전 확인 필요
}
