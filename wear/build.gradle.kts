plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.david.assistant.wear"
    compileSdk = 36

    defaultConfig {
        // 휴대폰 앱과 같은 applicationId를 써서 Data Layer 페어링을 단순하게 유지한다 (HANDOFF.md 5번 참고).
        applicationId = "com.david.assistant"
        minSdk = 30 // Wear OS 3
        targetSdk = 35 // Wear OS 앱은 완화된 기준 적용 (HANDOFF.md 10번 참고)
        versionCode = 1
        versionName = "0.1"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.wear.compose:wear-compose-material:1.4.0")
    implementation("androidx.wear.compose:wear-compose-foundation:1.4.0")

    // 휴대폰에 "/david/open" 메시지를 보내기 위함
    implementation("com.google.android.gms:play-services-wearable:18.2.0")
}
