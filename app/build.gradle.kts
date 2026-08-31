import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

// local.properties는 .gitignore에 있어 Git에 올라가지 않는다 — API 키는 반드시 여기서만 읽는다.
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use { load(it) }
}
val claudeApiKey: String = localProperties.getProperty("CLAUDE_API_KEY", "")

android {
    namespace = "com.david.assistant"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.david.assistant"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "0.1"
        buildConfigField("String", "CLAUDE_API_KEY", "\"$claudeApiKey\"")
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
        buildConfig = true
    }

    // anthropic-java가 끌고 오는 Apache HttpComponents 등에서 META-INF 메타파일이 중복돼 충돌하는 것을 막는다.
    packaging {
        resources {
            excludes += setOf(
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/LICENSE.md",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/INDEX.LIST",
            )
        }
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")

    // 워치가 보낸 Data Layer 메시지를 수신하기 위함 (WearRequestListenerService.kt)
    implementation("com.google.android.gms:play-services-wearable:18.2.0")

    // ClaudeBrain이 Anthropic API를 호출하기 위함
    implementation("com.anthropic:anthropic-java:2.34.0")
}
