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
