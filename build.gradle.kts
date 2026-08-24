// 루트 빌드 스크립트 — 각 모듈(app, wear)에서 공통으로 쓸 플러그인 버전만 선언한다.
plugins {
    id("com.android.application") version "8.13.0" apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false
}
