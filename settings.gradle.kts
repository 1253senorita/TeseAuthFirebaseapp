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
include(":quickstart-android-master")