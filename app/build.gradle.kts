// build.gradle.kts (Module: app)

plugins {
    id("com.android.application")
    id("com.google.gms.google-services") // חשוב!
}

android {
    namespace = "com.example.registerapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.registerapp"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
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
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:33.14.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.android.material:material:1.11.0")

    implementation(libs.firebase.database)
}
