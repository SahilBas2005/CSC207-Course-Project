import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
}

val myToken = gradleLocalProperties(rootDir, providers).getProperty("token");
val cohereToken = gradleLocalProperties(rootDir, providers).getProperty("cohere-token")

android {
    namespace = "com.example.csc207courseproject"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.csc207courseproject"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "TOKEN", "\"${myToken}\"")
        buildConfigField("String", "COHERE", "\"${cohereToken}\"")
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}


dependencies {
    implementation("com.sun.net.httpserver:http:20070405")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.legacy.support.v4)
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.cohere:cohere-java:1.4.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.12.3")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.12.3")

}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
