pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://dl.google.com/dl/android/maven2") } // 필요한 저장소 추가

    }



}

rootProject.name = "TeseAuthFirebaseapp"
include(":app")
<<<<<<< HEAD
include(":quickstart-android-master")

project(":quickstart-android-master").projectDir = File("../quickstart-android-master") // 최신  추가
=======
//include(":quickstart-android-master")
include(":feature_firestore")
include(":feature_storage")
>>>>>>> e433a96 (메이져 브런치 인증2025.06.15)
