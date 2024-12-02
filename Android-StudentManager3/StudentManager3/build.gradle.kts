// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
    }
    dependencies {
        val nav_version = "2.8.4"
        //val kotlin_version = "1.9.0"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
        //classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}