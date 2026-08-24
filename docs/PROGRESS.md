# DAVID 프로젝트 진행 기록
새 항목은 위에 쌓는다 (최신이 위로).

<!--
아래 형식으로 작업 후 기록한다 (HANDOFF.md 14번 참고):

## YYYY-MM-DD — 작업 제목
- 한 일:
- 변경한 파일:
- 확인: (성공/실패, 기기/환경)
- 결정 또는 위험:
- 다음:
-->

## 2026-08-24 — ROADMAP.md 신규 작성 (모듈형 아키텍처·음성 우선·자비스 지향)
- 한 일: 아버지가 세션 중 구두로 설명한 프로젝트 방향(아들 아이디어)을 `docs/ROADMAP.md`로 처음 문서화. 핵심 3원칙: (1) 모든 기능을 붙였다 뗄 수 있는 독립 모듈로 구성, (2) 워치·버즈·폰 모두에서 음성 명령을 기본 입력으로 지원(타이핑은 보조), (3) 최종 목표는 영화 <아이언맨>의 자비스처럼 음성으로 검색·지원·해결·수행. HANDOFF.md 2번 파일 구조에는 ROADMAP.md가 이미 있는 것처럼 적혀 있었지만 실제로는 이번에 처음 생성함(문서-실제 상태 불일치 정정).
- 변경한 파일: `docs/ROADMAP.md`(신규), `docs/PROGRESS.md`(이 기록)
- 확인: 문서 작성만 완료. 아버지 본인이 "아직 구체화는 부족하다"고 밝힌 대로, 모듈 경계·인터페이스·기능 우선순위 등 구체적인 설계는 전혀 결정되지 않았음 — ROADMAP.md에 "아직 정해지지 않은 것"으로 명시해둠.
- 결정 또는 위험: 이 방향(특히 웹 검색·캘린더/알림 실행 등)이 HANDOFF.md 4번 "의도적으로 구현하지 않은 기능" 목록과 겹치는 부분이 있어, 향후 그 목록을 이 로드맵에 맞게 조정할지 별도 논의가 필요함 (아직 결정 안 됨).
- 다음: 아버지·AI 세션에서 ROADMAP.md의 "아직 정해지지 않은 것" 항목을 하나씩 구체화

## 2026-08-24 — Gradle assembleDebug 빌드 실패 수정
- 한 일: `./gradlew :app:assembleDebug --stacktrace` 실행 시 발생한 두 가지 실제 에러를 로그로 확인 후 수정.
  1. 첫 번째 에러: `java.lang.IllegalArgumentException: 25.0.2` (`org.jetbrains.kotlin.com.intellij.util.lang.JavaVersion.parse`에서 발생, `KotlinCoreEnvironment` 생성 중 호출). 원인: 이 Codespace의 기본 JDK가 25.0.2(Microsoft 빌드)였는데, Gradle 8.13에 내장된 Kotlin DSL 스크립트 컴파일러가 `settings.gradle.kts`/`build.gradle.kts`를 해석하는 단계에서 "25.0.2" 버전 문자열을 파싱하지 못해 즉시 실패함. HANDOFF.md 8번 섹션이 "JDK 17이 아닌 다른 버전이 기본으로 잡혀있는 경우"로 미리 예측했던 바로 그 케이스였음을 이번에 직접 확인. sdkman으로 JDK 17(`17.0.20+1-ms`, Microsoft 빌드)을 설치하고 기본값으로 전환해 해결.
  2. 두 번째 에러: `SDK location not found. Define a valid SDK location with an ANDROID_HOME environment variable or by setting the sdk.dir path in your project's local properties file at '/workspaces/David/local.properties'.` 원인: 이 Codespace에 Android SDK 자체가 설치돼 있지 않았음. Android cmdline-tools(`commandlinetools-linux-13114758_latest.zip`)를 `/home/codespace/android-sdk`에 설치, 라이선스 전체 동의(`sdkmanager --licenses`), `platform-tools` / `platforms;android-36` / `build-tools;36.0.0` 설치. 빌드가 진행되며 AGP가 `build-tools;35.0.0`도 추가로 요구해 자동 설치됨(로그에서 직접 확인). `local.properties`에 `sdk.dir=/home/codespace/android-sdk` 기록.
  3. 위 두 가지를 고친 뒤에도 `dexBuilderDebug`~`mergeExtDexDebug` 단계에서 "Gradle build daemon disappeared unexpectedly"가 두 차례 재현됨. `ps aux`로 확인해보니 이전 세션들이 남긴 JDK 25 기반의 고아 Gradle 데몬 2개(gradle 9.2.0, gradle 8.13 각각 1개)가 이 2-vCPU 컨테이너에서 계속 CPU/메모리를 점유하고 있었음. `./gradlew --stop` + 남은 프로세스 강제 종료로 리소스를 확보한 뒤 재시도하니 성공. **다만 데몬이 사라진 정확한 원인(OOM 여부 등)은 확인하지 못함 — `dmesg`와 `/sys/fs/cgroup/memory.events`(`oom 0`, `oom_kill 0`)에 OOM 킬 기록이 없었음. 리소스 확보와 성공 재현이 시간상 일치했을 뿐, 인과관계는 확인 필요로 남김.**
- 변경한 파일: `local.properties`(신규 생성. `.gitignore`에 이미 등재돼 있어 Git에는 커밋되지 않음) / `.devcontainer/devcontainer.json`(신규) / `.devcontainer/setup-android-sdk.sh`(신규) / `docs/PROGRESS.md`, `docs/HANDOFF.md`(이 기록). 그 외 앱 소스나 Gradle 설정 파일은 변경 없음 — JDK 설치와 Android SDK 설치는 리포지토리 밖(Codespaces 컨테이너 환경) 조치였음.
- 확인: 성공. `./gradlew :app:assembleDebug --stacktrace` 최종 재실행 결과 `BUILD SUCCESSFUL in 51s` (37 actionable tasks: 12 executed, 25 up-to-date), `app/build/outputs/apk/debug/app-debug.apk` 생성 확인. **주의: 이건 "빌드/컴파일 성공"만 확인된 것이며, 에뮬레이터나 실기기에 앱을 설치·실행해본 것은 아님 — 실기기 테스트는 여전히 미수행 (HANDOFF.md 3번 표 참고).**
- 결정 또는 위험:
  - AGP/Gradle/compileSdk/Kotlin 버전은 **하나도 바꾸지 않음** — 원래대로 AGP 8.13.0, Gradle 8.13(wrapper), compileSdk 36, Kotlin 2.0.21 그대로 유지. 이번 실패는 전부 "코드가 실행되는 환경"(JDK, Android SDK 설치 여부) 문제였고 프로젝트 설정 파일 자체의 문제가 아니었음. 참고로 `app/build.gradle.kts`를 직접 읽어 확인한 결과 `targetSdk`도 36으로, HANDOFF.md 5번 섹션에 남아있던 "35" 표기는 이번에 정정함(아래 HANDOFF.md 변경 참고).
  - JDK 17 설치와 Android SDK 설치는 sdkman/수동 명령으로 **이번 Codespace 컨테이너에만** 적용됐던 상태라, 컨테이너가 새로 생성되면(새 Codespace, 컨테이너 재빌드) 같은 문제를 다시 겪을 위험이 있었음 — HANDOFF.md 8번이 예측했던 리스크가 실제로 재현된 것.
  - 같은 세션에서 `.devcontainer/devcontainer.json`(JDK 17 자동 설치, `ghcr.io/devcontainers/features/java:1` 사용) + `.devcontainer/setup-android-sdk.sh`(Android SDK 설치 자동화, `postCreateCommand`로 실행)를 추가함.
- 확인(추가, 같은 날): `devcontainer.json`이 실제 새 Codespace를 띄워야 완전히 검증되지만, 그건 이 세션에서 직접 만들 수 없어서 대신 **`setup-android-sdk.sh`를 완전히 빈 `ANDROID_HOME`에 대고 실제로 실행**해 처음부터 끝까지(cmdline-tools 다운로드 → 라이선스 동의 → platform-tools/platforms;android-36/build-tools 36·35 설치 → `local.properties` 작성) 재현했다. 첫 실행에서 실제로 실패하는 버그를 잡았다: `set -euo pipefail` 상태에서 `yes | sdkmanager ... --licenses`가 `yes`의 SIGPIPE(exit 141)를 파이프 실패로 간주해 스크립트가 라이선스 동의 단계에서 죽었음(로그: `[setup-android-sdk] Accepting SDK licenses` 이후 중단, exit 141). `|| true`를 추가해 수정 후 같은 방식으로 재실행하니 `SCRIPT_EXIT:0`으로 끝까지 성공. 테스트 후 실제 `local.properties`(`sdk.dir=/home/codespace/android-sdk`)로 복구하고 `./gradlew :app:assembleDebug`가 여전히 `BUILD SUCCESSFUL`인 것도 재확인했다. **JDK 17 설치 부분(`ghcr.io/devcontainers/features/java:1`)은 sdkman으로 JDK를 설치하는 방식이라 이 세션에서 이미 검증된 절차(JDK 17.0.20+1-ms 설치·전환 성공)와 동일하지만, feature 자체를 통한 설치는 실제 새 컨테이너 빌드로 아직 확인 못함 — 확인 필요로 남김.**
- 다음:
  - 다음에 실제로 새 Codespace를 생성할 때 `devcontainer.json`의 Java feature가 문제없이 도는지 최종 확인(스크립트 쪽은 이미 처음부터 끝까지 재현 검증 완료)
  - 에뮬레이터 또는 실기기에 `app-debug.apk`를 설치해 HANDOFF.md 6번 3~4단계(텍스트 입력 → 음성 버튼 → TTS 응답) 확인
  - `wear` 모듈(`:wear:assembleDebug`)도 같은 환경에서 빌드되는지 아직 미확인

## 2026-08-22 — Gradle 프로젝트 골격 생성 (app/wear 모듈)
- 한 일: `D:\...\DAVID` 루트에 있던 4개 소스 파일(`DavidApp.kt`, `MainActivity.kt`, `SimpleRecognitionListener.kt`, `WearRequestListenerService.kt`)의 실제 내용을 읽어 HANDOFF.md 설명과 일치함을 확인. 이후 정식 Android Gradle 프로젝트 골격(app/wear 모듈, AndroidManifest.xml, build.gradle.kts, settings.gradle.kts, res 리소스)을 새로 작성하고 기존 4개 파일을 `app/src/main/java/com/david/assistant/`로 정리. 워치 쪽 발신 코드가 없던 것을 확인하고 `wear/.../WearMainActivity.kt`(신규)를 작성해 "/david/open" 메시지 발신 기능을 채움. GitHub 저장소(`github.com/Dalbonz/David`, Public, 비어있음) 확인.
- 변경한 파일(신규): 루트 `settings.gradle.kts`, `build.gradle.kts`, `gradle.properties`, `.gitignore` / `app/build.gradle.kts`, `app/src/main/AndroidManifest.xml`, `app/src/main/res/values/{strings,themes}.xml` / `wear/build.gradle.kts`, `wear/src/main/AndroidManifest.xml`, `wear/src/main/java/com/david/assistant/wear/WearMainActivity.kt`, `wear/src/main/res/values/{strings,themes}.xml`. 기존 4개 .kt 파일은 내용 변경 없이 위치만 이동.
- 확인: 실제 Gradle 빌드/싱크는 **미실행** — 이 세션은 Google Maven(`dl.google.com`)과 Gradle 배포 서버(`services.gradle.org`)에 네트워크 접근이 막혀 있어 로컬에서 직접 빌드 검증을 할 수 없었음. 대신 각 설정값(AGP 8.13.0, Gradle 8.13, compileSdk 36, Kotlin 2.0.21 등)은 Android 공식 릴리스 노트를 웹 검색으로 확인해 반영. **Codespaces에서 첫 Gradle sync가 이 골격에 대한 최초의 실제 검증**이 됨.
- 결정 또는 위험:
  - **Gradle Wrapper 미생성** — `gradlew`/`gradlew.bat`/`gradle-wrapper.jar`는 바이너리라 이 세션에서 안전하게 만들 수 없어 포함하지 않음. Codespaces에서 최초 1회 `gradle wrapper --gradle-version 8.13` 실행 필요 (아래 '다음' 참고).
  - `D:\...\DAVID` 루트에 있던 원본 4개 .kt 파일은 이제 `app/src/main/java/...` 아래로 복사됐고, 루트의 원본은 중복 상태로 남아있음 — Claude가 사용자 기기 파일을 삭제할 수 없어 수동 정리 필요.
  - targetSdk를 문서상 35에서 36(휴대폰 모듈)으로 올림 — Google Play 정책(2026-08-31 API 36 요구, HANDOFF.md 10번) 선반영, 지금 당장 영향 없음. wear 모듈은 35 유지.
  - AGP는 최신인 9.x 대신 한 단계 아래인 안정 버전 8.13.0을 의도적으로 선택 (9.x는 이번 세션에서 실제 빌드 검증이 불가능해 리스크가 더 큼).
- 다음: (1) Codespaces에서 저장소 클론 후 `gradle wrapper --gradle-version 8.13` 실행 → 첫 Gradle sync 시도, (2) 루트에 남은 원본 4개 .kt 파일 수동 삭제, (3) `git init` → GitHub(`Dalbonz/David`)에 최초 push, (4) MainActivity/SimpleRecognitionListener의 무음 실패 이슈(음성인식 미지원/오류 시 사용자 피드백 없음) 코드 수정은 아직 미반영 — 다음 세션에서 처리.

## 2026-08-22 — 인수인계서 v3 작성 및 로컬 폴더 세팅
- 한 일: 기존 HANDOFF.md 내용을 분석하고 v2(성공 기준·협업 역할 추가) → v3(개발 환경 분리 방식 확정)로 갱신. `D:\01-개인 파일\엄빠\아빠\AX\DAVID` 폴더에 문서 정본을 배치.
- 변경한 파일: docs/HANDOFF.md(신규 작성), docs/PROGRESS.md(신규 작성), docs/claude-review-2026-08-22.md(신규 작성)
- 확인: 문서 작성만 완료. Git 저장소 초기화, 실제 `.kt` 소스 파일 이관, Android Studio 빌드는 아직 미완료.
- 결정 또는 위험: 아들은 채팅(Cowork) 기반, 아버지는 GitHub Codespaces 기반으로 작업 방식을 분리하기로 결정. 두 방식 동기화는 Git으로 하기로 함 (저장소 미생성 상태).
- 다음: 실제 소스 코드 위치 확인 → 이 폴더로 이관 또는 신규 시작 결정, GitHub 저장소 생성, git init/push 검증.

## 2026-08-24 — GitHub 최초 push 완료
- 한 일: Codespaces 웹 업로드로 DAVID.zip을 올린 뒤 압축 해제, 정리, git add/commit/push로 GitHub(main 브랜치)에 최초 반영 완료.
- 확인: raw.githubusercontent.com에서 settings.gradle.kts, docs/PROGRESS.md 내용 직접 대조하여 성공 확인.
- 다음: gradle wrapper 생성 → 첫 Gradle sync 시도 → Claude Code 설치해 이어서 진행.
